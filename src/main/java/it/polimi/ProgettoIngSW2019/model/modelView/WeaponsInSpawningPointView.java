package it.polimi.ProgettoIngSW2019.model.modelView;

import it.polimi.ProgettoIngSW2019.model.enums.RoomColor;

import java.util.ArrayList;

public class WeaponsInSpawningPointView {
    private RoomColor colorSP;
    private ArrayList<WeaponCardView> weapons;

    public RoomColor getColorSP() {
        return colorSP;
    }

    public ArrayList<WeaponCardView> getWeapons() {
        return weapons;
    }

    //TODO: override of toString
}
