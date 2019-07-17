package it.polimi.ProgettoIngSW2019.common.utilities;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.network_handler.NetworkHandler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Class that handles the receiving process of the client
 */
public class ClientMessageReceiver extends UnicastRemoteObject implements IClientMessageReceiver<Event> {
    NetworkHandler networkHandler;

    /**
     * Constructor
     * @param networkHandler
     * @throws RemoteException
     */
    public ClientMessageReceiver(NetworkHandler networkHandler) throws RemoteException {
        super();
        this.networkHandler = networkHandler;
    }

    /**
     * Send data to view
     * @param event
     */
    @Override
    public void send(Event event) {
        networkHandler.sendDataToView(event);
    }
}
