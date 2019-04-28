package it.polimi.ProgettoIngSW2019.model;

import it.polimi.ProgettoIngSW2019.model.enums.RoomColor;

import java.util.List;
/**
 * Class AmmoPoint extends Square
 *
 * @Author: Luca Iovine
 */
public class AmmoPoint extends Square {

    private AmmoCard ammoCard;

    AmmoPoint(RoomColor roomColor, int idRoom, Boolean isBlockedAtWeast, Boolean isBlockedAtEast, Boolean isBlockedAtNorth, Boolean isBlockedAtSouth) {
        super(roomColor, idRoom, isBlockedAtWeast, isBlockedAtEast, isBlockedAtNorth, isBlockedAtSouth);
    }

    /**
     * @return ammo card placed on the square
     * @Author: Luca Iovine
     */
    public AmmoCard getAmmoCard() {
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
     * @param deck ammo deck passed from GameTable
     * @Author: Luca Iovine
     */
    @Override
    public void reset(Deck deck) {
        if(ammoCard == null){
            ammoCard = (AmmoCard) deck.drawCard();
        }
    }
}