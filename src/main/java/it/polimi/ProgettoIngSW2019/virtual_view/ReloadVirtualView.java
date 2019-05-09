package it.polimi.ProgettoIngSW2019.virtual_view;

import it.polimi.ProgettoIngSW2019.common.message.IUserInputMsgForClient;
import it.polimi.ProgettoIngSW2019.common.message.reloadmsg.ReloadMessage;
import it.polimi.ProgettoIngSW2019.utilities.Observable;

public class ReloadVirtualView extends Observable<ReloadMessage> implements IUserInputVirtualView<ReloadMessage> {

    @Override
    public void sendData(ReloadMessage msg) {
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
    public void setMessage(ReloadMessage msg) {

    }
}
