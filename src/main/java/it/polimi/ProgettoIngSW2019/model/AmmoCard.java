package it.polimi.ProgettoIngSW2019.model;

import it.polimi.ProgettoIngSW2019.model.enums.AmmoType;

import java.util.ArrayList;

public class AmmoCard extends Card{
    private ArrayList<AmmoType> ammo;
    private boolean powerUp;

    public void AmmoCard (AmmoType ammo1, AmmoType ammo2, AmmoType ammo3) {

    }


    public void AmmoCard (AmmoType ammo1, AmmoType ammo2) {

    }


    public ArrayList<AmmoType> getAmmo () {
        //TODO: temporary variable to make Sonar work (it will be deteted)
        ArrayList<AmmoType> temporaryVariable = new ArrayList<AmmoType>();
        return temporaryVariable;
    }


    public boolean hasPowerUp () {
        //TODO: temporary variable to make Sonar work (it will be deteted)
        boolean temporaryVariable = true;
        return temporaryVariable;
    }
}
