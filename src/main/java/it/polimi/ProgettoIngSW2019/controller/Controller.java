package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.Observer;
import it.polimi.ProgettoIngSW2019.custom_exception.IllegalAttributeException;
import it.polimi.ProgettoIngSW2019.custom_exception.IllegalIdException;
import it.polimi.ProgettoIngSW2019.custom_exception.NotPartOfBoardException;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;
import jdk.nashorn.internal.runtime.regexp.joni.ApplyCaseFoldArg;

import java.util.List;

/**
 * Controller Class
 * it has the model classes needed to the all the Controllers
 * @author Priscilla Lo Bue
 */
public abstract class Controller implements Observer<Event> {
    private TurnManager turnManager;
    private VirtualView virtualView;
    private IdConverter idConverter;
    private CreateJson createJson;
    private IdPlayersCreateList idPlayersCreateList;



    /**
     * Constructor
     * @param turnManager   access to TurnManger model
     */
    public Controller(TurnManager turnManager, VirtualView virtualView, IdConverter idConverter, CreateJson createJson, IdPlayersCreateList idPlayersCreateList) {
        this.turnManager = turnManager;
        this.virtualView = virtualView;
        this.idConverter = idConverter;
        this.createJson = createJson;
        this.idPlayersCreateList = idPlayersCreateList;
    }


    /**
     * access to TurnManager
     * @return  turnManager
     */
    public TurnManager getTurnManager() {
        return turnManager;
    }


    /**
     * access to VirtualView
     * @return  virtualView
     */
    public VirtualView getVirtualView() {
        return virtualView;
    }


    /**
     * access to IdConverter
     * @return  IdConverter
     */
    public IdConverter getIdConverter() {
        return idConverter;
    }


    /**
     * access to CreateJson
     * @return  CreateJson
     */
    public CreateJson getCreateJson() {
        return createJson;
    }


    /**
     * access to IdPlayersCreateList
     * @return  IdPlayersCreateList
     */
    public IdPlayersCreateList getIdPlayersCreateList() {
        return idPlayersCreateList;
    }



    /**
     * send info/event by virtual view
     * @param eventType     type of the event
     * @param msgJson       message in json format
     * @param idPlayers     list of player who will receive the message
     */
    public void sendInfo(EventType eventType, String msgJson, List<Integer> idPlayers) {
        Event event = new Event(eventType, msgJson);
        virtualView.sendMessage(event, idPlayers);
    }


    /**
     * create the player if the id is correct
     * else return null and send a message error
     * @param idPlayer          id player
     * @return                  return the player id the id is correct, otherwise null
     */
    public Player convertPlayer(int idPlayer) {
        Player player;
        if(idPlayer < 0)
            throw new IllegalAttributeException("id player cannot be negative");

        try {
            player = idConverter.getPlayerById(idPlayer);
        } catch(IllegalIdException e) {
            String messageError = "Ops, qualcosa è andato storto!";
            //todo: mandare al client l'errore
            //sendInfo(EventType.ERROR, messageError, idPlayersCreateList.addOneIdPlayers(player));
            return null;
        }
        return player;
    }


//CONVERT
    /**
     * if the id is correct return the powerUp
     * otherwise null
     * @param player           player
     * @param idPowerUp        idPowerUp
     * @return                 powerUp or null
     */
    public PowerUp convertPowerUp(Player player, int idPowerUp) {
        if(player == null)
            throw new NullPointerException("player cannot be null");

        if(idPowerUp < 0)
            throw new IllegalAttributeException("id powerUp cannot be negative");

        PowerUp powerUp;

        try {
            powerUp = idConverter.getPowerUpCardById(player.getIdPlayer(), idPowerUp);
        } catch (IllegalIdException e) {
            String messageError = "Ops, qualcosa è andato storto!";
            sendInfo(EventType.ERROR, messageError, idPlayersCreateList.addOneIdPlayers(player));
            return null;
        }
        return powerUp;
    }


    /**
     * if the id is correct return the weapon
     * otherwise null
     * @param player            player
     * @param idWeapon          id weapon
     * @param unloadedWeapon    boolean to define if is an unloadedWeapon or not
     * @return                  weapon or null
     */
    public WeaponCard convertWeapon(Player player, int idWeapon, boolean unloadedWeapon) {
        if(player == null)
            throw new NullPointerException("player cannot be null");

        if(idWeapon < 0)
            throw new IllegalAttributeException("id powerUp cannot be negative");

        WeaponCard weaponCard;

        try {
            if(unloadedWeapon)
                weaponCard = idConverter.getUnloadedWeaponById(player.getIdPlayer(), idWeapon);
            else
                weaponCard = idConverter.getLoadedWeaponById(player.getIdPlayer(), idWeapon);
        } catch (IllegalIdException e) {
            String messageError = "Ops, qualcosa è andato storto!";
            sendInfo(EventType.ERROR, messageError, idPlayersCreateList.addOneIdPlayers(player));
            return null;
        }
        return weaponCard;
    }



    public Square convertSquare(Player player, int[] coordinates) {
        Square square;
        try {
            square = idConverter.getSquareByCoordinates(coordinates);
        } catch(NotPartOfBoardException e) {
            String messageError = "Ops, qualcosa è andato storto!";
            sendInfo(EventType.ERROR, messageError, idPlayersCreateList.addOneIdPlayers(player));
            return null;
        }
        return square;
    }



//CHECK TURN
    /**
     * check if is the turn of the player
     * if not send a message error
     * @param player    player
     * @return          true if is turn player, false if not
     */
    public boolean checkCurrentPlayer(Player player) {
        if(player == null)
            throw new NullPointerException("player cannot be null");

        if(player.getIdPlayer() != getTurnManager().getCurrentPlayer().getIdPlayer()) {
            String messageError = "ERROR: Non è il tuo turno";
            sendInfo(EventType.ERROR, messageError, idPlayersCreateList.addOneIdPlayers(player));
            return false;
        }
        return true;
    }


    /**
     * @param player    player
     * @return          true if there are no action left
     */
    public boolean checkNoActionLeft(Player player) {
        if(player == null)
            throw new NullPointerException("player cannot be null");

        if (getTurnManager().getActionsLeft() != 0) {
            String messageError = "ERROR: Non puoi finire il turno in questo momento.";
            sendInfo(EventType.ERROR, messageError, idPlayersCreateList.addOneIdPlayers(player));
            return false;
        }
        return true;
    }


    public boolean checkHasActionLeft(Player player) {
        if(player == null)
            throw new NullPointerException("player cannot be null");

        if (getTurnManager().getActionsLeft() == 0) {
            String messageError = "ERROR: Non hai più azioni a disposizione.";
            sendInfo(EventType.ERROR, messageError, idPlayersCreateList.addOneIdPlayers(player));
            return false;
        }
        return true;
    }



//CHECK CONTAINS
    /**
     *
     * @param player        player
     * @param powerUp       powerUp of the player
     * @return              boolean if the player has the powerUp
     */
    public boolean checkContainsPowerUp(Player player, PowerUp powerUp) {
        if(player == null)
            throw new NullPointerException("player cannot be null");

        if(powerUp == null)
            throw new NullPointerException("powerUp cannot be null");

        if (!player.getPowerUps().contains(powerUp)) {
            String messageError = "ERROR: Non hai questo PowerUp: " + powerUp.getName();
            sendInfo(EventType.ERROR, messageError, idPlayersCreateList.addOneIdPlayers(player));
            return false;
        }
        return true;
    }


    /**
     *
     * @param player            player
     * @param weaponCard        weapon card
     * @param unloadedWeapon    boolean if is an unloaded Weapon
     * @return                  true if the player has the weapon
     */
    public boolean checkContainsWeapon(Player player, WeaponCard weaponCard, boolean unloadedWeapon) {
        if(player == null)
            throw new NullPointerException("player cannot be null");

        if(weaponCard == null)
            throw new NullPointerException("player cannot be null");

        if (unloadedWeapon) {
            if (player.getUnloadedWeapons().contains(weaponCard))
                return true;
            else {
                String messageError = "ERROR: Non hai questa arma: " + weaponCard.getIdCard() + ", scegli un'altra";
                sendInfo(EventType.ERROR, messageError, idPlayersCreateList.addOneIdPlayers(player));
                return false;
            }
        } else {
            if (player.getLoadedWeapons().contains(weaponCard))
                return true;
            else {
                String messageError = "ERROR: Non hai questa arma: " + weaponCard.getIdCard() + ", scegli un'altra";
                sendInfo(EventType.ERROR, messageError, idPlayersCreateList.addOneIdPlayers(player));
                return false;
            }
        }
    }



//OTHER
    /**
     * check if the player has enough ammo to pay
     * @param player        player
     * @param ammoToPay     ammo cost
     * @return              true if the player can pay
     */
    public boolean checkHasEnoughAmmo(Player player, List<AmmoType> ammoToPay) {
        if(player == null)
            throw new NullPointerException("player cannot be null");

        if(ammoToPay == null)
            throw new NullPointerException("ammoToPay cannot be null");

        if(!player.hasEnoughAmmo(ammoToPay)) {
            String messageError = "ERROR: Non puoi pagare il costo";
            sendInfo(EventType.ERROR, messageError, getIdPlayersCreateList().addOneIdPlayers(player));
            return false;
        }
        else
            return true;
    }
}
