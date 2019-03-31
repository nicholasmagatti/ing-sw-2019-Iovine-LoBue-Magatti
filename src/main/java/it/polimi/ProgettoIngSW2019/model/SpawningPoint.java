package it.polimi.ProgettoIngSW2019.model;

import java.util.ArrayList;

public class SpawningPoint extends Square {

    private ArrayList<WeaponCard> weaponCards;

    SpawningPoint(RoomColor roomColor, int positionX, int positionY, InfoCardinal weastSide, InfoCardinal eastSide, InfoCardinal northSide, InfoCardinal sudSide) {
        super(roomColor, positionX, positionY, weastSide, eastSide, northSide, sudSide);
    }

   public ArrayList<WeaponCard> getWeaponCards(){
        return this.weaponCards;
    }

    public void setWeaponCards(){
        //TODO
    }
}
