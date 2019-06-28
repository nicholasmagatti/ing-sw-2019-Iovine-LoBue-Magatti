package it.polimi.ProgettoIngSW2019.common.Message.toController;

import it.polimi.ProgettoIngSW2019.common.LightModel.PowerUpLM;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;

import java.util.List;

public class TargetingScopeRequest extends PowerUpChoiceRequest {
    private int idTarget;
    private AmmoType ammoToSpend;
    private int idPowerUpToSpend;


    public TargetingScopeRequest(String hostName, int idPlayer, int idPowerUp, int idTarget, AmmoType ammoToSpend, int idPowerUpToSpend) {
        super(hostName, idPlayer, idPowerUp);
        this.idTarget = idTarget;
        this.ammoToSpend = ammoToSpend;
        this.idPowerUpToSpend = idPowerUpToSpend;
    }


    public int getIdTarget() {
        return idTarget;
    }


    public AmmoType getAmmoToSpend() {
        return ammoToSpend;
    }


    public int getIdPowerUpToSpend() {
        return idPowerUpToSpend;
    }
}
