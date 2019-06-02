package it.polimi.ProgettoIngSW2019.common.LightModel;

import java.util.ArrayList;
import java.util.List;

/**
 * MyLoadedWeapons class
 * @author Priscilla
 */
public class MyLoadedWeaponsLM {
    private List<WeaponLM> loadedWeapons;


    /**
     * Constructor
     * @param loadedWeapons     loaded weapons
     */
    public MyLoadedWeaponsLM(List<WeaponLM> loadedWeapons) {
        this.loadedWeapons = new ArrayList<>(loadedWeapons);
    }


    /**
     * get list of my loaded weapons
     * @return  list loaded weapons
     */
    public List<WeaponLM> getLoadedWeapons() {
        return loadedWeapons;
    }
}
