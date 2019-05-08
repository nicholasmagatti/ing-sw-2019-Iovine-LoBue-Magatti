package it.polimi.ProgettoIngSW2019.view.state;

import it.polimi.ProgettoIngSW2019.common.message.*;
import it.polimi.ProgettoIngSW2019.utilities.Observable;

public class SpawnState extends Observable<UserInputMessage> implements IState{

    @Override
    public void menu() {

    }

    private String showPowerUpInfo(int idPowerUp){
        return "";
    }
}
