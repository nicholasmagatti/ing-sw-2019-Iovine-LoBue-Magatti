package it.polimi.ProgettoIngSW2019.network_handler;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.utilities.ClientMessageReceiver;
import it.polimi.ProgettoIngSW2019.common.utilities.Observable;
import it.polimi.ProgettoIngSW2019.common.utilities.Observer;
import it.polimi.ProgettoIngSW2019.virtual_view.IVirtualView;

import java.rmi.RemoteException;

/**
 * Class NetwrokHandler
 *
 * @author: Luca Iovine
 */
public class NetworkHandler extends Observable<Event> implements Observer<Event> {
    IVirtualView virtualView;

    /**
     * Constructor of the class.
     * Whenever a client is launched, the networked handler register an object "ClientMessageReceiver"
     * on the server. This way the client can get data from it.
     *
     * @param virtualView reference of the remote object exported from the server
     * @throws RemoteException
     * @author: Luca Iovine
     */
    public NetworkHandler(IVirtualView virtualView) throws RemoteException {
        this.virtualView = virtualView;
        virtualView.registerMessageReceiver(new ClientMessageReceiver(this));
    }

    /**
     * Forward the event to the server calling the remote object "VirtualView"
     *
     * @param event contains data generated from client
     * @author: Luca Iovine
     */
    public void forwardEvent(Event event){
        try {
            virtualView.forwardEvent(event);
        }catch(RemoteException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Czlled whenever client receive data from the server to be notified to the client.
     *
     * @param event contains data generated from server
     * @author: Luca Iovine
     */
    public void sendDataToView(Event event){
        notify(event);
    }

    @Override
    public void update(Event event) {
        forwardEvent(event);
    }
}