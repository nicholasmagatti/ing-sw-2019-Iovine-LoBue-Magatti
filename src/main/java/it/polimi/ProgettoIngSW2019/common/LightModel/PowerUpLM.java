package it.polimi.ProgettoIngSW2019.common.LightModel;

import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;

public class PowerUpLM {
    private int idPowerUp;
    private String name;
    private String description;
    private AmmoType gainAmmoColor;


    /**
     * Get the id of the PowerUp to visualize
     * @return     int, the id
     */
    public int getIdWeapon() {
        return this.idPowerUp;
    }


    /**
     * Get the name of the powerUp
     * @return  String, the name
     */
    public String getName() {
        return this.name;
    }


    /**
     * Get the description of the PowerUp effect
     * @return  string, the description
     */
    public String getDescription() {
        return this.description;
    }


    /**
     * Get the ammo color of the PowerUp
     * @return AmmoType, ammo of the powerUp
     */
    public AmmoType getGainAmmoColor() {
        return this.gainAmmoColor;
    }
}
