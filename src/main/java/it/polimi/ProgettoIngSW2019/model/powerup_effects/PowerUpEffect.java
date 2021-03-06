package it.polimi.ProgettoIngSW2019.model.powerup_effects;

import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.PowerUp;
import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.common.enums.*;


/**
 * Interface PowerUpEffect
 * @author Priscilla Lo Bue
 */
public interface PowerUpEffect {

    /**
     * effect of the powerUp cards
     * @param playerTarget          player target of the powerUp effect
     * @param powerUpToDiscard      powerUp to discard
     * @param playerOwner           player who use the effect of the powerUp
     * @param position              a square, the final position
     * @param ammoDiscard           ammo to be discarded to do the effect
     */
    void activateEffect(AmmoType ammoDiscard, PowerUp powerUpToDiscard, Player playerTarget, Player playerOwner, Square position);
}

