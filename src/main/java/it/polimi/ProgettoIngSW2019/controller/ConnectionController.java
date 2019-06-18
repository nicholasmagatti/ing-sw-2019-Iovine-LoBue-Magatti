package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Message.toController.LoginRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.LoginResponse;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.Observer;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.model.LoginHandler;
import it.polimi.ProgettoIngSW2019.model.TurnManager;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;

public class ConnectionController extends Controller implements Observer<Event> {
    private LoginHandler loginHandler;

    public ConnectionController(TurnManager turnManager, VirtualView virtualView, IdConverter idConverter, CreateJson createJson, IdPlayersCreateList idPlayersCreateList, LoginHandler loginHandler) {
        super(turnManager, virtualView, idConverter, createJson, idPlayersCreateList);
        this.loginHandler = loginHandler;
    }


    @Override
    public void update(Event event) {
        LoginResponse loginResponse;

        if(event.getCommand().equals(EventType.REQUEST_LOGIN)){
            LoginRequest info = (LoginRequest) deserialize(event.getMessageInJsonFormat(), LoginRequest.class);

            if(!loginHandler.isGameStarted()) {
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
                getVirtualView().sendMessage(new Event(EventType.RESPONSE_NEW_LOGIN, serialize(loginResponse)), info.getHostname());
            }else{
                if(loginHandler.checkLoginValidity(info.getHostname(), info.getpassword(), info.getHostname()))
                    loginResponse = new LoginResponse(true);
                else
                    loginResponse = new LoginResponse(false);

                getVirtualView().sendMessage(new Event(EventType.RESPONSE_RECONNECT, serialize(loginResponse)), info.getHostname());
            }
        }
    }

    /**
     * To serialize information of the event
     *
     * @param objToSerialize object that needs to be serialized
     * @return object serialized
     * @author: Luca Iovine
     */
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
    private Object deserialize(String json, Class<?> cls){
        Gson gsonReader = new Gson();
        Object deserializedObj = gsonReader.fromJson(json, cls);

        return deserializedObj;
    }
}
