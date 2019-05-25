package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.model.*;

import java.util.List;

/**
 * Class IdConverter
 *
 * @author: Luca Iovine
 */
public class IdConverter {
    private GameTable gameTable;

    public IdConverter (GameTable gameTable){
        this.gameTable = gameTable;
    }

    /**
     * @param idPlayer id of the player
     * @return instnace of player associated to the id
     * @author: Luca Iovine
     */
    public Player getPlayerById(int idPlayer){
        Player userPlayer;
        userPlayer = gameTable.getPlayers()[idPlayer];

        return userPlayer;
    }

    /**
     * @param coordintes contains row value at position 0 and column value at position 1
     * @return the square based on the row x column coordinates
     * @author: Luca Iovine
     */
    public Square getSquareByCoordinates(int[] coordintes){
        Square square = null;
        int row = coordintes[0];
        int column = coordintes[1];

        if(row != -1 && column != -1)
            square = gameTable.getMap()[row][column];

        return  square;
    }

    /**
     * Retrive the weapon by its id if it is loaded
     *
     * @param idPlayer id of the player
     * @param idWeapon id of the weapon card
     * @return instance of the WeaponCard based on its id and the player owner id
     * @author: Luca Iovine
     */
    public WeaponCard getLoadedWeaponById(int idPlayer, int idWeapon){
        WeaponCard weaponCardLoaded = null;
        List<WeaponCard> weaponCardLoadedList = gameTable.getPlayers()[idPlayer].getUnloadedWeapons();

        for(WeaponCard wl: weaponCardLoadedList){
            if(wl.getIdCard() == idWeapon){
                weaponCardLoaded = wl;
            }
        }

        return weaponCardLoaded;
    }

    /**
     * Retrive the weapon by its id if it is unloaded
     *
     * @param idPlayer id of the player
     * @param idWeapon id of the weapon card
     * @return instance of the WeaponCard based on its id and the player owner id
     * @author: Luca Iovine
     */
    public WeaponCard getUnloadedWeaponById(int idPlayer, int idWeapon){
        WeaponCard weaponCardUnloaded = null;
        List<WeaponCard> weaponUnloadedList = gameTable.getPlayers()[idPlayer].getUnloadedWeapons();

        for(WeaponCard wu: weaponUnloadedList){
            if(wu.getIdCard() == idWeapon){
                weaponCardUnloaded = wu;
            }
        }

        return weaponCardUnloaded;
    }

    /**
     * @param idPlayer id of the player
     * @param idPowerUp id of the weapon card
     * @return instance of the PowerUpCard based on its id and the player owner id
     * @author: Luca Iovine
     */
    public PowerUp getPowerUpCardById(int idPlayer, int idPowerUp){
        List<PowerUp> powerUpInHand = gameTable.getPlayers()[idPlayer].getPowerUps();
        PowerUp powerUp = null;

        for(PowerUp pup: powerUpInHand){
            if(pup.getIdCard() == idPowerUp){
                powerUp = pup;
            }
        }

        return powerUp;
    }
}
