package it.polimi.ProgettoIngSW2019.common.Message.toView;

import it.polimi.ProgettoIngSW2019.common.LightModel.PowerUpLM;

import java.util.List;

public class PayAmmoList {
    private int idWeapon;
    private List<List<PowerUpLM>> powerUpsToUse;


    public PayAmmoList(int idWeapon, List<List<PowerUpLM>> powerUpsToUse) {
        this.idWeapon = idWeapon;
        this.powerUpsToUse = powerUpsToUse;
    }


    public int getIdWeapon() {
        return idWeapon;
    }


    public List<List<PowerUpLM>> getPowerUpsToUse() {
        return powerUpsToUse;
    }
}
