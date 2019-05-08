package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.utilities.Observer;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.common.message.MoveMessage;

public class MoveController implements Observer <MoveMessage> {
    private Player playerToMove;
    private Square newPos;


    public void update(MoveMessage moveMessage) {

    }


    public void move() {

    }
}
