package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.Message.toController.InfoRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.InfoResponse;
import it.polimi.ProgettoIngSW2019.common.Message.toView.Message;
import it.polimi.ProgettoIngSW2019.common.Message.toView.MessageScorePlayer;
import it.polimi.ProgettoIngSW2019.common.Message.toView.ScorePlayerWhoHit;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.enums.SquareType;
import it.polimi.ProgettoIngSW2019.custom_exception.IllegalAttributeException;
import it.polimi.ProgettoIngSW2019.model.Deck;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.model.TurnManager;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;

import java.util.ArrayList;
import java.util.List;

/**
 * EndTurnController1 class
 * score dead players
 * reset map
 */
public class EndTurnController extends Controller {
    private Deck weaponDeck;
    private Deck ammoDeck;
    private Player ownerPlayer;
    private List<MessageScorePlayer> messageScorePlayerList = new ArrayList<>();
    private List<Player> deadPlayers = new ArrayList<>();


    /**
     * Constructor
     * @param turnManager               TurnManager
     * @param virtualView               VirtualView
     * @param idConverter               IdConverter
     * @param createJson                CreateJson
     * @param hostNameCreateList        HostNameCreateList
     */
    public EndTurnController(TurnManager turnManager, VirtualView virtualView, IdConverter idConverter, CreateJson createJson, HostNameCreateList hostNameCreateList) {
        super(turnManager, virtualView, idConverter, createJson, hostNameCreateList);
        weaponDeck = turnManager.getGameTable().getWeaponDeck();
        ammoDeck = turnManager.getGameTable().getAmmoDeck();
    }


    /**
     * receive the message from the view
     * check type event
     * do actions based by type event
     *
     * @param event event sent
     */
    public void update(Event event) {
        if (event.getCommand().equals(EventType.REQUEST_ENDTURN_INFO)) {
            checkInfoFromView(event.getMessageInJsonFormat());
        }
    }


    /**
     * check if the data from view are correct
     * @param messageJson       message from view json
     */
    private void checkInfoFromView(String messageJson) {
        InfoRequest infoRequest = new Gson().fromJson(messageJson, InfoRequest.class);

        ownerPlayer = convertPlayer(infoRequest.getIdPlayer(), infoRequest.getHostNamePlayer());

        if(ownerPlayer != null) {
            if(checkHostNameCorrect(ownerPlayer, infoRequest.getHostNamePlayer())) {
                if (checkCurrentPlayer(ownerPlayer)) {
                    if (checkNoActionLeft(ownerPlayer)) {
                        checkScore();

                        if(!deadPlayers.isEmpty()) {
                            for (Player dp : deadPlayers) {
                                String message = "";
                                sendInfo(EventType.MSG_PLAYER_SPAWN, message, getHostNameCreateList().addOneHostName(dp));
                            }
                        }
                        //if the game has to come to its end
                        if(getTurnManager().getGameTable().isKillshotTrackFull()){
                            new FinalScoreController(getTurnManager(), getVirtualView(), getIdConverter(),
                                    getCreateJson(), getHostNameCreateList()).endGame();
                        }
                        else{
                            resetMap();
                            getTurnManager().changeCurrentPlayer();

                            sendInfo(EventType.UPDATE_MAP, getCreateJson().createMapLMJson(), getHostNameCreateList().addAllHostName());

                            Player player = getTurnManager().getCurrentPlayer();
                            sendInfo(EventType.MSG_NEW_TURN, getCreateJson().createMessageJson(player), getHostNameCreateList().addAllHostName());

                            if(player.getPosition() == null) {
                                sendInfo(EventType.MSG_FIRST_TURN_PLAYER, getCreateJson().createMessageJson(player), getHostNameCreateList().addOneHostName(player));
                            }
                            else {
                                String mess = "";
                                sendInfo(EventType.MSG_BEFORE_ENEMY_ACTION_OR_RELOAD, mess, getHostNameCreateList().addAllExceptOneHostName(ownerPlayer));

                                //inside there is the message for Nick
                                msgActionLeft(ownerPlayer);
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * check if there is a player dead
     * and some double kill
     *
     */
    private void checkScore() {
        deadPlayers = getTurnManager().checkDeadPlayers();

        if(!deadPlayers.isEmpty()) {
            boolean doubleKill;
            doubleKill = getTurnManager().assignDoubleKillPoint();

            if(doubleKill) {
                String msgDoubleKill = new Gson().toJson(new Message(ownerPlayer.getIdPlayer(), ownerPlayer.getCharaName()));
                sendInfo(EventType.MSG_DOUBLEKILL, msgDoubleKill, getHostNameCreateList().addAllHostName());
            }


            for(Player deadPlayer: deadPlayers) {
                String lastHitName = deadPlayer.getDamageLine().get(deadPlayer.getDamageLine().size() - 1);
                Player player = getTurnManager().getPlayerFromCharaName(lastHitName);
                if(player.getIdPlayer() != ownerPlayer.getIdPlayer())
                    throw new IllegalAttributeException("Something wrong with the killer");
            }

            scoreDeadPlayers();

            if(messageScorePlayerList == null)
                throw new NullPointerException("scorePlayersList cannot be null");

            //send the scores infoRequest to all players
            String messageScorePlayerListJson = new Gson().toJson(messageScorePlayerList);
            messageScorePlayerList.clear();
            sendInfo(EventType.SCORE_DEAD_PLAYERS, messageScorePlayerListJson, getHostNameCreateList().addAllHostName());

            sendInfo(EventType.UPDATE_KILLSHOTTRACK, getCreateJson().createKillShotTrackLMJson(), getHostNameCreateList().addAllHostName());
        }
    }



    /**
     * if there is a player dead count the score of each player who damage the dead ones
     */
    private void scoreDeadPlayers() {
        if(deadPlayers == null)
            throw new NullPointerException("Something wrong, deadPlayers cannot be null");

        if(deadPlayers.isEmpty())
            throw new IllegalArgumentException("Something wrong deadPlayers cannot be empty");

        String deadNamePlayer;
        String killerNamePlayer = ownerPlayer.getCharaName();
        String firstBloodNamePlayer;
        int[] scorePlayers;
        List<ScorePlayerWhoHit> scorePlayersWhoHits = new ArrayList<>();
        int nSkullsDeadPlayer;

        for (Player deadPlayer : deadPlayers) {
            deadNamePlayer = deadPlayer.getCharaName();
            firstBloodNamePlayer = getTurnManager().assignFirstBlood(deadPlayer).getCharaName();

            //mi viene restituito una lista di interi
            //gli interi sono i punti che ricevono i player dallo score della damage line del player morto
            //gli indici corrisponsono agli id dei player
            scorePlayers = getTurnManager().scoreDamageLineOf(deadPlayer);

            for (int i = 0; i < scorePlayers.length; i++) {
                if(scorePlayers[i] > 0) {
                    Player player = convertPlayerWithId(i);

                    if (player != null) {
                        String namePlayer = player.getCharaName();
                        scorePlayersWhoHits.add(new ScorePlayerWhoHit(namePlayer, scorePlayers[i]));
                    }
                    else
                        throw new IllegalAttributeException("The id player is wrong!");
                }
            }

            deadPlayer.increaseNumberOfSkulls();
            nSkullsDeadPlayer = deadPlayer.getNumberOfSkulls();

            getTurnManager().getGameTable().addTokenOnKillshotTrack(deadPlayer, ownerPlayer);
            messageScorePlayerList.add(new MessageScorePlayer(ownerPlayer.getIdPlayer(), deadNamePlayer, killerNamePlayer, firstBloodNamePlayer, scorePlayersWhoHits, nSkullsDeadPlayer));
        }
    }





    /**
     * reset map
     * add cards missed in the map
     */
    private void resetMap() {
        for(Square[] squareLine: getTurnManager().getGameTable().getMap()) {
            for (Square square : squareLine) {
                if (square != null) {
                    if (square.getSquareType() == SquareType.SPAWNING_POINT)
                        square.reset(weaponDeck);
                    if (square.getSquareType() == SquareType.AMMO_POINT)
                        square.reset(ammoDeck);
                }
            }
        }
    }

}