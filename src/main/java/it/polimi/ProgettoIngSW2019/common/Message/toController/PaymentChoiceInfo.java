package it.polimi.ProgettoIngSW2019.common.Message.toController;

import java.util.List;

public class PaymentChoiceInfo {
    private int[] ammoToDiscard;
    private List<Integer> idPowerUpToDiscard;

    public PaymentChoiceInfo(int[] ammoToDiscard, List<Integer> idPowerUpToDiscard){
        this.ammoToDiscard = ammoToDiscard;
        this.idPowerUpToDiscard = idPowerUpToDiscard;
    }

    public int[] getAmmoToDiscard() {
        return ammoToDiscard;
    }

    public List<Integer> getIdPowerUpToDiscard() {
        return idPowerUpToDiscard;
    }
}
