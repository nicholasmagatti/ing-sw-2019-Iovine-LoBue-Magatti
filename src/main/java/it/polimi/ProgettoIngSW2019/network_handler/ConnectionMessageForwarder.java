package it.polimi.ProgettoIngSW2019.network_handler;

import it.polimi.ProgettoIngSW2019.common.message.ConnectionMessage;
import it.polimi.ProgettoIngSW2019.utilities.Observable;
import it.polimi.ProgettoIngSW2019.utilities.Observer;
import it.polimi.ProgettoIngSW2019.virtual_view.IConnectionVirtualView;

public class ConnectionMessageForwarder extends Observable<Boolean> implements Observer<ConnectionMessage> {
    IConnectionVirtualView connectionVirtualView;

    public ConnectionMessageForwarder(IConnectionVirtualView connectionVirtualView){
        this.connectionVirtualView = connectionVirtualView;
    }

    @Override
    public void update(ConnectionMessage message) {
        connectionVirtualView.sendData(message);
    }
}
