package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.model.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.utilities.Observer;
import it.polimi.ProgettoIngSW2019.common.message.SpawnMessage;

public class SpawnController implements Observer<SpawnMessage> {
    private Square spawnPos;
    private Player spawnPlayer;
    TurnManager turnManager;
    GameTable gameTable;
    private PowerUp powerUpToDiscard;


    public void update(SpawnMessage spawnMessage) {

    }


    public void spawnDrawCard() {
        //TODO: controllo se è current player
        if(spawnPlayer.isPlayerDown()) {
            Card powerUp = gameTable.getPowerUpDeck().drawCard();
            spawnPlayer.getPowerUps().add((PowerUp) powerUp);
        }
    }


    public void spawnIntoSquare() {
        spawnPlayer.getPowerUps().remove(powerUpToDiscard);
        AmmoType colorToSpawn = powerUpToDiscard.getGainAmmoColor();
        int idRoom = AmmoType.intFromAmmoType(colorToSpawn);

        outLoops:
        {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 3; j++) {
                    spawnPos = gameTable.getMap()[i][j];
                    if ((spawnPos.getIdRoom() == idRoom) && (spawnPos instanceof SpawningPoint))
                        break outLoops;

                }
            }
        }

        spawnPlayer.moveTo(spawnPos);
        spawnPlayer.risePlayerUp();

        gameTable.getPowerUpDiscarded().add(powerUpToDiscard);
    }
}
