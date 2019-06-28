package it.polimi.ProgettoIngSW2019.model.powerup_effects;

import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.PowerUp;
import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.common.enums.*;


/**
 * Class effect Newton (powerUp)
 * @author Priscilla Lo Bue
 */
public class NewtonEff implements PowerUpEffect {

    /**
     * the effect of the card
     * @param ammoDiscard           ammo to be discarded to do the effect (null)
     * @param powerUp               powerUp to discard (null)
     * @param playerTarget          player target of the powerUp effect
     * @param playerOwner           player who use the effect of the powerUp (null)
     * @param position              a square, the final position
     */
    @Override
    public void activateEffect(AmmoType ammoDiscard, PowerUp powerUp, Player playerTarget, Player playerOwner, Square position){
        playerTarget.moveTo(position);
    }
}
