package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.common.utilities.Observer;
import it.polimi.ProgettoIngSW2019.model.Session;
import it.polimi.ProgettoIngSW2019.common.Event;

public class ConnectionController implements Observer<Event> {
    private Session session;
    private String ip;
    private String username;

    public void update(Event connectionMessage) {
    }


    public void registerClient(String ip) {
        session.registerClient(ip);
    }

    public void insertPlayer(String username){
        session.createPlayer(username);
    }
}
