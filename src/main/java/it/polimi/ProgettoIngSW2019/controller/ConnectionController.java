package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.model.Session;
import it.polimi.ProgettoIngSW2019.common.message.ConnectionMessage;
import it.polimi.ProgettoIngSW2019.utilities.Observer;

public class ConnectionController implements Observer<ConnectionMessage> {
    private Session session;
    private String ip;
    private String username;

    public void update(ConnectionMessage connectionMessage) {
        ip = connectionMessage.getIp();
        username = connectionMessage.getPlayerName();
        registerClient(ip);
        insertPlayer(username);
    }


    public void registerClient(String ip) {
        session.registerClient(ip);
    }

    public void insertPlayer(String username){
        session.createPlayer(username);
    }
}
