package it.polimi.ProgettoIngSW2019.common.Message.toView;

import it.polimi.ProgettoIngSW2019.common.LightModel.PowerUpLM;
import it.polimi.ProgettoIngSW2019.common.LightModel.WeaponLM;
import it.polimi.ProgettoIngSW2019.common.Message.toView.InfoResponse;

import java.util.List;

/**
 * Shoot info class
 * message with shoot ifo request and response
 * @author Priscilla Lo Bue
 */
public class ShootInfoResponse extends InfoResponse {
    private List<WeaponLM> availableWeapons;
    private List<PowerUpLM> availablePowerUp;


    public ShootInfoResponse(int idPlayer) {
        super(idPlayer);
    }


    /**
     * get the available weapons to be used
     * @return  available weapons
     */
    public List<WeaponLM> getAvailableWeapons() {
        return availableWeapons;
    }



    /**
     * get the available weapons to be used
     * @return  available PowerUps
     */
    public List<PowerUpLM> getAvailablePowerUp() {
        return availablePowerUp;
    }
}