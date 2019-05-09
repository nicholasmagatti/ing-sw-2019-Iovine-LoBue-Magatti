package it.polimi.ProgettoIngSW2019.view.state;

import it.polimi.ProgettoIngSW2019.common.message.*;
import it.polimi.ProgettoIngSW2019.utilities.Observable;

public class ReloadState extends Observable<UserInputMessage> implements IState {

    @Override
    public void menu(StateContext stateContext) {

    }

    private String showAmmoInBox(){
        return "";
    }

    private String showReloadCost(int idWeaponCard){
        return "";
    }
}
