package it.polimi.ProgettoIngSW2019.network_handler;

import it.polimi.ProgettoIngSW2019.common.message.UserInputMessage;
import it.polimi.ProgettoIngSW2019.virtual_view.IUserInputVirtualView;

public class GrabMsgForwarder extends InputMessageForwarder {

    public GrabMsgForwarder(IUserInputVirtualView userInputVirtualView) {
        super(userInputVirtualView);
    }

    @Override
    public void update(UserInputMessage message) {
        userInputVirtualView.sendData(message);
    }
}
