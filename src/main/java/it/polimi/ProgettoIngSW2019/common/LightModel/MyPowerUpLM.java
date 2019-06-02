package it.polimi.ProgettoIngSW2019.common.LightModel;

import java.util.ArrayList;
import java.util.List;

/**
 * MyPowerUp class
 * @author Priscilla Lo Bue
 */
public class MyPowerUpLM {
    private List<PowerUpLM> powerUps;



    public MyPowerUpLM(List<PowerUpLM> powerUps) {
        this.powerUps = new ArrayList<>(powerUps);
    }


    /**
     * get list of powerUps in hand
     * @return      list of powerUps
     */
    public List<PowerUpLM> getPowerUps() {
        return powerUps;
    }
}
