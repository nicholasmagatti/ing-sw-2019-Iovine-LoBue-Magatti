package it.polimi.ProgettoIngSW2019.view;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.Observable;
import it.polimi.ProgettoIngSW2019.common.utilities.Observer;

public abstract class State extends Observable<Event> implements Observer<Event> {
    abstract void startState();

    /**
     * Notify the controller of an event.
     * @param message - object of one of the classes specifically made for sending messages
     * @param eventType
     */
    protected void notifyEvent(Object message, EventType eventType){
        String jsonMsg = new Gson().toJson(message);
        Event eventToSend = new Event(eventType, jsonMsg);
        notify(eventToSend);
    }

}
