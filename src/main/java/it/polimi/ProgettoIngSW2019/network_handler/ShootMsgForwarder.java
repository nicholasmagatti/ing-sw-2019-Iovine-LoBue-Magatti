package it.polimi.ProgettoIngSW2019.network_handler;

import it.polimi.ProgettoIngSW2019.common.message.UserInputMessage;
import it.polimi.ProgettoIngSW2019.common.message.IUserInputMsgForClient;
import it.polimi.ProgettoIngSW2019.virtual_view.IUserInputVirtualView;

import java.rmi.RemoteException;

public class ShootMsgForwarder extends InputMessageForwarder {
    private IUserInputMsgForClient shootMsgForClient;

    public ShootMsgForwarder(IUserInputVirtualView userInputVirtualView, IUserInputMsgForClient shootMsgForClient) {
        super(userInputVirtualView);
        this.shootMsgForClient = shootMsgForClient;
        //Set the contain to retrive data from server by the client
        userInputVirtualView.setMessageForClient(shootMsgForClient);
    }

    @Override
    public void update(UserInputMessage message) {
        userInputVirtualView.sendData(message);
    }
}
