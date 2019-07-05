package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Message.toController.LoginRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.LoginResponse;
import it.polimi.ProgettoIngSW2019.common.Message.toView.MessageConnection;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.Observer;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.model.LoginHandler;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;

import java.util.Arrays;
import java.util.List;

/**
 * Manages the login before the setup of the game
 * @author Luca Iovine
 */
public class ConnectionController implements Observer<Event> {
    private LoginHandler loginHandler;
    private VirtualView virtualView;

    /**
     * Constructor
     * @param virtualView
     * @param loginHandler
     */
    public ConnectionController(VirtualView virtualView, LoginHandler loginHandler) {
        this.loginHandler = loginHandler;
        this.virtualView = virtualView;
    }

    /**
     * update to receive the events
     * @param event     event message from view
     */
    //TESTED:
    @Override
    public void update(Event event) {
        LoginResponse loginResponse;
        LoginRequest info = (LoginRequest) deserialize(event.getMessageInJsonFormat(), LoginRequest.class);
        /*
            --> RequestGameIsStartedTrueTest
                RequestGameIsStartedFalseTest
         */
        if(event.getCommand().equals(EventType.REQUEST_GAME_IS_STARTED)){
            if(loginHandler.isGameStarted())
                loginResponse = new LoginResponse(true);
            else
                loginResponse = new LoginResponse(false);

            virtualView.sendMessage(new Event(EventType.RESPONSE_GAME_IS_STARTED, serialize(loginResponse)), Arrays.asList(info.getHostname()));
        }

        /*
            --> RequestLoginBeforeStartValidUsernameTest
                RequestLoginBeforeStartNOTValidUsernameTest
                RequestLoginAfterStartValidCredentialTest
                RequestLoginAfterStartNOTValidCredentialTest
                RequestLoginAfterStartCapReachedTest
         */
        if(event.getCommand().equals(EventType.REQUEST_LOGIN)){
            if(loginHandler.getNrOfPlayerConnected() < 5) {
                if (!loginHandler.isGameStarted()) {
                    if (loginHandler.checkUserExist(info.getUsername())) {
                        /*
                            Non c'è bisogno di controllare che sia loggato nel caso la partita non sia ancora
                            iniziata, perché nel momento in cui si disconnette (lo sapremo grazie al ping da
                            parte del server) verrà dissociato come utente giocante e, di conseguenza, il nome
                            ritornerà ad essere libero.
                         */
                        loginResponse = new LoginResponse(false);
                    } else {
                        loginHandler.generateNewLogin(info.getUsername(), info.getpassword(), info.getHostname());
                        loginResponse = new LoginResponse(true);
                    }
                    virtualView.sendMessage(new Event(EventType.RESPONSE_NEW_LOGIN, serialize(loginResponse)), Arrays.asList(info.getHostname()));
                } else {
                    if (loginHandler.checkLoginValidity(info.getUsername(), info.getpassword(), info.getHostname()))
                        loginResponse = new LoginResponse(true);
                    else
                        loginResponse = new LoginResponse(false);

                    virtualView.sendMessage(new Event(EventType.RESPONSE_RECONNECT, serialize(loginResponse)), Arrays.asList(info.getHostname()));
                }
            }
            else{
                MessageConnection capPlayer = new MessageConnection(info.getUsername(), info.getHostname());
                virtualView.sendMessage(new Event(EventType.CAP_REACHED, serialize(capPlayer)), Arrays.asList(info.getHostname()));
            }
        }

        /*
            --> notAliveBeforeStartTest
                notAliveAfterStartTest
         */
        if(event.getCommand().equals(EventType.NOT_ALIVE)){
            MessageConnection msg = (MessageConnection) deserialize(event.getMessageInJsonFormat(), MessageConnection.class);
            MessageConnection disconnectedPlayer = new MessageConnection(msg.getUsername(), "");
            virtualView.sendMessage(new Event(EventType.USER_HAS_DISCONNECTED, serialize(disconnectedPlayer)), loginHandler.getActiveUsersHostname());
            loginHandler.disconnectPlayer(msg.getHostname());
        }

        // --> NOT TO BE TESTED
        if(event.getCommand().equals(EventType.START_ACTION_TIMER))
            loginHandler.startActionTimer();

        // --> NOT TO BE TESTED
        if(event.getCommand().equals(EventType.STOP_ACTION_TIMER))
            loginHandler.stopActionTimer();
    }

    /**
     * To serialize information of the event
     *
     * @param objToSerialize object that needs to be serialized
     * @return object serialized
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    private String serialize(Object objToSerialize){
        Gson gsonReader = new Gson();
        String serializedObj = gsonReader.toJson(objToSerialize, objToSerialize.getClass());

        return serializedObj;
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
