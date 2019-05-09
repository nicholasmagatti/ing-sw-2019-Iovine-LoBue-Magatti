package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.model.GameTable;
import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.model.TurnManager;
import it.polimi.ProgettoIngSW2019.utilities.Observer;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.common.message.MoveMessage;

/**
 * @author Nicholas Magatti
 */
public class MoveController implements Observer <MoveMessage> {
    private Player playerToMove;
    private Square newPos;
    //TODO: keep or delete?
    private GameTable gameTable;
   //TODO: keep or delete?
    private TurnManager turnManager;

    //TODO: keep or delete?
    public MoveController(GameTable gameTable, TurnManager turnManager){
        this.gameTable = gameTable;
        this.turnManager = turnManager;
    }


    public void update(MoveMessage moveMessage) {

    }


    public void move() {
        playerToMove.moveTo(newPos);
    }


}
