package it.polimi.ProgettoIngSW2019.model.powerup_effects;

import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.PowerUp;
import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.common.enums.*;

import java.util.Arrays;


/**
 * Class effect Targeting Scope (powerUp)
 * @author Priscilla Lo Bue
 */
public class TargetingScopeEff implements PowerUpEffect {

    /**
     * effect of the card
     * @param ammoDiscard           ammo to be discarded to do the effect
     * @param powerUpToDiscard      powerUp to discard
     * @param playerTarget          player target of the powerUp effect
     * @param playerOwner           player who use the effect of the powerUp
     * @param position              a square, the final position (null)
     */
    @Override
    public void activateEffect(AmmoType ammoDiscard, PowerUp powerUpToDiscard, Player playerTarget, Player playerOwner, Square position) {
        if(ammoDiscard != null) {
            playerOwner.discardAmmo(ammoDiscard);
            playerOwner.dealDamage(1,playerTarget);
        }
        else if(powerUpToDiscard != null) {
            playerOwner.discardPowerUps(Arrays.asList(powerUpToDiscard));
            playerOwner.dealDamage(1,playerTarget);
        }
        else
            throw new IllegalArgumentException("Sia l'ammo da scartare che il powerUp non possono essere nulli");
    }
}