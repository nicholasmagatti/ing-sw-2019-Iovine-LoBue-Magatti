package it.polimi.ProgettoIngSW2019.network_handler;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.utilities.ClientMessageReceiver;
import it.polimi.ProgettoIngSW2019.common.utilities.Observable;
import it.polimi.ProgettoIngSW2019.common.utilities.Observer;
import it.polimi.ProgettoIngSW2019.virtual_view.IVirtualView;

import java.rmi.RemoteException;

public class NetworkHandler extends Observable<Event> implements Observer<Event> {
    IVirtualView virtualView;

    public NetworkHandler(IVirtualView virtualView) throws RemoteException {
        this.virtualView = virtualView;
        virtualView.registerMessageReceiver(new ClientMessageReceiver(this));
    }

    public void forwardEvent(Event event){
        try {
            virtualView.forwardEvent(event);
        }catch(RemoteException ex){
            ex.printStackTrace();
        }
    }

    public void sendDataToView(Event data){
        notify(data);
    }

    @Override
    public void update(Event event) {
        forwardEvent(event);
    }
}