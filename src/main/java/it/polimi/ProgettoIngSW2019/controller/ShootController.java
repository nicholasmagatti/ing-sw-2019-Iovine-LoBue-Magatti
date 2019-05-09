package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.common.message.shootmsg.RequestTargetMessage;
import it.polimi.ProgettoIngSW2019.model.WeaponCard;
import it.polimi.ProgettoIngSW2019.utilities.Observer;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.common.message.shootmsg.*;
import it.polimi.ProgettoIngSW2019.virtual_view.ShootVirtualView;

public class ShootController {
    private Player targetPlayer;
    private Player userPlayer;
    private WeaponCard weaponUsed;
    private ShootVirtualView shootVirtualView;

    public ShootController(ShootVirtualView shootVirtualView){
        shootVirtualView.addObserver(new RequestTargetListener());
        shootVirtualView.addObserver(new UseEffectListener());
        this.shootVirtualView = shootVirtualView;
    }

    private class RequestTargetListener implements Observer<ShootMessage> {
        public void update(ShootMessage shootMessage) {
            if(shootMessage instanceof RequestTargetMessage){
                //TODO: nella RequestTargetMessage vanno passati i parametri da dare al client
                shootVirtualView.setMessage(new RequestTargetMessage());
            }
        }
    }

    private class UseEffectListener implements Observer<ShootMessage> {
        public void update(ShootMessage shootMessage) {
            if(shootMessage instanceof UseEffectMessage){
                shoot();
            }
        }
    }


    public void shoot() {
        //TODO:
    }
}
