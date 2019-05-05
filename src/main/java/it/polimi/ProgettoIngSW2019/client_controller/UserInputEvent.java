package it.polimi.ProgettoIngSW2019.client_controller;

import it.polimi.ProgettoIngSW2019.server_controller.IUserInputController;
import it.polimi.ProgettoIngSW2019.view.ViewMaster;

public abstract class UserInputEvent {
    private ViewMaster viewMaster;
    private int idPlayer;

    public void onEvent(IUserInputController inputController){

    }

    public void callRender(){

    }
}
