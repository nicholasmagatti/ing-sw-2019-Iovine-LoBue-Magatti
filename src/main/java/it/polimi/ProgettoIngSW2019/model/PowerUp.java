package it.polimi.ProgettoIngSW2019.model;

import it.polimi.ProgettoIngSW2019.model.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.model.powerup_effects.PowerUpEffect;
import it.polimi.ProgettoIngSW2019.model.enums.DeckType;

/**
 * Class PowerUp: the card PowerUp you can draw
 * @author Priscilla Lo Bue
 */

public class PowerUp extends Card {

    private String name;
    private String description;
    private AmmoType gainAmmoColor;
    private PowerUpEffect powerUpEffect;


    /**
     * This is the constructor
     * @param idCard        the id card (identify the card)
     * @param cardType      type of the card: ammo, powerUp, weapon
     * @param ammo          ammo you can gain from the powerUp
     * @param name          name of powerUp
     * @param description   description of the effect of the powerUp
     * @param powerUpEffect effect of the powerUp
     */
    public PowerUp (int idCard, DeckType cardType, AmmoType ammo, String name, String description, PowerUpEffect powerUpEffect) {
        super(idCard, cardType);
        if(name == null)
            throw new NullPointerException("the `name` must be not null");
        if(description == null)
            throw new NullPointerException("the `description` must be not null");
        if(powerUpEffect == null)
            throw new NullPointerException("the powerUpEffect cannot be null");

        this.name  = name;
        this.description = description;
        this.gainAmmoColor = ammo;
        this.powerUpEffect = powerUpEffect;
    }



    /**
     * get the name of the powerUp card
     * @return  name, a string
     */
    public String getName () {
        return name;
    }



    /**
     * get the description of the powerUp card
     * @return      description, a string
     */
    public String getDescription () {
        return description;
    }



    /**
     *Get the ammo color in the PowerUp card
     * @return      ammoType ammo color
     */
    public AmmoType getGainAmmoColor (){
        return gainAmmoColor;
    }



    /**
     * Show the info of the PowerUp card
     * @return      msg, a string with all the info
     */
    public String showPowerUpInfo() {
        String msg;
        msg = "Name: " + getName() + "\nDescription: " + getDescription() + "\nAmmo: " + this.getGainAmmoColor();
        return msg;
    }



    public PowerUpEffect getPowerUpEffect() {
        return powerUpEffect;
    }
}