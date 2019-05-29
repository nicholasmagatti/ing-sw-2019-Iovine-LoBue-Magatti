package it.polimi.ProgettoIngSW2019.common.LightModel;

import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nicholas Magatti
 */
public class AmmoCardLM {
    private List<AmmoType> ammo = new ArrayList<>();
    private boolean powerUp;

    /**
     * This is the constructor, if the cards has 3 Ammo and no powerup
     * @param ammo1     The first Ammo color
     * @param ammo2     The second Ammo color
     * @param ammo3     The third Ammo color
     */
    public AmmoCardLM (AmmoType ammo1, AmmoType ammo2, AmmoType ammo3) {
        ammo.add(ammo1);
        ammo.add(ammo2);
        ammo.add(ammo3);
        this.powerUp = false;

    }

    /**
     * This is the constructor, if the cards has 2 Ammo and 1 PowerUp
     * @param ammo1     The first Ammo color
     * @param ammo2     The second Ammo color
     */
    public AmmoCardLM (AmmoType ammo1, AmmoType ammo2) {
        ammo.add(ammo1);
        ammo.add(ammo2);
        this.powerUp = true;
    }

    public List<AmmoType> getAmmo() {
        return ammo;
    }

    public boolean hasPowerup(){
        return powerUp;
    }
}
