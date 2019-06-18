package it.polimi.ProgettoIngSW2019.virtual_view;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.utilities.IClientMessageReceiver;
import it.polimi.ProgettoIngSW2019.common.utilities.Observable;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

/**
 * Class VirtualView
 *
 * @author: Luca Iovine
 */
public class VirtualView extends Observable<Event> implements IVirtualView {
    private HashMap<String, IClientMessageReceiver<Event>> clientMessageReceiver;

    public VirtualView() {
        super();
        clientMessageReceiver = new HashMap<>();
    }

    /**
     * Add into a map the object "ClientMessageReceiver" exported from client to server.
     * This way the server can communicate data to client through it.
     *
     * @param clientMessageReceiver is the object "ClientMessageReceiver" received from the client
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    @Override
    public void registerMessageReceiver(String hostname, IClientMessageReceiver<Event> clientMessageReceiver) {
        this.clientMessageReceiver.put(hostname, clientMessageReceiver);
    }

    /**
     * Remove from the map the object "ClientMessageReceiver" exported from client to server.
     * @param hostname client from where the request comes
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    @Override
    public void deregisterMessageReceiver(String hostname){
        clientMessageReceiver.remove(hostname);
    }

    /**
     * Forward the event to the controllers that observes it.
     *
     * @param event contain data coming from client
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    @Override
    public void forwardEvent(Event event) {
        notify(event);
    }

    /**
     * @deprecated
     */
    public void sendMessage(Event event, List<Integer> idPlayers) {}

    /**
     * It allow to send the data generated from server to client knowing only the hostname
     * of the client
     *
     * @param event contain data coming rom server
     * @param hostname of the client to send data
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    public void sendMessage(Event event, String hostname) {
        try {
            clientMessageReceiver.get(hostname).send(event);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }
}