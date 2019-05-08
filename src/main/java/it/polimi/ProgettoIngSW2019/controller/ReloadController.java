package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.model.AmmoBox;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.WeaponCard;
import it.polimi.ProgettoIngSW2019.common.message.ReloadMessage;
import it.polimi.ProgettoIngSW2019.utilities.Observer;

public class ReloadController implements Observer<ReloadMessage> {
    private WeaponCard weaponToReload;
    private Player ownerPlayer;
    private AmmoBox ammoBox;


    public void update(ReloadMessage reloadMessage) {

    }


    public void reload() {

    }
}
