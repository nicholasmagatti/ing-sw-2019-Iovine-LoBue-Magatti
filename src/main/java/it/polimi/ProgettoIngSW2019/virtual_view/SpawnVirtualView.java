package it.polimi.ProgettoIngSW2019.virtual_view;

import it.polimi.ProgettoIngSW2019.common.message.IUserInputMsgForClient;
import it.polimi.ProgettoIngSW2019.common.message.SpawnMessage;
import it.polimi.ProgettoIngSW2019.utilities.Observable;

public class SpawnVirtualView extends Observable<SpawnMessage> implements IUserInputVirtualView<SpawnMessage>{

    @Override
    public void sendData(SpawnMessage msg) {
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
    public void setMessage(SpawnMessage msg) {

    }
}
