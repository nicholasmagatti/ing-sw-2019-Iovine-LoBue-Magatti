package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.Message.toController.InfoRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toController.SpawnChoiceRequest;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.enums.SquareType;
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


    public SpawnController(TurnManager turnManager, IdConverter idConverter, VirtualView virtualView, CreateJson createJson, IdPlayersCreateList idPlayersCreateList) {
        super(turnManager, idConverter, virtualView, createJson, idPlayersCreateList);
    }



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

        //Send powerUp list draw cards to the spawn player
        String powerUpsJsonList = getCreateJson().createPowerUpsListLMJson(spawnPlayer.getPowerUps());
        sendInfo(EventType.RESPONSE_REQUEST_INITIAL_SPAWN_CARDS, powerUpsJsonList, getIdPlayersCreateList().addOneIdPlayers(spawnPlayer));

        //send message to all players except the spawn player
        String messageEnemyPowerUp = getCreateJson().createMessageEnemyPowerUp(spawnPlayer, powerUpsDraw.size());
        sendInfo(EventType.MSG_ENEMY_DRAW_POWERUP, messageEnemyPowerUp, getIdPlayersCreateList().addAllExceptPlayer(spawnPlayer));
    }



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

        //send message to all players draw card
        String messageEnemyPowerUp = getCreateJson().createMessageEnemyPowerUp(spawnPlayer, powerUpsDraw.size());
        sendInfo(EventType.MSG_ENEMY_DRAW_POWERUP, messageEnemyPowerUp, getIdPlayersCreateList().addAllExceptPlayer(spawnPlayer));

        //send powerUps in hand to spawn player
        String powerUpsJsonList = getCreateJson().createPowerUpsListLMJson(spawnPlayer.getPowerUps());
        sendInfo(EventType.RESPONSE_REQUEST_SPAWN_CARDS, powerUpsJsonList, getIdPlayersCreateList().addOneIdPlayers(spawnPlayer));
    }



    public void respawn(String messageJson) {
        SpawnChoiceRequest spawnChoiceRequest = new Gson().fromJson(messageJson, SpawnChoiceRequest.class);
        spawnPlayer = getIdConverter().getPlayerById(spawnChoiceRequest.getIdPlayer());
        powerUpToDiscard = getIdConverter().getPowerUpCardById(spawnPlayer.getIdPlayer(), spawnChoiceRequest.getIdPowerUpToDiscard());

        if(!spawnPlayer.isPlayerDown())
            throw new IllegalAttributeException("The Player: " + spawnPlayer.getIdPlayer() + "is not dead, cannot spawn");

        if(!spawnPlayer.getPowerUps().contains(powerUpToDiscard))
            throw new IllegalAttributeException("The player: " + spawnPlayer.getCharaName() + " with id: " + spawnPlayer.getIdPlayer() + " doesn't have that powerUp");

        //send to all players info of the powerUp to discard
        String powerUpLMJason = getCreateJson().createPowerUpLMJson(powerUpToDiscard);
        sendInfo(EventType.MSG_SHOW_ENEMY_POWERUPS, powerUpLMJason, getIdPlayersCreateList().addAllIdPlayers());


        //save ammo color and associated color with idRoom
        AmmoType colorToSpawn = powerUpToDiscard.getGainAmmoColor();
        int idRoom = AmmoType.intFromAmmoType(colorToSpawn);

        Square[][] map = getTurnManager().getGameTable().getMap();
        boolean found = false;

        for(int i = 0; i < map.length && !found; i++) {
            for(int j = 0; j < map[0].length && !found; j++) {
                if((map[i][j].getSquareType() == SquareType.SPAWNING_POINT) && (map[i][j].getIdRoom() == idRoom)) {
                    spawnPos = map[i][j];
                    found = true;
                }
            }
        }

        //move the player into chosen square
        spawnPlayer.moveTo(spawnPos);

        //TODO: forse non è necessario, mando tutta la mappa
        //took the coordinates of the player and send it to all players
        int[] coordinates = spawnPos.getCoordinates(map);
        String spawnChoiceResponseJson = getCreateJson().createSpawnChoiceResponseJson(spawnPlayer, coordinates);
        sendInfo(EventType.RESPONSE_REQUEST_SPAWN, spawnChoiceResponseJson, getIdPlayersCreateList().addAllIdPlayers());


        //rise up the spawnPlayer
        spawnPlayer.risePlayerUp();

        //send to all the players the spawn player info
        String spawnPlayerLMJson = getCreateJson().createPlayerLMJson(spawnPlayer);
        sendInfo(EventType.UPDATE_PLAYER_INFO, spawnPlayerLMJson, getIdPlayersCreateList().addAllIdPlayers());

        //remove powerUp from powerUp list of spawnPlayer and put it in PowerUpDiscarded
        spawnPlayer.getPowerUps().remove(powerUpToDiscard);
        getTurnManager().getGameTable().getPowerUpDiscarded().add(powerUpToDiscard);


        //send my powerUpsLM to the player
        String myPowerUpsLMJson = getCreateJson().createMyPowerUpsListLMJson(spawnPlayer);
        sendInfo(EventType.UPDATE_MY_POWERUPS, myPowerUpsLMJson, getIdPlayersCreateList().addOneIdPlayers(spawnPlayer));

        //TODO: FORSE DEVO MANDARE MESS A TUTTI CHE IL SPAWN PLAYER HA UN POWERUP IN MENO
    }
}
