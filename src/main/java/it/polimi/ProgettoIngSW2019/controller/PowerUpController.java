package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.common.message.PowerUpMessage;
import it.polimi.ProgettoIngSW2019.utilities.Observer;

public class PowerUpController implements Observer<PowerUpMessage> {
    private PowerUp powerUpToUse;
    private Player userPlayer;
    private Player targetPlayer;
    private Square position;
    private GameTable gameTable;
    private TurnManager turnManager;


    public void update(PowerUpMessage powerUpMessage) {

    }



    public Player getUserPlayerById(int idUserPlayer){
        Player userPlayer;
        userPlayer = gameTable.getPlayers()[idUserPlayer];

        return userPlayer;
    }



    public PowerUp getPowerUpusedById(int idPowerUp){
        PowerUp powerUp;
        powerUp = userPlayer.getPowerUps().get(idPowerUp);

        return powerUp;
    }



    public Square getUserSquareToMoveByCoordinates(int[] coordintes){
        Square userSquareToMove = null;
        int row = coordintes[0];
        int column = coordintes[1];

        if(row != -1 && column != -1)
            userSquareToMove = gameTable.getMap()[row][column];

        return  userSquareToMove;
    }



    public void usePowerUp() {
        switch(powerUpToUse.getName()) {
            case "NEWTON":
                useNewton();
                break;

            case "TELEPORTER":
                useTeleporter();
                break;

            case "TAGBACK GRENADE":
                useTagbackGrenade();
                break;

            case "TARGETING SCOPE":
                useTargetingScope();
                break;

            default:
        }
    }



    public void useNewton() {
        //TODO: controllare meglio quando posso usare la carta
        if(userPlayer.getCharaName() == turnManager.getCurrentPlayer().getCharaName()) {
            powerUpToUse.usePowerUpEffect(null, targetPlayer, null, position);
        }
    }



    public void useTeleporter() {
        if(userPlayer.getCharaName() == turnManager.getCurrentPlayer().getCharaName()) {
            powerUpToUse.usePowerUpEffect(null, null, userPlayer, position);
        }
    }



    public void useTargetingScope() {
        if(userPlayer.getCharaName() == turnManager.getCurrentPlayer().getCharaName()) {
            //TODO: controllare che ha appena fatto lo sparo
                //TODO:SCEGLIERE TARGET
                    //TODO: attivare effetto
        }
    }



    public void useTagbackGrenade() {
        if(targetPlayer.getCharaName() == turnManager.getCurrentPlayer().getCharaName()) {
            //TODO: FARE CHECK SE PUò VEDERLO
                powerUpToUse.usePowerUpEffect(null, targetPlayer, userPlayer, null);
        }
    }
}
