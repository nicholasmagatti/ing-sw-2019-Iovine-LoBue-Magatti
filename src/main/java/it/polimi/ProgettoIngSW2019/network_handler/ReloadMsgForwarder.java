package it.polimi.ProgettoIngSW2019.network_handler;

import it.polimi.ProgettoIngSW2019.common.message.UserInputMessage;
import it.polimi.ProgettoIngSW2019.virtual_view.IUserInputVirtualView;

public class ReloadMsgForwarder extends InputMessageForwarder {

    public ReloadMsgForwarder(IUserInputVirtualView userInputVirtualView) {
        super(userInputVirtualView);
    }

    @Override
    public void update(UserInputMessage message) {

    }
}
