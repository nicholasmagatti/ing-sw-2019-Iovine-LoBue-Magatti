package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.Message.toController.InfoRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toController.SpawnChoiceRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.SpawnChoiceResponse;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.enums.SquareType;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.custom_exception.IllegalIdException;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;

import java.util.ArrayList;
import java.util.List;

/**
 * Class SpawnController
 * @author Priscilla Lo Bue
 */
public class SpawnController extends Controller {
    private Square spawnPos;
    private Player spawnPlayer;
    private PowerUp powerUpToDiscard;
    private List<PowerUp> powerUpsDraw = new ArrayList<>();


    /**
     * Constructor
     * @param turnManager               turn manager
     * @param virtualView               virtual view
     */
    public SpawnController(TurnManager turnManager, VirtualView virtualView, IdConverter idConverter, CreateJson createJson, IdPlayersCreateList idPlayersCreateList) {
        super(turnManager, virtualView, idConverter, createJson, idPlayersCreateList);
    }


    /**
     * update to receive the events
     * @param event event from view
     */
    public void update(Event event) {
        if((event.getCommand().equals(EventType.REQUEST_SPAWN_CARDS) || (event.getCommand().equals(EventType.REQUEST_INITIAL_SPAWN_CARDS)))) {
            InfoRequest infoRequest = new Gson().fromJson(event.getMessageInJsonFormat(), InfoRequest.class);

            spawnPlayer = convertPlayer(infoRequest.getIdPlayer());

            if(spawnPlayer != null) {
                if (event.getCommand().equals(EventType.REQUEST_SPAWN_CARDS)) {
                    if(!spawnPlayer.isPlayerDown()) {
                        String messageError = "ERROR: you are not dead, cannot spawn";
                        sendInfo(EventType.ERROR, messageError, getIdPlayersCreateList().addOneIdPlayers(spawnPlayer));
                        return;
                    }
                    spawnDrawCard();
                }

                if (event.getCommand().equals(EventType.REQUEST_INITIAL_SPAWN_CARDS)) {
                    if(spawnPlayer.getPosition() != null) {
                        String messageError = "ops! qualcosa è andato storto.";
                        sendInfo(EventType.ERROR, messageError, getIdPlayersCreateList().addOneIdPlayers(spawnPlayer));
                        return;
                    }
                    spawnDrawTwoCards();
                }

                //send message spawn player draw cards
                String messageDrawMyPowerUpJson = getCreateJson().createMessageDrawMyPowerUpJson(spawnPlayer, powerUpsDraw);
                sendInfo(EventType.MSG_DRAW_MY_POWERUP, messageDrawMyPowerUpJson, getIdPlayersCreateList().addOneIdPlayers(spawnPlayer));

                //send message to all players except the spawn player
                String messageEnemyDrawPowerUp = getCreateJson().createMessageEnemyDrawPowerUpJson(spawnPlayer, powerUpsDraw.size());
                sendInfo(EventType.MSG_ENEMY_DRAW_POWERUP, messageEnemyDrawPowerUp, getIdPlayersCreateList().addAllExceptPlayer(spawnPlayer));
            }
        }


        if(event.getCommand().equals(EventType.REQUEST_SPAWN)) {
            checkRespawnFromView(event.getMessageInJsonFormat());
        }
    }



    /**
     * spawn first time
     */
    public void spawnDrawTwoCards() {
        //spawnPlayer draw due powerUp all'inizio del game per lo spawn
        powerUpsDraw.clear();
        powerUpsDraw.add((PowerUp) getTurnManager().getGameTable().getPowerUpDeck().drawCard());
        spawnPlayer.getPowerUps().add(powerUpsDraw.get(0));
        powerUpsDraw.add((PowerUp) getTurnManager().getGameTable().getPowerUpDeck().drawCard());
        spawnPlayer.getPowerUps().add(powerUpsDraw.get(1));

        //send spawn info to spawn player
        String spawnDrawCardsResponseJson = getCreateJson().createDrawCardsInfoJson(spawnPlayer);
        sendInfo(EventType.RESPONSE_REQUEST_INITIAL_SPAWN_CARDS, spawnDrawCardsResponseJson, getIdPlayersCreateList().addOneIdPlayers(spawnPlayer));
    }



    /**
     * spawn when you are dead
     */
    public void spawnDrawCard() {
        //clear the damage line of the player
        spawnPlayer.getDamageLine().clear();

        //send new spawn player to all the players
        String playerLM = getCreateJson().createPlayerLMJson(spawnPlayer);
        sendInfo(EventType.UPDATE_PLAYER_INFO, playerLM, getIdPlayersCreateList().addAllExceptPlayer(spawnPlayer));

        powerUpsDraw.clear();

        //draw 1 powerUp and add it on powerUps list of the spawnPlayer
        powerUpsDraw.add((PowerUp) getTurnManager().getGameTable().getPowerUpDeck().drawCard());
        spawnPlayer.getPowerUps().add(powerUpsDraw.get(0));

        //send spawn info to spawn player
        String spawnDrawCardsResponseJson = getCreateJson().createDrawCardsInfoJson(spawnPlayer);
        sendInfo(EventType.RESPONSE_REQUEST_SPAWN_CARDS, spawnDrawCardsResponseJson, getIdPlayersCreateList().addOneIdPlayers(spawnPlayer));
    }


    /**
     * check if the info are correct from the view
     * @param messageJson       json message form view
     */
    public void checkRespawnFromView(String messageJson) {
        SpawnChoiceRequest spawnChoiceRequest = new Gson().fromJson(messageJson, SpawnChoiceRequest.class);

        spawnPlayer = convertPlayer(spawnChoiceRequest.getIdPlayer());

        if(spawnPlayer != null) {

            powerUpToDiscard = convertPowerUp(spawnPlayer, spawnChoiceRequest.getIdPowerUpToDiscard());

            if(powerUpToDiscard != null) {

                if(checkContainsPowerUp(spawnPlayer, powerUpToDiscard)) {
                    if (!spawnPlayer.isPlayerDown() || spawnPlayer.getPosition() != null) {
                        String messageError = "ERROR: Non puoi spawnare";
                        sendInfo(EventType.ERROR, messageError, getIdPlayersCreateList().addOneIdPlayers(spawnPlayer));
                    } else
                        respawn();
                }
            }
        }
    }


    /**
     * respawn actions
     */
    public void respawn() {
        //save ammo color and associated color with idRoom
        AmmoType colorToSpawn = powerUpToDiscard.getGainAmmoColor();
        int idRoom = AmmoType.intFromAmmoType(colorToSpawn);

        //search the square to spawn
        Square[][] map = getTurnManager().getGameTable().getMap();
        boolean found = false;

        for(int i = 0; i < GeneralInfo.ROWS_MAP; i++) {
            for(int j = 0; j < GeneralInfo.COLUMNS_MAP && !found; j++) {
                if((map[i][j].getSquareType() == SquareType.SPAWNING_POINT) && (map[i][j].getIdRoom() == idRoom)) {
                    spawnPos = map[i][j];
                    found = true;
                }
            }
        }

        //move the player into chosen square
        spawnPlayer.moveTo(spawnPos);

        //rise up the spawnPlayer
        spawnPlayer.risePlayerUp();

        //remove powerUp from powerUp list of spawnPlayer and put it in PowerUpDiscarded
        spawnPlayer.getPowerUps().remove(powerUpToDiscard);
        getTurnManager().getGameTable().getPowerUpDiscarded().add(powerUpToDiscard);

        //send spawn info to all the players
        String spawnChoiceResponse = createSpawnChoiceResponseJson(spawnPlayer, powerUpToDiscard);
        sendInfo(EventType.RESPONSE_REQUEST_SPAWN, spawnChoiceResponse, getIdPlayersCreateList().addOneIdPlayers(spawnPlayer));
        sendInfo(EventType.MSG_ENEMY_SPAWN, spawnChoiceResponse, getIdPlayersCreateList().addAllExceptPlayer(spawnPlayer));

        //send my powerUpsLM to the player
        String myPowerUpsLMJson = getCreateJson().createMyPowerUpsLMJson(spawnPlayer);
        sendInfo(EventType.UPDATE_MY_POWERUPS, myPowerUpsLMJson, getIdPlayersCreateList().addOneIdPlayers(spawnPlayer));
    }



    /**
     * Create SpawnChoiceResponse json
     * @param player        spawn player
     * @param powerUp       powerUp to discard
     * @return              json
     */
    public String createSpawnChoiceResponseJson(Player player, PowerUp powerUp) {
        if(player == null)
            throw new NullPointerException("Player cannot be null");

        if(powerUp == null)
            throw new NullPointerException("PowerUp card cannot be null");

        SpawnChoiceResponse spawnChoiceResponse = new SpawnChoiceResponse(player.getIdPlayer(), getCreateJson().createPowerUpLM(powerUp), getCreateJson().createMapLM(), getCreateJson().createPlayerLM(player));
        return new Gson().toJson(spawnChoiceResponse);
    }
}
