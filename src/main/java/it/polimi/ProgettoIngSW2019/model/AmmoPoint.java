package it.polimi.ProgettoIngSW2019.model;

import it.polimi.ProgettoIngSW2019.model.enums.InfoCardinal;
import it.polimi.ProgettoIngSW2019.model.enums.RoomColor;

import java.util.ArrayList;

public class AmmoPoint extends Square {

    private ArrayList<AmmoCard> AmmoCards;

    public AmmoPoint(RoomColor roomColor, InfoCardinal westSide, InfoCardinal eastSide, InfoCardinal northSide, InfoCardinal southSide) {
        super(roomColor, false, westSide, eastSide, northSide, southSide);
    }

    public ArrayList<AmmoCard> getAmmoCards() {
        return this.AmmoCards;
    }

    public void setAmmoCards() {
        //TODO
    }
}