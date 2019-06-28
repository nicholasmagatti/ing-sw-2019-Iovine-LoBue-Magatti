package it.polimi.ProgettoIngSW2019.common.Message.toController;

import java.util.ArrayList;
import java.util.List;

/**
 * Message to controller from a player who wants to grab
 * @author Priscilla Lo Bue
 */
public class GrabWeaponChoiceRequest extends PlayerChoiceRequest {
    private int idWeaponToDiscard;
    private int idWeaponToGrab;
    private int[] ammoToDiscard;
    private List<Integer> idPowerUpToDiscard;


    /**
     * Constructor
     * @param hostNamePlayer            host name of the client
     * @param idPlayer                  id of the player
     * @param idWeaponToDiscard         id of the weapon to discard/swap when the player has 3 weapons
     * @param idWeaponToGrab            id weapon to grab from the spawning point
     * @param ammoToDiscard             ammo to spend to buy the weapon
     * @param idPowerUpToDiscard        list of id of the powerUps to discard as ammo
     */
    public GrabWeaponChoiceRequest(String hostNamePlayer, int idPlayer, int idWeaponToDiscard, int idWeaponToGrab, int[] ammoToDiscard, List<Integer> idPowerUpToDiscard) {
        super(hostNamePlayer, idPlayer);
        this.idWeaponToDiscard = idWeaponToDiscard;
        this.idWeaponToGrab = idWeaponToGrab;
        this.ammoToDiscard = ammoToDiscard;
        this.idPowerUpToDiscard = new ArrayList<>(idPowerUpToDiscard);
    }


    /**
     * @return      id of the weapon to discard/swap
     */
    public int getIdWeaponToDiscard() {
        return idWeaponToDiscard;
    }


    /**
     * @return      id of the weapon to grab/buy
     */
    public int getIdWeaponToGrab() {
        return idWeaponToGrab;
    }


    /**
     * @return      list of id of the powerUps to discard as ammo to pay
     */
    public List<Integer> getIdPowerUpToDiscard() {
        return idPowerUpToDiscard;
    }


    /**
     * @return      ammo to spend to pay the weapon
     */
    public int[] getAmmoToDiscard() {
        return ammoToDiscard;
    }
}
