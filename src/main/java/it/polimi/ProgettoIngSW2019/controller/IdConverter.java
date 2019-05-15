package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.model.GameTable;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.model.WeaponCard;

public class IdConverter {
    private GameTable gameTable;

    public IdConverter (GameTable gameTable){
        this.gameTable = gameTable;
    }

    public Player getPlayerById(int idPlayer){
        Player userPlayer;
        userPlayer = gameTable.getPlayers()[idPlayer];

        return userPlayer;
    }

    public Square getSquareByCoordinates(int[] coordintes){
        Square userSquareToMove = null;
        int row = coordintes[0];
        int column = coordintes[1];

        if(row != -1 && column != -1)
            userSquareToMove = gameTable.getMap()[row][column];

        return  userSquareToMove;
    }

    public WeaponCard getLoadedWeaponById(int idPlayer, int idWeapon){
        WeaponCard weaponCard;
        weaponCard = gameTable.getPlayers()[idPlayer].getLoadedWeapons().get(idWeapon);

        return weaponCard;
    }

    public WeaponCard getUnloadedWeaponById(int idPlayer, int idWeapon){
        WeaponCard weaponCard;
        weaponCard = gameTable.getPlayers()[idPlayer].getUnloadedWeapons().get(idWeapon);

        return weaponCard;
    }
}
