package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.common.utilities.Observer;
import it.polimi.ProgettoIngSW2019.custom_exception.IllegalAttributeException;
import it.polimi.ProgettoIngSW2019.custom_exception.IllegalIdException;
import it.polimi.ProgettoIngSW2019.custom_exception.NotPartOfBoardException;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Controller Class
 * it has the model classes needed to the all the Controllers
 * @author Priscilla Lo Bue
 */
abstract class Controller implements Observer<Event> {
    private TurnManager turnManager;
    private VirtualView virtualView;
    private IdConverter idConverter;
    private CreateJson createJson;
    private HostNameCreateList hostNameCreateList;



    /**
     * Constructor
     * @param turnManager           TurnManager
     * @param virtualView           VirtualView
     * @param idConverter           IdConverter
     * @param createJson            CreateJson
     * @param hostNameCreateList    hostNameCreateList
     */
    Controller(TurnManager turnManager, VirtualView virtualView, IdConverter idConverter, CreateJson createJson, HostNameCreateList hostNameCreateList) {
        this.turnManager = turnManager;
        this.virtualView = virtualView;
        this.idConverter = idConverter;
        this.createJson = createJson;
        this.hostNameCreateList = hostNameCreateList;
    }


    /**
     * access to TurnManager
     * @return  turnManager
     */
    TurnManager getTurnManager() {
        return turnManager;
    }


    /**
     * access to VirtualView
     * @return  virtualView
     */
    VirtualView getVirtualView() {
        return virtualView;
    }


    /**
     * access to IdConverter
     * @return  IdConverter
     */
    IdConverter getIdConverter() {
        return idConverter;
    }


    /**
     * access to CreateJson
     * @return  CreateJson
     */
    CreateJson getCreateJson() {
        return createJson;
    }


    /**
     * access to HostNameCreateList
     * @return  HostPlayerCreateList
     */
    HostNameCreateList getHostNameCreateList() {
        return hostNameCreateList;
    }


    /**
     * send info/event by virtual view
     * @param eventType         type of the event
     * @param msgJson           message in json format
     * @param hostNameList      list of host name player who will receive the message
     */
    void sendInfo(EventType eventType, String msgJson, List<String> hostNameList) {
        Event event = new Event(eventType, msgJson);
        virtualView.sendMessage(event, hostNameList);
    }


    /**
     * create the player if the id is correct
     * else return null and send a message error
     * @param idPlayer          id player
     * @return                  return the player id the id is correct, otherwise null
     */
    Player convertPlayer(int idPlayer, String hostName) {
        Player player;
        if(idPlayer < 0)
            throw new IllegalAttributeException("id player cannot be negative");

        try {
            player = idConverter.getPlayerById(idPlayer);
        } catch(IllegalIdException e) {
            String messageError = GeneralInfo.MSG_ERROR;
            sendInfo(EventType.ERROR, messageError, Arrays.asList(hostName));
            return null;
        }
        return player;
    }


    /**
     * create the player if the id is correct
     * else return null and send a message error
     * @param idPlayer          id player
     * @return                  return the player id the id is correct, otherwise null
     */
    Player convertPlayerWithId(int idPlayer) {
        Player player;
        if(idPlayer < 0)
            throw new IllegalAttributeException("id player cannot be negative");

        try {
            player = idConverter.getPlayerById(idPlayer);
        } catch(IllegalIdException e) {
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
    PowerUp convertPowerUp(Player player, int idPowerUp) {
        if(player == null)
            throw new NullPointerException("player cannot be null");

        if(idPowerUp < 0)
            throw new IllegalAttributeException("id powerUp cannot be negative");

        PowerUp powerUp;

        try {
            powerUp = idConverter.getPowerUpCardById(player.getIdPlayer(), idPowerUp);
        } catch (IllegalIdException e) {
            String messageError = GeneralInfo.MSG_ERROR;
            sendInfo(EventType.ERROR, messageError, hostNameCreateList.addOneHostName(player));
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
    WeaponCard convertWeapon(Player player, int idWeapon, boolean unloadedWeapon) {
        if(player == null)
            throw new NullPointerException("player cannot be null");

        if(idWeapon < 0)
            throw new IllegalAttributeException("id powerUp cannot be negative");

        WeaponCard weaponCard;

        if(unloadedWeapon) {
            try {
                weaponCard = idConverter.getUnloadedWeaponById(player.getIdPlayer(), idWeapon);
            }
            catch (IllegalIdException e) {
                String messageError = GeneralInfo.MSG_ERROR;
                sendInfo(EventType.ERROR, messageError, hostNameCreateList.addOneHostName(player));
                return null;
            }
        }
        else {
            try {
                weaponCard = idConverter.getLoadedWeaponById(player.getIdPlayer(), idWeapon);
            }
            catch (IllegalIdException e) {
                String messageError = GeneralInfo.MSG_ERROR;
                sendInfo(EventType.ERROR, messageError, hostNameCreateList.addOneHostName(player));
                return null;
            }
        }

        return weaponCard;
    }


    /**
     * convert a square from his coordinates
     * @param player            player who request
     * @param coordinates       coordinates of the square
     * @return                  the square converted by the coordinates
     */
    Square convertSquare(Player player, int[] coordinates) {
        if(player == null)
            throw new NullPointerException("player cannot be null");

        Square square;
        try {
            square = idConverter.getSquareByCoordinates(coordinates);
        } catch(NotPartOfBoardException e) {
            String messageError = GeneralInfo.MSG_ERROR;
            sendInfo(EventType.ERROR, messageError, hostNameCreateList.addOneHostName(player));
            return null;
        }
        return square;
    }



//CHECK TURN

    /**
     * Check if the hostName is equal to the hostName of the player
     * @param player        player to check
     * @param hostName      hostName from view
     * @return              true if is correct, false otherwise
     */
    boolean checkHostNameCorrect(Player player, String hostName) {
        if(player.getHostname().equals(hostName))
            return true;
        else {
            String messageError = GeneralInfo.MSG_ERROR;
            sendInfo(EventType.ERROR, messageError, Arrays.asList(hostName));
            return false;
        }
    }


    /**
     * check if the id is equal to the id of the player
     * @param player            player to check
     * @param idPlayer          id from view
     * @param hostName          hostName from view
     * @return                  true if is correct, false otherwise
     */
    boolean checkIdCorrect(Player player, int idPlayer, String hostName) {
        if(player.getIdPlayer() == idPlayer)
            return true;
        else {
            String messageError = GeneralInfo.MSG_ERROR;
            sendInfo(EventType.ERROR, messageError, Arrays.asList(hostName));
            return false;
        }
    }



    /**
     * check if is the turn of the player
     * if not send a message error
     * @param player    player
     * @return          true if is turn player, false if not
     */
    boolean checkCurrentPlayer(Player player) {
        if(player == null)
            throw new NullPointerException("player cannot be null");

        if(player.getIdPlayer() != getTurnManager().getCurrentPlayer().getIdPlayer()) {
            String messageError = GeneralInfo.MSG_ERROR;
            sendInfo(EventType.ERROR, messageError, hostNameCreateList.addOneHostName(player));
            return false;
        }
        return true;
    }


    /**
     * @param player    player
     * @return          true if there are no action left
     */
    boolean checkNoActionLeft(Player player) {
        if(player == null)
            throw new NullPointerException("player cannot be null");

        if (getTurnManager().getActionsLeft() != 0) {
            String messageError = GeneralInfo.MSG_ERROR;
            sendInfo(EventType.ERROR, messageError, hostNameCreateList.addOneHostName(player));
            return false;
        }
        return true;
    }


    /**
     * check if the player has more then 0 action left
     * @param player        player to check
     * @return              true if is correct, false otherwise
     */
    boolean checkHasActionLeft(Player player) {
        if(player == null)
            throw new NullPointerException("player cannot be null");

        if (getTurnManager().getActionsLeft() == 0) {
            String messageError = GeneralInfo.MSG_ERROR;
            sendInfo(EventType.ERROR, messageError, hostNameCreateList.addOneHostName(player));
            return false;
        }
        return true;
    }



//CHECK CONTAINS
    /**
     *
     * @param player            player
     * @param weaponCard        weapon card
     * @param unloadedWeapon    boolean if is an unloaded Weapon
     * @return                  true if the player has the weapon
     */
    boolean checkContainsWeapon(Player player, WeaponCard weaponCard, boolean unloadedWeapon) {
        if(player == null)
            throw new NullPointerException("player cannot be null");

        if(weaponCard == null)
            throw new NullPointerException("WeaponCard cannot be null");

        if (unloadedWeapon) {
            if (player.getUnloadedWeapons().contains(weaponCard))
                return true;
            else {
                String messageError = GeneralInfo.MSG_ERROR;
                sendInfo(EventType.ERROR, messageError, hostNameCreateList.addOneHostName(player));
                return false;
            }
        } else {
            if (player.getLoadedWeapons().contains(weaponCard))
                return true;
            else {
                String messageError = GeneralInfo.MSG_ERROR;
                sendInfo(EventType.ERROR, messageError, hostNameCreateList.addOneHostName(player));
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
    boolean checkHasEnoughAmmo(Player player, List<AmmoType> ammoToPay) {
        if(player == null)
            throw new NullPointerException("player cannot be null");

        if(!player.hasEnoughAmmo(ammoToPay)) {
            String messageError = GeneralInfo.MSG_ERROR;
            sendInfo(EventType.ERROR, messageError, hostNameCreateList.addOneHostName(player));
            return false;
        }
        else
            return true;
    }


    /**
     * message to view with the actionLeft of the player and his powerUps he can use
     * @param ownerPlayer       player to send the message
     */
    void msgActionLeft(Player ownerPlayer) {
        List<PowerUp> powerUpsCanUse = new ArrayList<>();

        if(!ownerPlayer.getPowerUps().isEmpty()) {
            for(PowerUp p:ownerPlayer.getPowerUps()) {
                if(!p.getName().equals(GeneralInfo.TAGBACK_GRENADE) && !p.getName().equals(GeneralInfo.TARGETING_SCOPE))
                    powerUpsCanUse.add(p);
            }
        }

        String messageActionLeftJson = getCreateJson().createMessageActionsLeftJson(ownerPlayer, powerUpsCanUse);
        sendInfo(EventType.MSG_MY_N_ACTION_LEFT, messageActionLeftJson, getHostNameCreateList().addOneHostName(ownerPlayer));
    }
}
