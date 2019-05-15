package it.polimi.ProgettoIngSW2019.common.utilities;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClientMessageReceiver<T> extends Remote {
    void send(T message) throws RemoteException;
}
