package it.polimi.ProgettoIngSW2019.model.powerup_effects;

import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.PowerUp;
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
    /**
     * the effect of the card
     * @param ammoDiscard           ammo to be discarded to do the effect (null)
     * @param powerUpToDiscard      powerUp to discard
     * @param playerTarget          player target of the powerUp effect
     * @param playerOwner           player who use the effect of the powerUp
     * @param position              a square, the final position (null)
     */
    @Override
    public void activateEffect(AmmoType ammoDiscard, PowerUp powerUpToDiscard, Player playerTarget, Player playerOwner, Square position){
        playerOwner.markPlayer(1, playerTarget);
    }

}
