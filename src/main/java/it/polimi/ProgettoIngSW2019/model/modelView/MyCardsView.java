package it.polimi.ProgettoIngSW2019.model.modelView;

import java.util.ArrayList;

public class MyCardsView {
    private int idPlayer;
    private ArrayList<WeaponCardView> loadedWeapons;
    private ArrayList<PowerUpCardView> powerUps;

    public int getIdPlayer() {
        return idPlayer;
    }

    public ArrayList<WeaponCardView> getLoadedWeapons() {
        return loadedWeapons;
    }

    public ArrayList<PowerUpCardView> getPowerUps() {
        return powerUps;
    }

    //TODO: override of toString
}
