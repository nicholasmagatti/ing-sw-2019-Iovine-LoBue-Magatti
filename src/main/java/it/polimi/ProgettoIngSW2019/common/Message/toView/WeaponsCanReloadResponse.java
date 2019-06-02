package it.polimi.ProgettoIngSW2019.common.Message.toView;

import it.polimi.ProgettoIngSW2019.common.LightModel.WeaponLM;
import java.util.ArrayList;
import java.util.List;

/**
 * Reload info class
 * @author Priscilla Lo Bue
 */
public class WeaponsCanReloadResponse extends InfoResponse {
    private List<WeaponLM> weaponsCanReload;
    private List<PayAmmoList> listPaymentReload;


    /**
     * Constructor from controller to view
     * @param idPlayer              id player that wants to reload a weapon
     * @param weaponsCanReload      list of weapon the player can reload
     */
    public WeaponsCanReloadResponse(int idPlayer, List<WeaponLM> weaponsCanReload, List<PayAmmoList> listPaymentReload) {
        super(idPlayer);
        this.weaponsCanReload = new ArrayList<>(weaponsCanReload);
        this.listPaymentReload = listPaymentReload;
    }


    /**
     * get list of weapons the player can reload
     * @return  list of weapons
     */
    public List<WeaponLM> getWeaponsCanReload() {
        return weaponsCanReload;
    }


    public List<PayAmmoList> getListPaymentReload() {
        return listPaymentReload;
    }
}
