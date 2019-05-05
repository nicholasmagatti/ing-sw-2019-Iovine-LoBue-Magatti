package it.polimi.ProgettoIngSW2019.client_controller;

import it.polimi.ProgettoIngSW2019.server_controller.IUserInputController;

import java.util.List;

public class ShootEvent extends UserInputEvent{

    //TODO: decide if it is better to use this attribute or to delete it
    private List<Integer> idTargetPlayers;

    @Override
    public void onEvent(IUserInputController inputController) {

    }
}
