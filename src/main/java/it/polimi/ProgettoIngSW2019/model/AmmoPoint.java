package it.polimi.ProgettoIngSW2019.model;

import java.util.ArrayList;

public class AmmoPoint extends Square {

    private ArrayList<AmmoCard> AmmoCards;

    AmmoPoint(RoomColor roomColor, int positionX, int positionY, InfoCardinal weastSide, InfoCardinal eastSide, InfoCardinal northSide, InfoCardinal sudSide) {
        super(roomColor, positionX, positionY, weastSide, eastSide, northSide, sudSide);
    }

    public ArrayList<AmmoCard> getAmmoCards() {
        return this.AmmoCards;
    }

    public void setAmmoCards() {
        //TODO
    }
}