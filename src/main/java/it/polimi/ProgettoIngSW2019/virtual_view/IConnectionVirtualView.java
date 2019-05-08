package it.polimi.ProgettoIngSW2019.virtual_view;

import it.polimi.ProgettoIngSW2019.common.message.ConnectionMessage;

public interface IConnectionVirtualView {
    void sendData(ConnectionMessage msg);
    boolean retriveData();
}
