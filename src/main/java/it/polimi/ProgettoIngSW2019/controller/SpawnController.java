package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.Message.toController.InfoRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toController.SpawnChoiceRequest;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.enums.SquareType;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;

import java.util.ArrayList;
import java.util.Arrays;
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
     * @param turnManager               TurnManager
     * @param virtualView               VirtualView
     * @param idConverter               IdConverter
     * @param createJson                CreateJson
     * @param hostNameCreateList        HostNameCreateList
     */
    public SpawnController(TurnManager turnManager, VirtualView virtualView, IdConverter idConverter, CreateJson createJson, HostNameCreateList hostNameCreateList) {
        super(turnManager, virtualView, idConverter, createJson, hostNameCreateList);
    }


    /**
     * update to receive the events
     * @param event event from view
     */
    public void update(Event event) {
        if(event.getCommand().equals(EventType.REQUEST_SPAWN_CARDS)) {
            if(checkInfoFromView(event.getMessageInJsonFormat())) {
                if (!spawnPlayer.isPlayerDown()) {
                    String messageError = "ERROR: you are not dead, cannot spawn";
                    sendInfo(EventType.ERROR, messageError, getHostNameCreateList().addOneHostName(spawnPlayer));
                    return;
                }
                spawnDrawCard();
            }
        }

        if(event.getCommand().equals(EventType.REQUEST_INITIAL_SPAWN_CARDS)) {
            if(checkInfoFromView(event.getMessageInJsonFormat())) {
                if (spawnPlayer.getPosition() != null) {
                    String messageError = "ops! qualcosa è andato storto.";
                    sendInfo(EventType.ERROR, messageError, getHostNameCreateList().addOneHostName(spawnPlayer));
                    return;
                }
                spawnDrawTwoCards();
            }
        }

        if(event.getCommand().equals(EventType.REQUEST_SPAWN) || (event.getCommand().equals(EventType.REQUEST_INITIAL_SPAWN))) {
            checkRespawnFromView(event.getMessageInJsonFormat());

            if(event.getCommand().equals(EventType.REQUEST_INITIAL_SPAWN)) {
                String mess = "";
                sendInfo(EventType.MSG_BEFORE_ENEMY_ACTION_OR_RELOAD, mess, getHostNameCreateList().addAllExceptOneHostName(spawnPlayer));
                msgActionLeft(spawnPlayer);
            }
        }
    }


    /**
     * check if the data from view are correct
     * @param messageJson       json from view
     * @return                  true if the data from view are correct
     */
    private boolean checkInfoFromView(String messageJson) {
        InfoRequest infoRequest = new Gson().fromJson(messageJson, InfoRequest.class);
        spawnPlayer = convertPlayer(infoRequest.getIdPlayer(), infoRequest.getHostNamePlayer());

        if(spawnPlayer != null) {
            if(checkHostNameCorrect(spawnPlayer, infoRequest.getHostNamePlayer())) {
                return true;
            }
        }
        return false;
    }



    /**
     * spawn first time
     */
    private void spawnDrawTwoCards() {
        //spawnPlayer draw due powerUp all'inizio del game per lo spawn
        powerUpsDraw.clear();
        powerUpsDraw.add((PowerUp) getTurnManager().getGameTable().getPowerUpDeck().drawCard());
        powerUpsDraw.add((PowerUp) getTurnManager().getGameTable().getPowerUpDeck().drawCard());

        //send message spawn player draw cards
        String messageDrawMyPowerUpJson = getCreateJson().createMessageDrawMyPowerUpJson(spawnPlayer, powerUpsDraw);
        sendInfo(EventType.MSG_DRAW_MY_POWERUP, messageDrawMyPowerUpJson, getHostNameCreateList().addOneHostName(spawnPlayer));

        //send message to all players except the spawn player
        String messageEnemyDrawPowerUp = getCreateJson().createMessageEnemyDrawPowerUpJson(spawnPlayer, powerUpsDraw.size());
        sendInfo(EventType.MSG_ENEMY_DRAW_POWERUP, messageEnemyDrawPowerUp, getHostNameCreateList().addAllExceptOneHostName(spawnPlayer));

        //send spawn info to spawn player
        String spawnDrawCardsResponseJson = getCreateJson().createDrawCardsInfoJson(spawnPlayer, powerUpsDraw);
        sendInfo(EventType.RESPONSE_REQUEST_INITIAL_SPAWN_CARDS, spawnDrawCardsResponseJson, getHostNameCreateList().addOneHostName(spawnPlayer));
    }



    /**
     * spawn when you are dead
     */
    private void spawnDrawCard() {
        //clear the damage line of the player
        spawnPlayer.getDamageLine().clear();

        //send new spawn player to all the players
        String playerLM = getCreateJson().createPlayerLMJson(spawnPlayer);
        sendInfo(EventType.UPDATE_PLAYER_INFO, playerLM, getHostNameCreateList().addAllExceptOneHostName(spawnPlayer));

        powerUpsDraw.clear();

        //draw 1 powerUp and add it on powerUps list of the spawnPlayer
        powerUpsDraw.add((PowerUp) getTurnManager().getGameTable().getPowerUpDeck().drawCard());

        //send message spawn player draw cards
        String messageDrawMyPowerUpJson = getCreateJson().createMessageDrawMyPowerUpJson(spawnPlayer, powerUpsDraw);
        sendInfo(EventType.MSG_DRAW_MY_POWERUP, messageDrawMyPowerUpJson, getHostNameCreateList().addOneHostName(spawnPlayer));

        //send message to all players except the spawn player
        String messageEnemyDrawPowerUp = getCreateJson().createMessageEnemyDrawPowerUpJson(spawnPlayer, powerUpsDraw.size());
        sendInfo(EventType.MSG_ENEMY_DRAW_POWERUP, messageEnemyDrawPowerUp, getHostNameCreateList().addAllExceptOneHostName(spawnPlayer));

        if(!spawnPlayer.getPowerUps().isEmpty()) {
            powerUpsDraw.addAll(spawnPlayer.getPowerUps());
        }

        //send spawn info to spawn player
        String spawnDrawCardsResponseJson = getCreateJson().createDrawCardsInfoJson(spawnPlayer, powerUpsDraw);
        sendInfo(EventType.RESPONSE_REQUEST_SPAWN_CARDS, spawnDrawCardsResponseJson, getHostNameCreateList().addOneHostName(spawnPlayer));
    }


    /**
     * check if the info are correct from the view
     * @param messageJson       json message form view
     */
    private void checkRespawnFromView(String messageJson) {
        SpawnChoiceRequest spawnChoiceRequest = new Gson().fromJson(messageJson, SpawnChoiceRequest.class);

        if(spawnPlayer.getIdPlayer() == spawnChoiceRequest.getIdPlayer() && spawnPlayer.getHostname().equals(spawnChoiceRequest.getHostNamePlayer())) {

            boolean found = false;

            for(PowerUp p:powerUpsDraw) {
                if(spawnChoiceRequest.getIdPowerUpToDiscard() == p.getIdCard()) {
                    powerUpToDiscard = p;
                    found = true;
                }
            }

            if (found) {
                if (!spawnPlayer.isPlayerDown() || spawnPlayer.getPosition() != null) {
                    String messageError = "ERROR: Non puoi spawnare";
                    sendInfo(EventType.ERROR, messageError, getHostNameCreateList().addOneHostName(spawnPlayer));
                } else
                    respawn(spawnPlayer);
            }
            else {
                String messageError = "ERROR: Qualcosa è andato storto";
                sendInfo(EventType.ERROR, messageError, getHostNameCreateList().addOneHostName(spawnPlayer));
            }
        }
    }


    /**
     * respawn actions
     */
    private void respawn(Player player) {
        spawnPlayer = player;
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

        if(spawnPlayer.isPlayerDown())
            spawnPlayer.risePlayerUp();

        if(spawnPlayer.getPowerUps().contains(powerUpToDiscard)) {
            spawnPlayer.getPowerUps().remove(powerUpToDiscard);
        }
        else {
            powerUpsDraw.remove(powerUpToDiscard);
            for(PowerUp powerUp:powerUpsDraw) {
                if(!spawnPlayer.getPowerUps().contains(powerUp)) {
                    if(spawnPlayer.getNumberOfPoweUps() < 3)
                        spawnPlayer.getPowerUps().add(powerUp);
                }
            }
        }

        getTurnManager().getGameTable().getPowerUpDiscarded().add(powerUpToDiscard);

        String updateMapLMJson = getCreateJson().createMapLMJson();
        String updateMyPowerUpLMJson = getCreateJson().createMyPowerUpsLMJson(spawnPlayer);
        String msgPowerUpDiscardedLMJson = getCreateJson().createMessagePowerUpsDiscardedJson(spawnPlayer, Arrays.asList(powerUpToDiscard));

        sendInfo(EventType.MSG_POWERUP_DISCARDED_TO_SPAWN, msgPowerUpDiscardedLMJson, getHostNameCreateList().addAllHostName());
        sendInfo(EventType.UPDATE_MAP, updateMapLMJson, getHostNameCreateList().addAllHostName());
        sendInfo(EventType.UPDATE_MY_POWERUPS, updateMyPowerUpLMJson, getHostNameCreateList().addOneHostName(spawnPlayer));
    }


    /**
     * spawn for an inactive player
     * @param player    inactive player to spawn
     */
    public void spawnInactivePlayer(Player player) {
        powerUpsDraw.clear();

        //draw 1 powerUp and add it on powerUps list of the spawnPlayer
        powerUpsDraw.add((PowerUp) getTurnManager().getGameTable().getPowerUpDeck().drawCard());

        String messageEnemyDrawPowerUp = getCreateJson().createMessageEnemyDrawPowerUpJson(player, powerUpsDraw.size());
        sendInfo(EventType.MSG_ENEMY_DRAW_POWERUP, messageEnemyDrawPowerUp, getHostNameCreateList().addAllExceptOneHostName(player));

        powerUpToDiscard = powerUpsDraw.get(0);
    }
}
