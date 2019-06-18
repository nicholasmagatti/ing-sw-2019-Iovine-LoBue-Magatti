package it.polimi.ProgettoIngSW2019.common.Message.toView;

import it.polimi.ProgettoIngSW2019.common.LightModel.WeaponLM;
import java.util.ArrayList;
import java.util.List;

/**
 * Reload info class
 * @author Priscilla Lo Bue
 */
public class WeaponsCanPayResponse extends InfoResponse {
    private List<WeaponLM> weaponsCanPay;
    private List<PayAmmoList> listPaymentWeapons;


    /**
     * Constructor from controller to view
     * @param idPlayer              id player that wants to reload a weapon
     * @param weaponsCanPay         list of weapon the player can pay
     * @param listPaymentWeapons    list payment
     */
    public WeaponsCanPayResponse(int idPlayer, List<WeaponLM> weaponsCanPay, List<PayAmmoList> listPaymentWeapons) {
        super(idPlayer);

        if(weaponsCanPay == null)
            throw new NullPointerException("list weapons cannot be null");
        if(listPaymentWeapons == null)
            throw new NullPointerException("list payment cannot be null");

        this.weaponsCanPay = new ArrayList<>(weaponsCanPay);
        this.listPaymentWeapons = new ArrayList<>(listPaymentWeapons);
    }


    /**
     * get list of weapons the player can reload
     * @return  list of weapons
     */
    public List<WeaponLM> getWeaponsCanReload() {
        return weaponsCanPay;
    }


    /**
     * get list of the payment
     * @return     list pay ammo list
     */
    public List<PayAmmoList> getListPaymentReload() {
        return listPaymentWeapons;
    }
}
