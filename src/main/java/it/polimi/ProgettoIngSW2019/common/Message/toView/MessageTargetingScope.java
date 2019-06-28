package it.polimi.ProgettoIngSW2019.common.Message.toView;

import it.polimi.ProgettoIngSW2019.common.LightModel.PowerUpLM;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;

public class MessageTargetingScope extends Message {
    private PowerUpLM powerUpUsed;
    private AmmoType ammoDiscarded;
    private String nameTarget;


    public MessageTargetingScope(int idPlayer, String namePlayer, PowerUpLM powerUpUsed, AmmoType ammoDiscarded, String nameTarget) {
        super(idPlayer, namePlayer);
        this.powerUpUsed = powerUpUsed;
        this.ammoDiscarded = ammoDiscarded;
        this.nameTarget = nameTarget;
    }


    public PowerUpLM getPowerUpUsed() {
        return powerUpUsed;
    }


    public AmmoType getAmmoDiscarded() {
        return ammoDiscarded;
    }


    public String getNameTarget() {
        return nameTarget;
    }
}
