package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.Message.DoubleKillInfo;
import it.polimi.ProgettoIngSW2019.common.Message.Info;
import it.polimi.ProgettoIngSW2019.common.Message.ScorePlayersInfoResponse;
import it.polimi.ProgettoIngSW2019.common.Message.ScoreInfo;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.enums.SquareType;
import it.polimi.ProgettoIngSW2019.common.utilities.Observer;
import it.polimi.ProgettoIngSW2019.custom_exception.IllegalAttributeException;
import it.polimi.ProgettoIngSW2019.model.Deck;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.model.TurnManager;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;

import java.util.ArrayList;
import java.util.List;

/**
 * EndTurnController class
 * score dead players
 * reset map
 */
public class EndTurnController extends Controller implements Observer<Event> {
    private Deck weaponDeck;
    private Deck ammoDeck;
    private Player ownerPlayer;



    /**
     * Constructor
     * @param turnManager   turnManager
     * @param idConverter   idConverter
     * @param virtualView   virtualView
     * @param createJson    createJson
     */
    public EndTurnController(TurnManager turnManager, IdConverter idConverter, VirtualView virtualView, CreateJson createJson) {
        super(turnManager, idConverter, virtualView, createJson);
        weaponDeck = turnManager.getGameTable().getWeaponDeck();
        ammoDeck = turnManager.getGameTable().getAmmoDeck();
    }


    /**
     * receive the message from the view
     * check type event
     * do actions based by type event
     * @param event event sent
     */
    public void update(Event event) {
        if(event.getCommand().equals(EventType.REQUEST_ENDTURN_INFO))
            endTurn(event.getMessageInJsonFormat());
    }


    /**
     * @param messageJson json message
     */
    public void endTurn(String messageJson) {
        List<ScorePlayersInfoResponse> scorePlayersInfoResponses;
        Event event;
        boolean doubleKill;
        String msgDoubleKill;

        Info info = new Gson().fromJson(messageJson, Info.class);

        if(info.getIdPlayer() != getTurnManager().getCurrentPlayer().getIdPlayer())
            throw new IllegalAttributeException("It is not Player: " + info.getIdPlayer() + " turn");

        ownerPlayer = getIdConverter().getPlayerById(info.getIdPlayer());
        List<Player> deadPlayers = getTurnManager().checkDeadPlayers();

        if(deadPlayers != null) {
            scorePlayersInfoResponses = scoreDeadPlayers(deadPlayers);

            if(scorePlayersInfoResponses == null)
                throw new NullPointerException("scorePlayersList cannot be null");

            //TODO: inviare a tutti i client
            String scorePlayersListJson = new Gson().toJson(scorePlayersInfoResponses);
            event = new Event(EventType.SCORE_DEAD_PLAYERS, scorePlayersListJson);
            //getVirtualView().sendMessage(event);

            doubleKill = getTurnManager().assignDoubleKillPoint();
            msgDoubleKill = new Gson().toJson(new DoubleKillInfo(ownerPlayer.getIdPlayer(), ownerPlayer.getCharaName(), doubleKill));
            event = new Event(EventType.DOUBLE_KILL, msgDoubleKill);
            //getVirtualView().sendMessage(event);


            for(Player player: deadPlayers) {
                //TODO: passo in spawn state tutti i player morti
            }
        }
        else {
            //if there are not dead players
            String endTurnInfoJson = new Gson().toJson(new Info(ownerPlayer.getIdPlayer()));
            sendInfo(EventType.RESPONSE_REQUEST_ENDTURN_INFO, endTurnInfoJson);
        }

        resetMap();
        //TODO: RESTITUIRE NUOVA MAPPA
    }




    public List<ScorePlayersInfoResponse> scoreDeadPlayers(List<Player> deadPlayers) {
        List<ScorePlayersInfoResponse> scorePlayersList = new ArrayList<>();
        String deadNamePlayer;
        String killerNamePlayer = ownerPlayer.getCharaName();
        String firstBloodNamePlayer;
        int[] scorePlayers;
        List<ScoreInfo> scorePlayersWhoHits = new ArrayList<>();
        int nSkullsDeadPlayer;

        for (Player player : deadPlayers) {
            deadNamePlayer = player.getCharaName();
            firstBloodNamePlayer = getTurnManager().assignFirstBlood(player).getCharaName();

            //mi viene restituito una lista di interi
            //gli interi sono i punti che ricevono i player dallo score della damage line del player morto
            //gli indici corrisponsono agli id dei player
            scorePlayers = getTurnManager().scoreDamageLineOf(player);

            for (int i = 0; i < scorePlayers.length; i++) {
                if(scorePlayers[i] > 0) {
                    String namePlayer = getIdConverter().getPlayerById(i).getCharaName();
                    scorePlayersWhoHits.add(new ScoreInfo(namePlayer, scorePlayers[i]));
                }
            }

            player.increaseNumberOfSkulls();
            nSkullsDeadPlayer = player.getNumberOfSkulls();

            getTurnManager().getGameTable().addTokenOnKillshotTrack(player, ownerPlayer);
            scorePlayersList.add(new ScorePlayersInfoResponse(ownerPlayer.getIdPlayer(), deadNamePlayer, killerNamePlayer, firstBloodNamePlayer, scorePlayersWhoHits, nSkullsDeadPlayer));
        }
        return scorePlayersList;
    }


    /**
     * reset map
     * add cards missed in the map
     */
    public void resetMap() {
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