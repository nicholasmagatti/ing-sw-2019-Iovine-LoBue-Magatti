package it.polimi.ProgettoIngSW2019.common.Message.toView;

import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;

/**
 * Message to view when a player wants to use the Targeting scope powerUp
 * @author Priscilla Lo Bue
 */
public class MessageTargetingScope extends Message {
    private AmmoType ammoDiscarded;
    private String nameTarget;


    /**
     * Constructor
     * @param idPlayer          id player who uses the powerUp
     * @param namePlayer        name player who uses the powerUp
     * @param ammoDiscarded     ammo discarded to use the targeting scope
     * @param nameTarget        name of the target
     */
    public MessageTargetingScope(int idPlayer, String namePlayer, AmmoType ammoDiscarded, String nameTarget) {
        super(idPlayer, namePlayer);
        this.ammoDiscarded = ammoDiscarded;
        this.nameTarget = nameTarget;
    }


    /**
     * @return      ammo discarded to use the targeting scope
     */
    public AmmoType getAmmoDiscarded() {
        return ammoDiscarded;
    }


    /**
     * @return      name of the target of the targeting scope
     */
    public String getNameTarget() {
        return nameTarget;
    }
}
