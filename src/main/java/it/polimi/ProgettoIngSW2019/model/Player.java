package it.polimi.ProgettoIngSW2019.model;

import java.util.ArrayList;

//written by Nicholas Magatti
public class Player {
    private GameTable gameTable;
    private String charaName;
    private ArrayList<HitPoint> damageLine;
    //every mark is represented by a string that
    //is the name of the palyer who marked this player
    private ArrayList<String> markLine;
    //skulls = times this palyer has been killed
    //necessary for counting scores after he/she get killed
    //IMPORTANT: 'skulls' doesn't increment right after the player has
    //been killed, but after the score update
    private int skulls;
    private Square square;
    private int score;
    private ArrayList<WeaponCard> weapons;
    private ArrayList<PowerUp> powerUps;
    private boolean startingPlayer;
    //not sure I can pass an Enum in an ArrayList
    private ArrayList<AmmoType> ammoBox;
    //alternative: 3 int elements:
    //each one represent the number of ammotype contanined in
    //the ammoBox, in this order: red, blue, yellow
    //private ArrayList<int> ammoBox;


    public GameTable getGameTable(){

    }

    public int getSkullNr(){

    }

    public ArrayList<HitPoint> getDamageLine(){

    }

    public void respawn(){

    }

    public boolean isStartingPlayer(){

    }

    public ArrayList<AmmoType> getAmmoBox(){

    }

    public void move(){

    }

    public void grabAmmo(){

    }

    public void grabWeapon(){

    }

    public void shoot(){

    }

    public void reload(){

    }


}
