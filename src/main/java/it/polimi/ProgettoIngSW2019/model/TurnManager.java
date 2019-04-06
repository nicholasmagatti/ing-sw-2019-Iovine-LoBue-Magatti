package it.polimi.ProgettoIngSW2019.model;

import java.util.ArrayList;
import it.polimi.ProgettoIngSW2019.utilities.Observable;


public class TurnManager extends Observable <String>{

    private ArrayList<Player> players;
    private GameTable gameTable;
    private Player currentPlayer;

    public void setOrder(){

    }

    //is skull line full of damage tokens?
    //in other words: have all the skull tokens been removed
    //from the skull line?
    public boolean isSkullLineFull(){

    }

    public void changePlayer(){

    }

    //a single action of the player
    //a reload DOESN'T count as an action!
    public void action(){

    }

    //the whole turn of a player:
    //composed by actions (+ optional reload at the end)
    public void turn(){

    }

    public void updateScore(Player player){

    }

    //at the end of a turn, check if any player has just died,
    //in that case: use updateScore and respawn those palyers
    public void checkDeadPlayer(){

    }

}
