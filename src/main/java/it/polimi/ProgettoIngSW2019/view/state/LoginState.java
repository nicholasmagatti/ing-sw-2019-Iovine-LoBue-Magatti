package it.polimi.ProgettoIngSW2019.view.state;

import it.polimi.ProgettoIngSW2019.common.message.ConnectionMessage;
import it.polimi.ProgettoIngSW2019.utilities.Observable;
import it.polimi.ProgettoIngSW2019.utilities.Observer;

public class LoginState extends Observable<ConnectionMessage> implements IState, Observer<Boolean>{
    @Override
    public void menu(){

    }

    @Override
    public void update(Boolean isFirstUser) {

    }
}
