package it.polimi.ProgettoIngSW2019.view;

import it.polimi.ProgettoIngSW2019.client_controller.*;
import it.polimi.ProgettoIngSW2019.utilities.Observable;

public class ReloadState extends Observable<UserInputEvent> implements IState {

    @Override
    public void menu() {

    }

    private String showAmmoInBox(){
        return "";
    }

    private String showReloadCost(int idWeaponCard){
        return "";
    }
}
