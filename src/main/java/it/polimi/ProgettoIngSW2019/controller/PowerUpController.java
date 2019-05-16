package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.model.powerup_effects.TagbackGrenadeEff;
import it.polimi.ProgettoIngSW2019.common.utilities.Observer;

import java.util.List;

public class PowerUpController implements Observer<Event> {
    private PowerUp powerUpToUse;
    private Player userPlayer;
    private Player targetPlayer;
    private Square position;
    private TurnManager turnManager;
    private List<Player> targetList;


    public PowerUpController(TurnManager turnManager) {
        this.turnManager = turnManager;
    }



    public void update(Event event) {

    }



    public Player getUserPlayerById(int idUserPlayer){
        Player userPlayer;
        userPlayer = turnManager.getGameTable().getPlayers()[idUserPlayer];

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
            userSquareToMove = turnManager.getGameTable().getMap()[row][column];

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



    /*DONE*/
    public void useNewton() {
        if(userPlayer.getCharaName().equals(turnManager.getCurrentPlayer().getCharaName())) {
            powerUpToUse.usePowerUpEffect(null, targetPlayer, null, position);
        }
        else
            throw new IllegalArgumentException("Player: " + userPlayer.getCharaName() + "\nId: " + userPlayer.getIdPlayer() + "\nit is not your turn");
    }



    /*DONE*/
    public void useTeleporter() {
        if(userPlayer.getCharaName().equals(turnManager.getCurrentPlayer().getCharaName())) {
            powerUpToUse.usePowerUpEffect(null, null, userPlayer, position);
        }
        else
            throw new IllegalArgumentException("Player: " + userPlayer.getCharaName() + "\nId: " + userPlayer.getIdPlayer() + "\nit is not your turn");
    }



    public void useTargetingScope() {
        if(userPlayer.getCharaName().equals(turnManager.getCurrentPlayer().getCharaName())) {
            //TODO: controllare che ha appena fatto lo sparo
                //TODO:SCEGLIERE TARGET
                    //TODO: attivare effetto
        }
        else
            throw new IllegalArgumentException("Player: " + userPlayer.getCharaName() + "\nId: " + userPlayer.getIdPlayer() + "\nit is not your turn");
    }



    public void useTagbackGrenade() {
        if(targetPlayer.getCharaName().equals(turnManager.getCurrentPlayer().getCharaName())) {
            if(((TagbackGrenadeEff)powerUpToUse.getPowerUpEffect()).canSeeTarget(userPlayer, targetPlayer))
                powerUpToUse.usePowerUpEffect(null, targetPlayer, userPlayer, null);
            else {
                //TODO: mandare msg errore, perchè non può vedere il terget a cui fare il marchio
            }
        }
        else
            throw new IllegalArgumentException("Player: " + targetPlayer.getCharaName() + "\nId: " + targetPlayer.getIdPlayer() + "\ndoesn't deal you damage");
    }
}
