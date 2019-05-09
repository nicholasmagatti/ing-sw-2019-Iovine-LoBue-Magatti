package it.polimi.ProgettoIngSW2019.virtual_view;

import it.polimi.ProgettoIngSW2019.common.message.GrabMessage;
import it.polimi.ProgettoIngSW2019.common.message.IUserInputMsgForClient;
import it.polimi.ProgettoIngSW2019.utilities.Observable;

public class GrabVirtualView extends Observable<GrabMessage> implements IUserInputVirtualView <GrabMessage> {

    @Override
    public void sendData(GrabMessage msg){
        notify(msg);
    }

    @Override
    public void setMessageForClient(IUserInputMsgForClient msg) {

    }

    @Override
    public IUserInputMsgForClient getMessageForClient() {
        return null;
    }

    @Override
    public void setMessage(GrabMessage msg) {

    }

}
