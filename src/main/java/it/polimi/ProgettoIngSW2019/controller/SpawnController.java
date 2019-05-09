package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.model.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.utilities.Observer;
import it.polimi.ProgettoIngSW2019.common.message.SpawnMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Class SpawnController
 * @author Priscilla Lo Bue
 */
public class SpawnController implements Observer<SpawnMessage> {
    private Square spawnPos;
    private Player spawnPlayer;
    private TurnManager turnManager;
    private GameTable gameTable;
    private PowerUp powerUpToDiscard;


    public void update(SpawnMessage spawnMessage) {

    }



    public Player getUserPlayerById(int idUserPlayer){
        Player userPlayer;
        userPlayer = gameTable.getPlayers()[idUserPlayer];

        return userPlayer;
    }



    public PowerUp getPowerUpusedById(int idPowerUp){
        PowerUp powerUp;
        powerUp = spawnPlayer.getPowerUps().get(idPowerUp);

        return powerUp;
    }



    /**
     * Check if it's the spawnPlayer turn
     * if not -> exception
     */
    public void checkTurnPlayer() {
        //TODO: DA SISTEMARE
        /*
        1- potrebbe non esser più necessiario il controllo sulle azioni
        2- in base al messaggio faremo controllo di quale tipo di spawn fare
         */
        int nActionsLeft = turnManager.getActionsLeft();

        if(nActionsLeft == 0) {
            if(spawnPlayer.isPlayerDown()) {
                spawnDrawCard();
            }
            else
                //throw new IllegalArgumentException("The player: " + spawnPlayer.getCharaName() + "with id: " + spawnPlayer.getIdPlayer() + "is not dead.");
                spawnDrawTwoCards();
        }
    }



    public void spawnDrawTwoCards() {
        //spawnPlayer pesca due powerUp all'inizio del gioco per lo spawn
        PowerUp powerUp = (PowerUp) gameTable.getPowerUpDeck().drawCard();
        spawnPlayer.getPowerUps().add(powerUp);
        powerUp = (PowerUp) gameTable.getPowerUpDeck().drawCard();
        spawnPlayer.getPowerUps().add(powerUp);
    }



    public void spawnDrawCard() {
        //azzero i danni subiti dallo spawnPlayer
        spawnPlayer.emptyDamageLine();

        //pesco 1 powerUp e lo aggiungo alla lista dello spawnPlayer
        PowerUp powerUp = (PowerUp) gameTable.getPowerUpDeck().drawCard();
        spawnPlayer.getPowerUps().add(powerUp);
        //il player deve scegliere la carta da scartare
    }



    public void spawnIntoSquare() {
        //TODO: controllare che è un powerUp?

        //rimuovo powerUp da lista powerUp di spawnPlayer
        spawnPlayer.getPowerUps().remove(powerUpToDiscard);

        //salvo colore ammo
        AmmoType colorToSpawn = powerUpToDiscard.getGainAmmoColor();
        //in base al colore trovo la stanza e mi salvo l'idRoom
        int idRoom = AmmoType.intFromAmmoType(colorToSpawn);

        //scarto la carta
        gameTable.getPowerUpDiscarded().add(powerUpToDiscard);

        //cerco lo spawn di quel colore e con idRoom
        //appena lo trovo esco dai cicli
        outLoops:
        {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 4; j++) {
                    spawnPos = gameTable.getMap()[i][j];
                    if ((spawnPos.getIdRoom() == idRoom) && (spawnPos instanceof SpawningPoint))
                        break outLoops;

                }
            }
        }

        //sposto il spawnPlayer nello spawn scelto
        spawnPlayer.moveTo(spawnPos);

        //risveglio spawnPlayer
        spawnPlayer.risePlayerUp();
    }
}
