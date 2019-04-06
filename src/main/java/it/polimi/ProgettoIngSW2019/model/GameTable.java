package it.polimi.ProgettoIngSW2019.model;

import java.util.ArrayList;
import it.polimi.ProgettoIngSW2019.utilities.Observable;

public class GameTable extends Observable <String>{

    private Deck weaponDeck;
    private Deck powerUpDeck;
    private Deck ammoDeck;
    private ArrayList<PowerUp> powerUpDiscarded;
    private ArrayList<AmmoType> ammoDiscarded;
    private Square[][] map;
    // dovrà essere Square[4][3]
    
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

    }


    public void setMap () {

    }


    public int calculateFinalScore () {

    }
}
