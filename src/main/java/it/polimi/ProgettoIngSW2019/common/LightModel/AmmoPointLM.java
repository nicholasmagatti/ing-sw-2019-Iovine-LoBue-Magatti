package it.polimi.ProgettoIngSW2019.common.LightModel;

import it.polimi.ProgettoIngSW2019.common.enums.SquareType;

import java.util.List;

/**
 * @author Nicholas Magatti
 */
public class AmmoPointLM extends SquareLM {

    private AmmoCardLM ammoCard;

    /**
     * Constructor
     * @param players - list of ids of the players on the square
     * @param ammoCard - ammo card on the ammo point if there is one, null otherwise
     */
    public AmmoPointLM(List<Integer> players, AmmoCardLM ammoCard,
                       boolean blockedAtNorth, boolean blockedAtEast, boolean blockedAtSouth, boolean blockedAtWest){

        super(players, SquareType.AMMO_POINT,
                blockedAtNorth, blockedAtEast, blockedAtSouth, blockedAtWest);
        this.ammoCard = ammoCard;
    }

    /**
     * Get ammo card on the ammo point
     * @return ammo card on ammo point if there is one, null otherwise
     */
    public AmmoCardLM getAmmoCard() {
        return ammoCard;
    }
}
