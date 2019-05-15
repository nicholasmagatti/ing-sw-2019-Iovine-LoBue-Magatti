package it.polimi.ProgettoIngSW2019.model;

import it.polimi.ProgettoIngSW2019.common.enums.SquareType;

/**
 * Class AmmoPoint extends Square
 *
 * @Author: Luca Iovine
 */
public class AmmoPoint extends Square {

    private AmmoCard ammoCard;
    /**
     * Constructor
     *
     * @param idRoom identify the room of appartenance
     * @param isBlockedAtWest identify if there is a wall at its left
     * @param isBlockedAtEast identify if there is a wall at its right
     * @param isBlockedAtNorth identify if there is a wall at its up
     * @param isBlockedAtSouth identify if there is a wall at its down
     * @Author: Luca Iovine
     */
    public AmmoPoint(int idRoom, Boolean isBlockedAtNorth, Boolean isBlockedAtEast, Boolean isBlockedAtSouth, Boolean isBlockedAtWest) {
        super(idRoom, isBlockedAtNorth, isBlockedAtEast, isBlockedAtSouth, isBlockedAtWest);
        setSquareType(SquareType.AMMO_POINT);
    }

    /**
     * @return ammo card placed on the square
     * @Author: Luca Iovine
     */
    public AmmoCard getAmmoCard() {
        //TODO: ritornare solo le info?
        return ammoCard;
    }

    /**
     * Pick the ammo card on the square where the player is and remove it from the table
     *
     * @return the ammo card grabbed from the game table by the player
     * @Author: Luca Iovine
     */
    public AmmoCard grabCard() {
        AmmoCard c = ammoCard;
        ammoCard = null;
        return c;
    }
    /**
     * At the end of the turn every ammo card missing on the table has to be replaced
     *
     * @Author: Luca Iovine
     */
    @Override
    public void reset(Deck deck) {
        if(ammoCard == null){
            ammoCard = (AmmoCard) deck.drawCard();
        }
    }
}