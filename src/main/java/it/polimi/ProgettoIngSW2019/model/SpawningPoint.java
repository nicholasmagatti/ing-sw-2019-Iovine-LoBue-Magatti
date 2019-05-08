package it.polimi.ProgettoIngSW2019.model;

import java.util.ArrayList;
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
     * @param idRoom identify the room of appartenance
     * @param isBlockedAtWest identify if there is a wall at its left
     * @param isBlockedAtEast identify if there is a wall at its right
     * @param isBlockedAtNorth identify if there is a wall at its up
     * @param isBlockedAtSouth identify if there is a wall at its down
     * @Author: Luca Iovine
     */
    public SpawningPoint(int idRoom, Boolean isBlockedAtNorth, Boolean isBlockedAtEast, Boolean isBlockedAtSouth, Boolean isBlockedAtWest) {
        super(idRoom, isBlockedAtNorth, isBlockedAtEast, isBlockedAtSouth, isBlockedAtWest);
        weaponCards = new ArrayList<>();
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
     * @param weaponCardToGrab card to grab
     * @return weapon card grabbed from the game table by the player
     * @Author: Luca Iovine
     */
    public void removeWeaponFromSpawnPoint(WeaponCard weaponCardToGrab){
        weaponCards.remove(weaponCardToGrab);
    }
    /**
     * At the end of the turn every weapon card missing on the table has to be replaced
     *
     * @param deck weapon deck passed from GameTable
     * @Author: Luca Iovine
     */
    @Override
    public void reset(Deck deck){
        while(weaponCards.size() != MAX_CARD){
            weaponCards.add((WeaponCard) deck.drawCard());
        }
    }

    /**
     * In case someone wanna grab a new weapon but he alredy has 3 weapon card
     *
     * @Author: Luca Iovine
     */
    public void swapWeaponsOnSpawnPoint(WeaponCard cardToGrab, WeaponCard cardToFree){
        weaponCards.remove(cardToGrab);
        weaponCards.add(cardToFree);
    }
}
