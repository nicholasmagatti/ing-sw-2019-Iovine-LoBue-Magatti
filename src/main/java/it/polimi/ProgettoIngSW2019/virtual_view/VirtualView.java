package it.polimi.ProgettoIngSW2019.virtual_view;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.utilities.IClientMessageReceiver;
import it.polimi.ProgettoIngSW2019.common.utilities.Observable;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class VirtualView extends Observable<Event> implements IVirtualView {
    private List<IClientMessageReceiver<Event>> clientMessageReceiver = new ArrayList<>();

    public VirtualView() {
        super();
    }

    @Override
    public void registerMessageReceiver(IClientMessageReceiver<Event> clientMessageReceiver) {
        this.clientMessageReceiver.add(clientMessageReceiver);
    }

    @Override
    public void forwardEvent(Event event) {
        notify(event);
    }

    public void sendMessage(Event event) {
        try {
            clientMessageReceiver.get(0).send(event);
            //TODO
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }
}