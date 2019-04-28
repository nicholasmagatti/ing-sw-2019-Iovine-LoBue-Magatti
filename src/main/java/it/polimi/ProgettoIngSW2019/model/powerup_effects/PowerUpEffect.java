package it.polimi.ProgettoIngSW2019.model.powerup_effects;

import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.model.enums.AmmoType;


/**
 * Interface PowerUpEffect
 * @author Priscilla Lo Bue
 */
public interface PowerUpEffect {

    /**
     * effect of the powerUp cards
     * @param playerTarget          player target of the powerUp effect
     * @param playerOwner           player who use the effect of the powerUp
     * @param position              a square, the final position
     */
    void activateEffect(AmmoType ammoDiscard, Player playerTarget, Player playerOwner, Square position);
}

