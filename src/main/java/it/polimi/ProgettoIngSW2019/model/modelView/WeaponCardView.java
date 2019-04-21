package it.polimi.ProgettoIngSW2019.model.modelView;

import it.polimi.ProgettoIngSW2019.model.enums.AmmoType;

import java.util.List;

public class WeaponCardView {
    private int idCard;
    private String name;
    private String effects;
    private List<AmmoType> cost;

    public int getIdCard() {
        return idCard;
    }

    public String getName() {
        return name;
    }

    public String getEffects() {
        return effects;
    }

    public List<AmmoType> getCost() {
        return cost;
    }

    //TODO: override of toString
}
