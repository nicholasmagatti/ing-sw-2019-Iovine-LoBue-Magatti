package it.polimi.ProgettoIngSW2019.model.modelView;

import it.polimi.ProgettoIngSW2019.model.enums.AmmoType;

public class PowerUpCardView {
    private int idCard;
    private String name;
    private AmmoType gainAmmoColor;
    private String effectDescription;

    public int getIdCard() {
        return idCard;
    }

    public String getName() {
        return name;
    }

    public AmmoType getGainAmmoColor() {
        return gainAmmoColor;
    }

    public String getEffectDescription() {
        return effectDescription;
    }

    //TODO: override of toString
}
