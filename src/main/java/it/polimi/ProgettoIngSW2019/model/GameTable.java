package it.polimi.ProgettoIngSW2019.model;

import java.util.ArrayList;

//written by Nicholas Magatti
public class GameTable {

    private Deck weaponDeck;
    private Deck powerUpDeck;
    private Deck ammoDeck;
    private ArrayList<PowerUp> powerUpDiscarded;
    private ArrayList<Ammo> ammoDiscarded;
    private ArrayList<ArrayList<Square>> map;
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
}
