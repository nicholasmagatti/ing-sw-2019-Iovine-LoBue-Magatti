package it.polimi.ProgettoIngSW2019.model;

import it.polimi.ProgettoIngSW2019.common.enums.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class AmmoCard: the card with ammo you can grab from the board
 * @author Priscilla Lo Bue
 */
public class AmmoCard extends Card{
    private List<AmmoType> ammo = new ArrayList<>();
    private boolean powerUp;

    /**
     * This is the constructor, if the cards has 3 Ammo
     * @param idCard    the id card (identify the card)
     * @param cardType  type of the card: ammo, powerUp, weapon
     * @param ammo1     The first Ammo color
     * @param ammo2     The second Ammo color
     * @param ammo3     The third Ammo color
     */
    public AmmoCard (int idCard, DeckType cardType, AmmoType ammo1, AmmoType ammo2, AmmoType ammo3) {
        super(idCard, cardType);
        ammo.add(ammo1);
        ammo.add(ammo2);
        ammo.add(ammo3);
        this.powerUp = false;

    }


    /**
     * This is the constructor, if the cards has 2 Ammo and 1 PowerUp
     * @param idCard    the id card (identify the card)
     * @param cardType  type of the card
     * @param ammo1     The first Ammo color
     * @param ammo2     The second Ammo color
     */
    public AmmoCard (int idCard, DeckType cardType, AmmoType ammo1, AmmoType ammo2) {
        super(idCard, cardType);
        ammo.add(ammo1);
        ammo.add(ammo2);
        this.powerUp = true;
    }


    /**
     * Get the Ammo in the card
     * @return      a list of Ammo color of the card
     */
    public List<AmmoType> getAmmo () {
        return this.ammo;
    }


    /**
     * Get if the card has PowerUp
     * @return  true if the card has PowerUp, or false if doesn't
     */
    public boolean hasPowerUp () {
        return this.powerUp;
    }
}
