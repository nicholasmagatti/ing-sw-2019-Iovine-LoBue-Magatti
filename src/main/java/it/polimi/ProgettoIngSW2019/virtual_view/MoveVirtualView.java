package it.polimi.ProgettoIngSW2019.virtual_view;

import it.polimi.ProgettoIngSW2019.common.message.IUserInputMsgForClient;
import it.polimi.ProgettoIngSW2019.common.message.MoveMessage;
import it.polimi.ProgettoIngSW2019.utilities.Observable;

public class MoveVirtualView extends Observable<MoveMessage> implements IUserInputVirtualView<MoveMessage> {

    @Override
    public void sendData(MoveMessage msg) {
        notify(msg);
    }

    @Override
    public void setMessage(MoveMessage msg) {

    }

    @Override
    public IUserInputMsgForClient getMessageForClient() {
        return null;
    }

    @Override
    public void setMessageForClient(IUserInputMsgForClient msg) {

    }
}
