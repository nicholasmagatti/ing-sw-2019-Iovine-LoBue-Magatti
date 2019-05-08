package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.model.WeaponCard;
import it.polimi.ProgettoIngSW2019.utilities.Observer;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.common.message.ShootMessage;

public class ShootController implements Observer<ShootMessage> {
    private Player targetPlayer;
    private Player userPlayer;
    private WeaponCard weaponUsed;

    
    public void update(ShootMessage shootMessage) {

    }


    public void shoot() {

    }
}
