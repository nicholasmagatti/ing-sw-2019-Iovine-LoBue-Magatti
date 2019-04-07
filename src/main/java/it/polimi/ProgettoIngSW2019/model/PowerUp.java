package it.polimi.ProgettoIngSW2019.model;

import it.polimi.ProgettoIngSW2019.model.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.model.interfaces.PowerUpEffect;

public class PowerUp extends Card {

    private String name;
    private String description;
    private AmmoType gainAmmoColor;
    private PowerUpEffect powerUpEffect;


    public PowerUp (AmmoType ammo, String name, String description, PowerUpEffect powerUpEffect) {

    }


    public String getName () {
        return name;
    }


    public String getDescription () {
        return description;
    }


    public AmmoType getGainAmmoColor (){
        return gainAmmoColor;
    }


    public void effect() {

    }


    public void showPowerUpInfo() {

    }
}