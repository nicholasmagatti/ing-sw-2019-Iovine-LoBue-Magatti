package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.Message.toController.InfoRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toController.SpawnChoiceRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.SpawnChoiceResponse;
import it.polimi.ProgettoIngSW2019.common.Message.toView.DrawCardsInfo;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.enums.SquareType;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.custom_exception.IllegalAttributeException;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.utilities.Observer;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;

import java.util.ArrayList;
import java.util.List;

/**
 * Class SpawnController
 * @author Priscilla Lo Bue
 */
public class SpawnController extends Controller implements Observer<Event> {
    private Square spawnPos;
    private Player spawnPlayer;
    private PowerUp powerUpToDiscard;
    private List<PowerUp> powerUpsDraw = new ArrayList<>();


    /**
     * Constructor
     * @param turnManager               turn manager
     * @param idConverter               id converter
     * @param virtualView               virtual view
     * @param createJson                create json/LM
     * @param idPlayersCreateList       create id players list to send message
     */
    public SpawnController(TurnManager turnManager, IdConverter idConverter, VirtualView virtualView, CreateJson createJson, IdPlayersCreateList idPlayersCreateList) {
        super(turnManager, idConverter, virtualView, createJson, idPlayersCreateList);
    }


    /**
     * update to receive the events
     * @param event event from view
     */
    public void update(Event event) {
        if(event.getCommand().equals(EventType.REQUEST_SPAWN_CARDS)) {
            spawnDrawCard(event.getMessageInJsonFormat());
        }

        if(event.getCommand().equals(EventType.REQUEST_INITIAL_SPAWN_CARDS)) {
            spawnDrawTwoCards(event.getMessageInJsonFormat());
        }

        if(event.getCommand().equals(EventType.REQUEST_SPAWN)) {
            respawn(event.getMessageInJsonFormat());
        }
    }


    /**
     * spawn first time
     * @param messageJson message json from view with all info
     */
    public void spawnDrawTwoCards(String messageJson) {
        InfoRequest infoRequest = new Gson().fromJson(messageJson, InfoRequest.class);
        spawnPlayer = getIdConverter().getPlayerById(infoRequest.getIdPlayer());

        if(spawnPlayer.getPosition() != null)
            throw new IllegalAttributeException("The player: " + spawnPlayer.getIdPlayer() + "is not first spawn");

        //spawnPlayer draw due powerUp all'inizio del game per lo spawn
        powerUpsDraw.add((PowerUp) getTurnManager().getGameTable().getPowerUpDeck().drawCard());
        spawnPlayer.getPowerUps().add(powerUpsDraw.get(0));
        powerUpsDraw.add((PowerUp) getTurnManager().getGameTable().getPowerUpDeck().drawCard());
        spawnPlayer.getPowerUps().add(powerUpsDraw.get(1));

        //send message spawn player draw cards
        String messageMyPowerUpJson = getCreateJson().createMessageMyPowerUpJson(spawnPlayer, powerUpsDraw);
        sendInfo(EventType.MSG_DRAW_MY_POWERUP, messageMyPowerUpJson, getIdPlayersCreateList().addOneIdPlayers(spawnPlayer));

        //send spawn info to spawn player
        String spawnDrawCardsResponseJson = getCreateJson().createDrawCardsInfoJson(spawnPlayer);
        sendInfo(EventType.RESPONSE_REQUEST_INITIAL_SPAWN_CARDS, spawnDrawCardsResponseJson, getIdPlayersCreateList().addOneIdPlayers(spawnPlayer));

        //send message to all players except the spawn player
        String messageEnemyPowerUp = getCreateJson().createMessageEnemyPowerUpJson(spawnPlayer, powerUpsDraw.size());
        sendInfo(EventType.MSG_ENEMY_DRAW_POWERUP, messageEnemyPowerUp, getIdPlayersCreateList().addAllExceptPlayer(spawnPlayer));
    }



    /**
     * spawn when you are dead
     * @param messageJson   message json from view with all info
     */
    public void spawnDrawCard(String messageJson) {
        InfoRequest infoRequest = new Gson().fromJson(messageJson, InfoRequest.class);
        spawnPlayer = getIdConverter().getPlayerById(infoRequest.getIdPlayer());

        if(!spawnPlayer.isPlayerDown())
            throw new IllegalAttributeException("The Player: " + spawnPlayer.getIdPlayer() + "is not dead, cannot spawn");

        //clear the damage line of the player
        spawnPlayer.emptyDamageLine();

        //send new spawn player to all the players
        String playerLM = getCreateJson().createPlayerLMJson(spawnPlayer);
        sendInfo(EventType.UPDATE_PLAYER_INFO, playerLM, getIdPlayersCreateList().addAllExceptPlayer(spawnPlayer));

        //draw 1 powerUp and add it on powerUps list of the spawnPlayer
        powerUpsDraw.add((PowerUp) getTurnManager().getGameTable().getPowerUpDeck().drawCard());
        spawnPlayer.getPowerUps().add(powerUpsDraw.get(0));

        //send message to spawn player draw card
        String messageMyPowerUpJson = getCreateJson().createMessageMyPowerUpJson(spawnPlayer, powerUpsDraw);
        sendInfo(EventType.MSG_DRAW_MY_POWERUP, messageMyPowerUpJson, getIdPlayersCreateList().addOneIdPlayers(spawnPlayer));

        //send spawn info to spawn player
        String spawnDrawCardsResponseJson = getCreateJson().createDrawCardsInfoJson(spawnPlayer);
        sendInfo(EventType.RESPONSE_REQUEST_SPAWN_CARDS, spawnDrawCardsResponseJson, getIdPlayersCreateList().addOneIdPlayers(spawnPlayer));

        //send message to all players draw card
        String messageEnemyPowerUp = getCreateJson().createMessageEnemyPowerUpJson(spawnPlayer, powerUpsDraw.size());
        sendInfo(EventType.MSG_ENEMY_DRAW_POWERUP, messageEnemyPowerUp, getIdPlayersCreateList().addAllExceptPlayer(spawnPlayer));
    }


    /**
     * respawn actions
     * @param messageJson   message json from view with all the info
     */
    public void respawn(String messageJson) {
        SpawnChoiceRequest spawnChoiceRequest = new Gson().fromJson(messageJson, SpawnChoiceRequest.class);
        spawnPlayer = getIdConverter().getPlayerById(spawnChoiceRequest.getIdPlayer());
        powerUpToDiscard = getIdConverter().getPowerUpCardById(spawnPlayer.getIdPlayer(), spawnChoiceRequest.getIdPowerUpToDiscard());

        if(!spawnPlayer.isPlayerDown())
            throw new IllegalAttributeException("The Player: " + spawnPlayer.getIdPlayer() + "is not dead, cannot spawn");

        if(powerUpToDiscard == null)
            throw new IllegalAttributeException("PowerUpToDiscard cannot be null");

        if(!spawnPlayer.getPowerUps().contains(powerUpToDiscard))
            throw new IllegalAttributeException("The player: " + spawnPlayer.getCharaName() + " with id: " + spawnPlayer.getIdPlayer() + " doesn't have that powerUp");


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
        String myPowerUpsLMJson = getCreateJson().createMyPowerUpsListLMJson(spawnPlayer);
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
