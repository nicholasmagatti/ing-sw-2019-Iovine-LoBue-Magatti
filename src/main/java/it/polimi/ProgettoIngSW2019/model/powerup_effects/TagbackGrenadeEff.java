package it.polimi.ProgettoIngSW2019.model.powerup_effects;

import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.enums.AreaOfEffect;

import java.util.List;


/**
 * Class effect Tagback Grenade (powerUp)
 * @author Priscilla Lo Bue
 */
public class TagbackGrenadeEff implements PowerUpEffect {
    private DistanceDictionary d;

    /**
     * the effect of the card
     * @param ammoDiscard           ammo to be discarded to do the effect (null)
     * @param playerTarget          player target of the powerUp effect
     * @param playerOwner           player who use the effect of the powerUp
     * @param position              a square, the final position (null)
     */
    @Override
    public void activateEffect(AmmoType ammoDiscard, Player playerTarget, Player playerOwner, Square position){
        playerOwner.markPlayer(1, playerTarget);
    }


    /**
     * Check if the powerUp can be activated.
     * The target must be visible from the owner
     * @param playerOwner       player who use the effect of the powerUp
     * @param playerTarget      player target of the powerUp effect
     * @return                  a boolean, if the target is visible
     */
    public boolean canSeeTarget(Player playerOwner, Player playerTarget) {
        Square positionOwner = playerOwner.getPosition();
        Square positionTarget = playerTarget.getPosition();

        List<Square> posCanSee = d.getTargetPosition(AreaOfEffect.CAN_SEE, positionOwner);

        return posCanSee.contains(positionTarget);
    }
}
