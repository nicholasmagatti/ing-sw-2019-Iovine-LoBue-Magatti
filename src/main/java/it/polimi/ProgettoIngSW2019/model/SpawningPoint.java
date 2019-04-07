package it.polimi.ProgettoIngSW2019.model;

import it.polimi.ProgettoIngSW2019.model.enums.InfoCardinal;
import it.polimi.ProgettoIngSW2019.model.enums.RoomColor;

import java.util.ArrayList;

public class SpawningPoint extends Square {

    private ArrayList<WeaponCard> weaponCards;

    SpawningPoint(RoomColor roomColor, InfoCardinal westSide, InfoCardinal eastSide, InfoCardinal northSide, InfoCardinal sudSide) {
        super(roomColor, false, westSide, eastSide, northSide, sudSide);
    }

   public ArrayList<WeaponCard> getWeaponCards(){
        return this.weaponCards;
    }

    public void setWeaponCards(){
        //TODO
    }
}
