package it.polimi.ProgettoIngSW2019.network_handler;

import it.polimi.ProgettoIngSW2019.common.message.UserInputMessage;
import it.polimi.ProgettoIngSW2019.utilities.Observable;
import it.polimi.ProgettoIngSW2019.utilities.Observer;
import it.polimi.ProgettoIngSW2019.virtual_view.IUserInputVirtualView;

public abstract class InputMessageForwarder extends Observable implements Observer<UserInputMessage> {
    IUserInputVirtualView userInputVirtualView;

    public InputMessageForwarder(IUserInputVirtualView userInputVirtualView){
        this.userInputVirtualView = userInputVirtualView;
    }
}
