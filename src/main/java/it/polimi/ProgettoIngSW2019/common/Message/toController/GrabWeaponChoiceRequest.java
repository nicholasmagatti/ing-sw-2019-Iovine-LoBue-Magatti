package it.polimi.ProgettoIngSW2019.common.Message.toController;

import java.util.List;

public class GrabWeaponChoiceRequest extends PlayerChoiceRequest {
    private int idWeaponToDiscard;
    private int idWeaponToGrab;
    private int[] ammoToDiscard;
    private List<Integer> idPowerUpToDiscard;


    public GrabWeaponChoiceRequest(int idPlayer, int idWeaponToDiscard, int idWeaponToGrab, int[] ammoToDiscard, List<Integer> idPowerUpToDiscard) {
        super(idPlayer);
        this.idWeaponToDiscard = idWeaponToDiscard;
        this.idWeaponToGrab = idWeaponToGrab;
        this.ammoToDiscard = ammoToDiscard;
        this.idPowerUpToDiscard = idPowerUpToDiscard;
    }


    public int getIdWeaponToDiscard() {
        return idWeaponToDiscard;
    }


    public int getIdWeaponToGrab() {
        return idWeaponToGrab;
    }


    public List<Integer> getIdPowerUpToDiscard() {
        return idPowerUpToDiscard;
    }


    public int[] getAmmoToDiscard() {
        return ammoToDiscard;
    }
}
