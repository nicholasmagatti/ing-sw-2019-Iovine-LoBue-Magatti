package it.polimi.ProgettoIngSW2019.common.utilities;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.network_handler.NetworkHandler;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientMessageReceiver extends UnicastRemoteObject implements IClientMessageReceiver<Event> {
    NetworkHandler networkHandler;

    public ClientMessageReceiver(NetworkHandler networkHandler) throws RemoteException {
        super();
        this.networkHandler = networkHandler;
    }

    @Override
    public void send(Event event) {
        networkHandler.sendDataToView(event);
    }
}
