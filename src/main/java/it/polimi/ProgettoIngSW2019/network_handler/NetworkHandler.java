package it.polimi.ProgettoIngSW2019.network_handler;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.Message.toView.LoginResponse;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.ClientMessageReceiver;
import it.polimi.ProgettoIngSW2019.common.utilities.Observable;
import it.polimi.ProgettoIngSW2019.common.utilities.Observer;
import it.polimi.ProgettoIngSW2019.virtual_view.IVirtualView;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Class NetwrokHandler
 *
 * @author: Luca Iovine
 */
public class NetworkHandler extends Observable<Event> implements Observer<Event> {
    private IVirtualView virtualView;
    private ClientMessageReceiver clientMessageReceiver;

    /**
     * Constructor of the class.
     * Whenever a client is launched, the networked handler register an object "ClientMessageReceiver"
     * on the server. This way the client can get data from it.
     *
     * @param virtualView reference of the remote object exported from the server
     * @author: Luca Iovine
     */
    public NetworkHandler(IVirtualView virtualView) {
        this.virtualView = virtualView;
    }

    /**
     * Forward the event to the server calling the remote object "VirtualView"
     *
     * @param event contains data generated from client
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    private void forwardEvent(Event event){
        try {
            virtualView.forwardEvent(event);
        }catch(RemoteException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Czlled whenever client receive data from the server to be notified to the client.
     * Only exception is for the Login based event, where the network must disassociate server and client
     * before send back the information when the login was not succesfull.
     *
     * @param event contains data generated from server
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    public void sendDataToView(Event event){
        if (event.getCommand().equals(EventType.RESPONSE_NEW_LOGIN) ||
                event.getCommand().equals(EventType.RESPONSE_RECONNECT)) {
            LoginResponse response = (LoginResponse) deserialize(event.getMessageInJsonFormat(), LoginResponse.class);
            if (!response.isLoginSuccessfull()) {
                try {
                    virtualView.deregisterMessageReceiver(InetAddress.getLocalHost().getHostName());
                    UnicastRemoteObject.unexportObject(clientMessageReceiver, true);
                } catch (NoSuchObjectException e) {
                    //TODO: vedere come gestire eccezione
                    e.printStackTrace();
                }catch(RemoteException e) {
                    //TODO: vedere come gestire eccezione
                    e.printStackTrace();
                }catch(UnknownHostException e){
                    //TODO: vedere come gestire eccezione
                    e.printStackTrace();
                }
            }
        }
        notify(event);
    }

    @Override
    public void update(Event event) {
        if(event.getCommand().equals(EventType.REQUEST_LOGIN)) {
            try {
                clientMessageReceiver = new ClientMessageReceiver(this);
                virtualView.registerMessageReceiver(InetAddress.getLocalHost().getHostName(),clientMessageReceiver);
            }catch(RemoteException e){
                //TODO: vedere come gestire eccezione
                e.printStackTrace();
            }catch(UnknownHostException e){
                //TODO: vedere come gestire eccezione
                e.printStackTrace();
            }
        }
        forwardEvent(event);
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