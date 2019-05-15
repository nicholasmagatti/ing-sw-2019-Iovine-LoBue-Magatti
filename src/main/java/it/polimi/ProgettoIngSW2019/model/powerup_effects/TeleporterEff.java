package it.polimi.ProgettoIngSW2019.model.powerup_effects;

import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.common.enums.*;


/**
 * Class effect Teleporter card (powerUp)
 * @author Priscilla Lo Bue
 */
public class TeleporterEff implements PowerUpEffect {

    /**
     * effect of the card
     * @param ammoDiscard           ammo to be discarded to do the effect (null)
     * @param playerTarget          player target of the powerUp effect (null)
     * @param playerOwner           player who use the effect of the powerUp
     * @param position              a square, the final position
     */
    @Override
    public void activateEffect(AmmoType ammoDiscard, Player playerTarget, Player playerOwner, Square position) {
        playerOwner.moveTo(position);
    }
}
