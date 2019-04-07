package it.polimi.ProgettoIngSW2019.model;

import java.util.ArrayList;

import it.polimi.ProgettoIngSW2019.model.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.utilities.Observable;

public class GameTable extends Observable <String>{

    private Deck weaponDeck;
    private Deck powerUpDeck;
    private Deck ammoDeck;
    private ArrayList<PowerUp> powerUpDiscarded;
    private ArrayList<AmmoType> ammoDiscarded;
    private Square[][] map;
    // it'll be Square[4][3]
    
    private ArrayList<HitPoint> skullLine;
    private ArrayList<HitPoint> frenzyKillList;


    //set left/right board A/B are used to create all the possible 4 faces of the two boards
    private void setLeftBoardA(){

    }

    private void setLeftBoardB(){

    }

    private void setRightBoardA(){

    }

    private void setRightBoardB(){

    }

    //set the whole board putting the two boards in the requested way
    //parameters: 1 is side A, 0 is side B
    public void inizializeGameTable(boolean leftBoard, boolean rightBoard){

    }


    public ArrayList<ArrayList<Square>> getMap () {
        //TODO: temporary variable to make Sonar work (it will be deteted)
        ArrayList<ArrayList<Square>> tempVar = new ArrayList<ArrayList<Square>>();
        return tempVar;
    }


    public void setMap () {

    }


    public int calculateFinalScore () {
        //TODO: temporary variable to make Sonar work (it will be deteted)
        int tempVar = 3;
        return tempVar;
    }
}
