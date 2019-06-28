package it.polimi.ProgettoIngSW2019.common.Message.toController;

import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;

/**
 * Message from view to use Targeting scope powerUp
 * @author Priscilla Lo Bue
 */
public class TargetingScopeRequest extends PowerUpChoiceRequest {
    private int idTarget;
    private AmmoType ammoToSpend;
    private int idPowerUpToSpend;

    /**
     * Constructor
     * @param hostName          host name of the client
     * @param idPlayer          id of the player
     * @param idPowerUp         id of the powerUp to use
     * @param idTarget          id of the target player
     * @param ammoToSpend       ammo to spend to use the powerUp
     * @param idPowerUpToSpend  id powerUp to discard as ammo
     */
    public TargetingScopeRequest(String hostName, int idPlayer, int idPowerUp, int idTarget, AmmoType ammoToSpend, int idPowerUpToSpend) {
        super(hostName, idPlayer, idPowerUp);
        this.idTarget = idTarget;
        this.ammoToSpend = ammoToSpend;
        this.idPowerUpToSpend = idPowerUpToSpend;
    }


    /**
     * @return      return the id of the target player
     */
    public int getIdTarget() {
        return idTarget;
    }


    /**
     * @return      the ammo to spend to use the powerUp
     */
    public AmmoType getAmmoToSpend() {
        return ammoToSpend;
    }


    /**
     * @return      id of the powerUp to discard as ammo
     */
    public int getIdPowerUpToSpend() {
        return idPowerUpToSpend;
    }
}
