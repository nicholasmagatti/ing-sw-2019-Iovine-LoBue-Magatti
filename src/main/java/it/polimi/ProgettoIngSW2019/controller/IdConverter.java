package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.custom_exception.IllegalIdException;
import it.polimi.ProgettoIngSW2019.custom_exception.NotPartOfBoardException;
import it.polimi.ProgettoIngSW2019.model.*;

import java.util.List;

/**
 * Class IdConverter
 *
 * @author: Luca Iovine
 */
public class IdConverter {
    private GameTable gameTable;
    private final int NR_OF_POWERUP = 24;
    private final int NR_OF_WEAPON = 21;

    public IdConverter (GameTable gameTable){
        this.gameTable = gameTable;
    }

    /**
     * @param idPlayer id of the player
     * @return instnace of player associated to the id
     * @author: Luca Iovine
     */
    public Player getPlayerById(int idPlayer) throws IllegalIdException {
        Player[] playerInGame = gameTable.getPlayers();
        Player player;

        if(idPlayer >= 0 && idPlayer <= playerInGame.length - 1)
            player = playerInGame[idPlayer];
        else
            throw new IllegalIdException();

        return player;
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

        if((row >= 0 && row <= GeneralInfo.ROWS_MAP) && (column >= 0 && column <= GeneralInfo.COLUMNS_MAP))
            square = gameTable.getMap()[row][column];
        else
            throw new NotPartOfBoardException("");

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
    public WeaponCard getLoadedWeaponById(int idPlayer, int idWeapon) throws IllegalIdException{
        WeaponCard weaponCardLoaded = null;

        if(idWeapon >= DeckFactory.getFirstIdWeapon() && idWeapon <= DeckFactory.getFirstIdWeapon() + NR_OF_WEAPON - 1) {
            List<WeaponCard> weaponCardLoadedList = gameTable.getPlayers()[idPlayer].getUnloadedWeapons();

            for (WeaponCard wl : weaponCardLoadedList) {
                if (wl.getIdCard() == idWeapon) {
                    weaponCardLoaded = wl;
                }
            }
        }else
            throw new IllegalIdException();

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
    public WeaponCard getUnloadedWeaponById(int idPlayer, int idWeapon) throws IllegalIdException{
        WeaponCard weaponCardUnloaded = null;

        if(idWeapon >= DeckFactory.getFirstIdWeapon() && idWeapon <= DeckFactory.getFirstIdWeapon() + NR_OF_WEAPON - 1) {
            List<WeaponCard> weaponUnloadedList = gameTable.getPlayers()[idPlayer].getUnloadedWeapons();

            for (WeaponCard wu : weaponUnloadedList) {
                if (wu.getIdCard() == idWeapon) {
                    weaponCardUnloaded = wu;
                }
            }
        }else
            throw new IllegalIdException();

        return weaponCardUnloaded;
    }

    /**
     * @param idPlayer id of the player
     * @param idPowerUp id of the weapon card
     * @return instance of the PowerUpCard based on its id and the player owner id
     * @author: Luca Iovine
     */
    public PowerUp getPowerUpCardById(int idPlayer, int idPowerUp) throws IllegalIdException{
        PowerUp powerUp = null;

        if(idPowerUp >= DeckFactory.getFirstIdPowerUp() && idPowerUp <= DeckFactory.getFirstIdPowerUp() + NR_OF_POWERUP - 1) {
            List<PowerUp> powerUpInHand = gameTable.getPlayers()[idPlayer].getPowerUps();


            for (PowerUp pup : powerUpInHand) {
                if (pup.getIdCard() == idPowerUp) {
                    powerUp = pup;
                }
            }
        }else
            throw new IllegalIdException();

        return powerUp;
    }
}
