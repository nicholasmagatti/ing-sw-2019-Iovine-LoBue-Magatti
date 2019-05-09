package it.polimi.ProgettoIngSW2019.virtual_view;

import it.polimi.ProgettoIngSW2019.common.message.IUserInputMsgForClient;
import it.polimi.ProgettoIngSW2019.common.message.shootmsg.ShootMessage;
import it.polimi.ProgettoIngSW2019.utilities.Observable;

public class ShootVirtualView extends Observable<ShootMessage> implements IUserInputVirtualView<ShootMessage> {
    IUserInputMsgForClient msgForClient;

    @Override
    public void sendData(ShootMessage msg) {
        notify(msg);
    }

    @Override
    public void setMessageForClient(IUserInputMsgForClient msgManager){
        msgForClient = msgManager;
    }

    @Override
    public IUserInputMsgForClient getMessageForClient() {
        return msgForClient;
    }

    @Override
    public void setMessage(ShootMessage msg) {
        msgForClient.setMessage(msg);
    }
}
