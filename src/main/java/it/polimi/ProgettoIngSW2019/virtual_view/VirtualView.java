package it.polimi.ProgettoIngSW2019.virtual_view;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.Message.toView.MessageConnection;
import it.polimi.ProgettoIngSW2019.common.Message.toView.SetupInfo;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.IClientMessageReceiver;
import it.polimi.ProgettoIngSW2019.common.utilities.Observable;
import it.polimi.ProgettoIngSW2019.common.utilities.Observer;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

/**
 * Class VirtualView
 *
 * @author: Luca Iovine
 */
public class VirtualView extends Observable<Event> implements IVirtualView, Observer<Event> {
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
    public synchronized void registerMessageReceiver(String hostname, IClientMessageReceiver<Event> clientMessageReceiver) {
        this.clientMessageReceiver.put(hostname, clientMessageReceiver);
        notifyAll();
    }

    /**
     * Remove from the map the object "ClientMessageReceiver" exported from client to server.
     * @param hostname client from where the request comes
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    @Override
    public synchronized void deregisterMessageReceiver(String hostname){
        clientMessageReceiver.remove(hostname);
        notifyAll();
    }

    /**
     * Forward the event to the controllers that observes it.
     *
     * @param event contain data coming from client
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    @Override
    public synchronized void forwardEvent(Event event) {
        notify(event);
        notifyAll();
    }

    /**
     * It allow to send the data generated from server to client knowing only the hostname
     * of the client
     *
     * @param event contain data coming rom server
     * @param hostnameList list of the client to send data
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    public synchronized void sendMessage(Event event, List<String> hostnameList) {
        try {
            for(String hostname: hostnameList)
                clientMessageReceiver.get(hostname).send(event);
        } catch (RemoteException ex) {
            //TODO: non riesce a contattare il client e lo disconnette
            ex.printStackTrace();
        }finally{
            notifyAll();
        }
    }

    //NOT TO BE TESTED
    @Override
    public void update(Event event) {
        MessageConnection msg = (MessageConnection) deserialize(event.getMessageInJsonFormat(), MessageConnection.class);
        if(event.getCommand().equals(EventType.CHECK_IS_ALIVE)) {
            try {
                clientMessageReceiver.get(msg.getHostname()).send(event);
            }catch(RemoteException e){
                notify(new Event(EventType.NOT_ALIVE, event.getMessageInJsonFormat()));
            }
        }
        if(event.getCommand().equals(EventType.INPUT_TIME_EXPIRED)){
            try {
                clientMessageReceiver.get(msg.getHostname()).send(event);
                notify(new Event(EventType.NOT_ALIVE, event.getMessageInJsonFormat()));
            }catch(RemoteException e){
                notify(new Event(EventType.NOT_ALIVE, event.getMessageInJsonFormat()));
            }
        }
        if(event.getCommand().equals((EventType.GO_IN_GAME_SETUP))){
            SetupInfo setupInfo = (SetupInfo) deserialize(event.getMessageInJsonFormat(), SetupInfo.class);
            try {
                clientMessageReceiver.get(setupInfo.getHostname()).send(event);
            }catch(RemoteException e){
                notify(new Event(EventType.NOT_ALIVE, event.getMessageInJsonFormat()));
            }
        }
    }

    /**
     * To deserialize information in the event
     *
     * @param json string that contains data in json format
     * @param cls class to deserialize
     * @return object deserialized
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    private Object deserialize(String json, Class<?> cls){
        Gson gsonReader = new Gson();
        Object deserializedObj = gsonReader.fromJson(json, cls);

        return deserializedObj;
    }
}