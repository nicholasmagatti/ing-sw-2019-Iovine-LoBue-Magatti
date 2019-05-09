package it.polimi.ProgettoIngSW2019.virtual_view;

import it.polimi.ProgettoIngSW2019.common.message.IUserInputMsgForClient;

public interface IUserInputVirtualView<T> {
    void sendData(T msg);
    void setMessageForClient(IUserInputMsgForClient msg);
    IUserInputMsgForClient getMessageForClient();
    void setMessage(T msg);
}
