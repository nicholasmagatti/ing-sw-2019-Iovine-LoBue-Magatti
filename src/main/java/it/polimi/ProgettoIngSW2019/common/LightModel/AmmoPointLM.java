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
     * @param players - players on the square
     * @param ammoCard - ammo card on the ammo point if there is one, null otherwise
     */
    public AmmoPointLM(List<PlayerDataLM> players, AmmoCardLM ammoCard){

        super(players, SquareType.AMMO_POINT);
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
