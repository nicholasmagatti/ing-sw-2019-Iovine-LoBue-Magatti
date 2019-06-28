package it.polimi.ProgettoIngSW2019.common.Message.toView;

import it.polimi.ProgettoIngSW2019.common.LightModel.PowerUpLM;
import java.util.ArrayList;
import java.util.List;

/**
 * for each weapon payment this class tells the way the player can pay this weapon
 * @author Priscilla Lo Bue
 */
public class PayAmmoList {
    private int idWeapon;
    private int[] ammoCost;
    private int[] ammoInAmmoBox;
    private List<PowerUpLM> ammoInPowerUp;


    /**
     * Constructor
     * @param idWeapon             id weapon to pay
     * @param ammoCost             array int cost in ammo of the weapon
     * @param ammoInAmmoBox        array int ammo in the box he can use
     * @param ammoInPowerUp        list of powerUps with ammo the player can use
     */
    public PayAmmoList(int idWeapon, int[] ammoCost, int[] ammoInAmmoBox, List<PowerUpLM> ammoInPowerUp) {
        if(idWeapon < 0)
            throw new IllegalArgumentException("id weapon cannot be negative");

        if(ammoInPowerUp == null)
            throw new NullPointerException("ammo in powerUp cannot be null");

        this.idWeapon = idWeapon;
        this.ammoCost = ammoCost;
        this.ammoInAmmoBox = ammoInAmmoBox;
        this.ammoInPowerUp = new ArrayList<>(ammoInPowerUp);
    }


    /**
     * @return  id of the weapon
     */
    public int getIdWeapon() {
        return idWeapon;
    }


    /**
     * @return array int of ammo to spend
     */
    public int[] getAmmoCost() {
        return ammoCost;
    }


    /**
     * @return array int ammo in the box
     */
    public int[] getAmmoInAmmoBox() {
        return ammoInAmmoBox;
    }


    /**
     * @return a list of powerUpsLM to discard as ammo
     */
    public List<PowerUpLM> getAmmoInPowerUp() {
        return ammoInPowerUp;
    }
}
