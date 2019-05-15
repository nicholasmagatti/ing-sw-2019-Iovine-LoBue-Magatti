package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.utilities.*;
import it.polimi.ProgettoIngSW2019.model.GameTable;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.AmmoPoint;
import it.polimi.ProgettoIngSW2019.model.SpawningPoint;
import it.polimi.ProgettoIngSW2019.model.WeaponCard;
import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.model.TurnManager;

import java.util.List;

/**
 * @author Nicholas Magatti
 */
public class GrabController implements Observer<Event> {
    private Player playerWhoGrab;
    private Square squareToGrabFrom;
    //TODO: keep or delete?
    private GameTable gameTable;
    //TODO: keep or delete?
    private TurnManager turnManager;

    //TODO: keep or delete?
    public GrabController(GameTable gameTable, TurnManager turnManager){
        this.gameTable = gameTable;
        this.turnManager = turnManager;
    }



    public void update(Event grabMessage) {

    }

    public void grab() {
        if (squareToGrabFrom instanceof AmmoPoint) {
            playerWhoGrab.moveTo(squareToGrabFrom);
            playerWhoGrab.grabAmmoCardFromThisSquare();
        } else {
            if (squareToGrabFrom instanceof SpawningPoint) {
                //TODO: should this be split in two methods?
            } else {
                throw new RuntimeException("Something went wrong in grab in the controller");
            }
        }
    }

    public List<WeaponCard> availableWeaponsFromSquare(){
        if(squareToGrabFrom instanceof AmmoPoint){
            throw new RuntimeException("Something went wrong in the GrabController.");
        }
        return ((SpawningPoint)squareToGrabFrom).getWeaponCards();
    }

    public boolean isPlayerAlreadyOnThatSquare(){
        return (playerWhoGrab.getPosition() == squareToGrabFrom);
    }

    public void grabWeapon(WeaponCard weaponToGet, WeaponCard weaponToLeave){
        playerWhoGrab.grabWeapon(weaponToGet, weaponToLeave);
    }
}
