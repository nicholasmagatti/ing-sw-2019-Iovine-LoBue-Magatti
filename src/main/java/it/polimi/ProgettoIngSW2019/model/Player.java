package it.polimi.ProgettoIngSW2019.model;

import java.util.ArrayList;

import it.polimi.ProgettoIngSW2019.model.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.utilities.Observable;

public class Player extends Observable <String>{

    private int idPlayer;
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
    private ArrayList<AmmoType> ammoBox;



    public GameTable getGameTable(){
        //TODO: temporary variable to make Sonar work (it will be deteted)
        GameTable temporaryVariable = new GameTable();
        return temporaryVariable;
    }

    public int getSkullNr(){
        //TODO: temporary variable to make Sonar work (it will be deteted)
        int temporaryVariable = 1;
        return temporaryVariable;
    }

    public ArrayList<HitPoint> getDamageLine(){
        //TODO: temporary variable to make Sonar work (it will be deteted)
        ArrayList<HitPoint> temporaryVariable = new ArrayList<HitPoint>();
        return temporaryVariable;
    }

    public void respawn(){

    }

    public boolean isStartingPlayer(){
        //TODO: temporary variable to make Sonar work (it will be deteted)
        boolean temporaryVariable = true;
        return temporaryVariable;
    }

    public ArrayList<AmmoType> getAmmoBox() {
        //TODO: temporary variable to make Sonar work (it will be deteted)
        ArrayList<AmmoType> temporaryVariable = new ArrayList<AmmoType>();
        return temporaryVariable;
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
