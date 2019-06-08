package it.polimi.ProgettoIngSW2019.common.Message.toController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Priscilla Lo Bue
 */
public class ReloadChoiceRequest extends PlayerChoiceRequest {
    private int idWeaponToReload;
    private int[] ammoToDiscard;
    private List<Integer> idPowerUpToDiscard;


    /**
     * Constructor
     * @param idPlayer          id Player
     * @param idWeaponToReload  id Weapon to reload
     */
    public ReloadChoiceRequest(int idPlayer, int idWeaponToReload, int[] ammoToDiscard, List<Integer> idPowerUpToDiscard) {
        super(idPlayer);

        if(idWeaponToReload < 0)
            throw new IllegalArgumentException("IdWeapon cannot be negative");

        this.idWeaponToReload = idWeaponToReload;
        this.ammoToDiscard = ammoToDiscard;
        this.idPowerUpToDiscard = new ArrayList<>(idPowerUpToDiscard);
    }


    /**
     * get the id of the weapon to reload
     * @return id weapon
     */
    public int getIdWeaponToReload() {
        return idWeaponToReload;
    }


    /**
     *
     * @return  array of number of ammo to discard/spend
     */
    public int[] getAmmoToDiscard() {
        return ammoToDiscard;
    }


    /**
     *
     * @return  list id powerUps
     */
    public List<Integer> getIdPowerUpToDiscard() {
        return idPowerUpToDiscard;
    }
}
