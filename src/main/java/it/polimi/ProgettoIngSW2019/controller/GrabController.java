package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.common.message.GrabMessage;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.utilities.Observer;


public class GrabController implements Observer<GrabMessage> {
    private Player playerWhoGrab;
    private Square currentPos;


    public void update(GrabMessage grabMessage) {

    }


    public void grab() {

    }
}
