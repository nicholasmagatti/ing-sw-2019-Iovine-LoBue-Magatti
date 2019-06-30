package it.polimi.ProgettoIngSW2019.common.LightModel;

import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;

public class PowerUpLM {
    private int idPowerUp;
    private String name;
    private String description;
    private AmmoType gainAmmoColor;


    /**
     * Constructor
     * @param idPowerUp         id powerUp
     * @param name              name powerUp
     * @param description       deacription powerUp
     * @param gainAmmoColor     ammo powerUp
     */
    public PowerUpLM(int idPowerUp, String name, String description, AmmoType gainAmmoColor) {
        if(idPowerUp < 0)
            throw new IllegalArgumentException("The id cannot be negative");

        if(name == null)
            throw new NullPointerException("Name of the weapon cannot be null");

        if(description == null)
            throw new NullPointerException("Description of the weapon cannot be null");

        this.idPowerUp = idPowerUp;
        this.name = name;
        this.description = description;
        this.gainAmmoColor = gainAmmoColor;
    }



    /**
     * Get the id of the PowerUp to visualize
     * @return     int, the id
     */
    public int getIdPowerUp() {
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
