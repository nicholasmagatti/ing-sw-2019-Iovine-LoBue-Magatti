package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.LightModel.MyPowerUpLM;
import it.polimi.ProgettoIngSW2019.common.LightModel.PowerUpLM;
import it.polimi.ProgettoIngSW2019.common.Message.Info;
import it.polimi.ProgettoIngSW2019.common.Message.SpawnChoice;
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


    public SpawnController(TurnManager turnManager, IdConverter idConverter, VirtualView virtualView, CreateJson createJson) {
        super(turnManager, idConverter, virtualView, createJson);
    }



    public void update(Event event) {
        if(event.getCommand().equals(EventType.REQUEST_SPAWN_INFO)) {
            spawnDrawCard(event.getMessageInJsonFormat());
        }

        if(event.getCommand().equals(EventType.REQUEST_INITIAL_SPAWN_INFO)) {
            spawnDrawTwoCards(event.getMessageInJsonFormat());
        }

        if(event.getCommand().equals(EventType.REQUEST_SPAWN)) {
            respawn(event.getMessageInJsonFormat());
        }
    }



    public void spawnDrawTwoCards(String messageJson) {
        Info info = new Gson().fromJson(messageJson, Info.class);
        spawnPlayer = getIdConverter().getPlayerById(info.getIdPlayer());

        //TODO: CHECK FIRST TIME IN GAME

        //spawnPlayer pesca due powerUp all'inizio del gioco per lo spawn
        PowerUp powerUp = (PowerUp) getTurnManager().getGameTable().getPowerUpDeck().drawCard();
        spawnPlayer.getPowerUps().add(powerUp);
        powerUp = (PowerUp) getTurnManager().getGameTable().getPowerUpDeck().drawCard();
        spawnPlayer.getPowerUps().add(powerUp);

        String myPowerUpsJson = getCreateJson().createPowerUpsListLMJson(spawnPlayer.getPowerUps());
        sendInfo(EventType.RESPONSE_REQUEST_INITIAL_SPAWN_INFO, myPowerUpsJson);
    }



    public void spawnDrawCard(String messageJson) {
        Info info = new Gson().fromJson(messageJson, Info.class);
        spawnPlayer = getIdConverter().getPlayerById(info.getIdPlayer());

        if(!spawnPlayer.isPlayerDown())
            throw new IllegalAttributeException("The Player: " + spawnPlayer.getIdPlayer() + "is not dead, cannot spawn");

        //azzero i danni subiti dallo spawnPlayer
        spawnPlayer.emptyDamageLine();

        //pesco 1 powerUp e lo aggiungo alla lista dello spawnPlayer
        PowerUp powerUp = (PowerUp) getTurnManager().getGameTable().getPowerUpDeck().drawCard();
        spawnPlayer.getPowerUps().add(powerUp);

        String myPowerUpsJson = getCreateJson().createPowerUpsListLMJson(spawnPlayer.getPowerUps());
        sendInfo(EventType.RESPONSE_REQUEST_SPAWN_INFO, myPowerUpsJson);
    }



    public void respawn(String messageJson) {
        SpawnChoice spawnChoice = new Gson().fromJson(messageJson, SpawnChoice.class);
        spawnPlayer = getIdConverter().getPlayerById(spawnChoice.getIdPlayer());
        powerUpToDiscard = getIdConverter().getPowerUpbyId(spawnChoice.getIdPowerUpToDiscard());

        if(!spawnPlayer.isPlayerDown())
            throw new IllegalAttributeException("The Player: " + spawnPlayer.getIdPlayer() + "is not dead, cannot spawn");


        String powerUpLMJason = getCreateJson().createPowerUpLMJson(powerUpToDiscard);
        Event event = new Event(EventType.UPDATE_POWERUPS, powerUpLMJason);
        //TODO: INVIO INFO POWERUP da scartare A TUTTI GLI ALTRI GIOCATORI

        //rimuovo powerUp da lista powerUp di spawnPlayer e la metto nella pila degli scarti
        spawnPlayer.getPowerUps().remove(powerUpToDiscard);
        getTurnManager().getGameTable().getPowerUpDiscarded().add(powerUpToDiscard);

        //ritorno le powerUps al player
        String myPowerUpsLMJson = getCreateJson().createMyPowerUpsListLMJson(spawnPlayer);
        sendInfo(EventType.UPDATE_MY_POWERUPS, myPowerUpsLMJson);


        //salvo colore ammo
        AmmoType colorToSpawn = powerUpToDiscard.getGainAmmoColor();

        //in base al colore trovo la stanza e mi salvo l'idRoom
        int idRoom = AmmoType.intFromAmmoType(colorToSpawn);

        //cerco lo spawn di quel colore e con idRoom
        //appena lo trovo esco dai cicli
        outLoops: {
            for (Square[] squareLine : getTurnManager().getGameTable().getMap()) {
                for (Square spawnSquare : squareLine) {
                    if ((spawnSquare.getSquareType() == SquareType.SPAWNING_POINT) && (spawnSquare.getIdRoom() == idRoom)) {
                        spawnPos = spawnSquare;
                        break outLoops;
                    }
                }
            }
        }

        //sposto il spawnPlayer nello spawn scelto
        spawnPlayer.moveTo(spawnPos);

        //risveglio spawnPlayer
        spawnPlayer.risePlayerUp();

        //TODO:CREARE MESSAGGIO DI RITORNO CON NUOVE COORDINATE
    }
}
