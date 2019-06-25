package it.polimi.ProgettoIngSW2019.common.Message.toView;


import it.polimi.ProgettoIngSW2019.common.LightModel.PowerUpLM;
import it.polimi.ProgettoIngSW2019.model.PowerUp;

import java.util.ArrayList;
import java.util.List;

/**
 * Spawn Draw Cards Response
 */
public class DrawCardsInfoResponse extends InfoResponse {
    private List<PowerUpLM> powerUps;


    /**
     * Constructor
     * @param idPlayer          id spawn player
     */
    public DrawCardsInfoResponse(int idPlayer, List<PowerUpLM> powerUps) {
        super(idPlayer);

        if(powerUps == null)
            throw new NullPointerException("powerUps cannot be null");

        this.powerUps = powerUps;
    }


    /**
     * get list of powerUps the player has in hand
     * the player must choose one to spawn
     * @return  list of powerUps
     */
    public List<PowerUpLM>  getSpawnPlayerLM() {
        return powerUps;
    }

}
