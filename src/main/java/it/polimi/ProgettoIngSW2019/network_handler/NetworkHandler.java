package it.polimi.ProgettoIngSW2019.network_handler;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.Message.toController.LoginRequest;
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
    String hostname;

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
        try {
            clientMessageReceiver = new ClientMessageReceiver(this);
        }catch(RemoteException e){
            System.out.println();
            System.out.println(virtualView);
            //e.printStackTrace();
            System.exit(-1);
        }
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
            System.out.println("Cannot reach the server.\n" +
                    "You have been disconnected");
            ex.printStackTrace();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //System.exit(-1);
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
        notify(event);
    }

    //NOT TO BE TESTED
    @Override
    public void update(Event event) {
        if(event.getCommand().equals(EventType.REQUEST_GAME_IS_STARTED)) {
            try {
                hostname =  (new Gson().fromJson(event.getMessageInJsonFormat(), LoginRequest.class)).getHostname();
                virtualView.registerMessageReceiver(hostname, clientMessageReceiver);
            }catch(RemoteException e) {
                System.out.println("Cannot reach the server.\n" +
                        "You have been disconnected");
                e.printStackTrace();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    e.printStackTrace();
                }
                //System.exit(-1);
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