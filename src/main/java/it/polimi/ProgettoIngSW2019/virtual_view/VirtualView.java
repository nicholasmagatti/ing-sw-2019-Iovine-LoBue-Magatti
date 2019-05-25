package it.polimi.ProgettoIngSW2019.virtual_view;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.utilities.IClientMessageReceiver;
import it.polimi.ProgettoIngSW2019.common.utilities.Observable;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class VirtualView
 *
 * @author: Luca Iovine
 */
public class VirtualView extends Observable<Event> implements IVirtualView {
    private List<IClientMessageReceiver<Event>> clientMessageReceiver = new ArrayList<>();

    public VirtualView() {
        super();
    }

    /**
     * Add into a list the object "ClientMessageReceiver" exported from client to server.
     * This way the server can communicate data to client through it.
     *
     * @param clientMessageReceiver is the object "ClientMessageReceiver" received from the client
     * @author: Luca Iovine
     */
    @Override
    public void registerMessageReceiver(IClientMessageReceiver<Event> clientMessageReceiver) {
        this.clientMessageReceiver.add(clientMessageReceiver);
    }

    /**
     * Forward the event to the controllers that observes it.
     *
     * @param event contain data coming from client
     * @author: Luca Iovine
     */
    @Override
    public void forwardEvent(Event event) {
        notify(event);
    }

    /**
     * It allow to send the data generated from server to client.
     *
     * @param event contain data coming rom server
     * @param idPlayers list of player's id which need to be informed
     * @author: Luca Iovine
     */
    public void sendMessage(Event event, List<Integer> idPlayers) {
        try {
            for(int id: idPlayers)
                clientMessageReceiver.get(id).send(event);
            //TODO
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }
}