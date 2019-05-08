package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.PowerUp;
import it.polimi.ProgettoIngSW2019.common.message.PowerUpMessage;
import it.polimi.ProgettoIngSW2019.utilities.Observer;

public class PowerUpController implements Observer<PowerUpMessage> {
    private PowerUp powerUpToUse;
    private Player userPlayer;


    public void update(PowerUpMessage powerUpMessage) {

    }


    public void usePowerUp() {

    }
}
