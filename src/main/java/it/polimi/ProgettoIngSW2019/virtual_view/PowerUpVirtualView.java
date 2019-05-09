package it.polimi.ProgettoIngSW2019.virtual_view;

import it.polimi.ProgettoIngSW2019.common.message.IUserInputMsgForClient;
import it.polimi.ProgettoIngSW2019.common.message.PowerUpMessage;
import it.polimi.ProgettoIngSW2019.utilities.Observable;

public class PowerUpVirtualView extends Observable<PowerUpMessage> implements IUserInputVirtualView<PowerUpMessage> {

    @Override
    public void sendData(PowerUpMessage msg) {

    }

    @Override
    public void setMessageForClient(IUserInputMsgForClient msg) {

    }

    @Override
    public IUserInputMsgForClient getMessageForClient() {
        return null;
    }

    @Override
    public void setMessage(PowerUpMessage msg) {

    }

}
