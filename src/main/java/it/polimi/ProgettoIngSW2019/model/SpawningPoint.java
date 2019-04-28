package it.polimi.ProgettoIngSW2019.model;

import it.polimi.ProgettoIngSW2019.model.enums.RoomColor;

import java.util.List;

/**
 * Class SpawningPoint extends Square
 *
 * @Author: Luca Iovine
 */
public class SpawningPoint extends Square {
    private final static int MAX_CARD = 3;
    private List<WeaponCard> weaponCards;

    /**
     * Constructor
     *
     * @param roomColor
     * @param idRoom
     * @param isBlockedAtWeast
     * @param isBlockedAtEast
     * @param isBlockedAtNorth
     * @param isBlockedAtSouth
     * @Author: Luca Iovine
     */
    SpawningPoint(RoomColor roomColor, int idRoom, Boolean isBlockedAtWeast, Boolean isBlockedAtEast, Boolean isBlockedAtNorth, Boolean isBlockedAtSouth) {
        super(roomColor, idRoom, isBlockedAtWeast, isBlockedAtEast, isBlockedAtNorth, isBlockedAtSouth);
    }

    /**
     * @return the list of wapon on the square
     * @Author: Luca Iovine
     */
    public List<WeaponCard> getWeaponCards(){
        return this.weaponCards;
    }
    /**
     * Pick the Weapon Card chosen by the player and remove it from the table
     *
     * @param idCard identifier for the WeaponCards
     * @return weapon card grabbed from the game table by the player
     * @Author: Luca Iovine
     */
    public WeaponCard grabCard(int idCard){
        WeaponCard w = null;

        for(int i = 0; i < MAX_CARD; i++){
            if(idCard == weaponCards.get(i).getIdCard()){
                w = weaponCards.get(i);
                weaponCards.remove(i);
            }
        }
        return w;
    }
    /**
     * At the end of the turn every weapon card missing on the table has to be replaced
     *
     * @param deck weapon deck passed from GameTable
     * @Author: Luca Iovine
     */
    @Override
    public void reset(Deck deck){
        for(int i = 0; i < MAX_CARD; i++){
            if(weaponCards.get(i) == null){
                weaponCards.get(i) = (WeaponCard) deck.drawCard();
            }
        }
    }

    /**
     * In case someone wanna grab a new weapon but he alredy has 3 weapon card
     *
     * @Author: Luca Iovine
     */
    public WeaponCard swapWeaponCard(WeaponCard cardToGrab, WeaponCard cardToFree){
        WeaponCard cardGrabbed = cardToGrab;
        for(WeaponCard w: weaponCards){
            if(cardToGrab == w){
                w = cardToFree;
            }
        }
        return cardGrabbed;
    }
}
