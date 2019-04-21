package it.polimi.ProgettoIngSW2019.model;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ProgettoIngSW2019.model.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.model.enums.Directions;
import it.polimi.ProgettoIngSW2019.utilities.Observable;

public class Player extends Observable <String>{

    private int idPlayer;
    private GameTable gameTable;
    private String charaName;
    private ArrayList<KillToken> damageLine;
    //every mark is represented by a string that
    //is the name of the palyer who marked this player
    private ArrayList<String> markLine;
    //NumberOfkulls = times this palyer has been killed
    //necessary for counting scores after he/she gets killed
    //IMPORTANT: 'numberOfSkulls' doesn't increment right after the player has
    //been killed, but after the score updates
    private int numberOfSkulls;
    private boolean playerDown;
    private Square square;
    private int score;
    private ArrayList<WeaponCard> loadedWeapons;
    private ArrayList<WeaponCard> unloadedWeapons;
    private ArrayList<PowerUp> powerUps;
    private boolean startingPlayer;
    private ArrayList<AmmoType> ammoBox;



    public GameTable getGameTable(){
        return gameTable;
    }

    public int getNumberOfSkulls() {
        return numberOfSkulls;
    }

    public List<KillToken> getDamageLine(){
        return damageLine;
    }

    public void respawn(){

    }

    public boolean isStartingPlayer(){
        return startingPlayer;
    }

    public void dealDamage(int nrDamage, Player targetPlayer){

    }

    public void markPlayer(int nrMark, Player targetPlayer){

    }

    public void move(List<Directions> movementDirections){

    }

    public boolean isPlayerDown(){
        return playerDown;
    }

    public List<AmmoType> getAmmoBox() {
        return ammoBox;
    }

    public void grabAmmo(){

    }

    public void grabWeapon(){

    }

    public void shoot(){

    }

    public void reload(){

    }

    public void discardAmmo(List<AmmoType> ammoToSpend){

    }


}
