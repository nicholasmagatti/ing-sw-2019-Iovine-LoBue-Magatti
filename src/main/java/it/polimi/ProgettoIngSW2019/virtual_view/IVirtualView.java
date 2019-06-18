package it.polimi.ProgettoIngSW2019.virtual_view;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.utilities.IClientMessageReceiver;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IVirtualView extends Remote {
    void registerMessageReceiver(String hostname, IClientMessageReceiver<Event> IClientMessageReceiver) throws RemoteException;
    void deregisterMessageReceiver(String hostname) throws RemoteException;
    void forwardEvent(Event event) throws RemoteException;
}
