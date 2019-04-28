package it.polimi.ProgettoIngSW2019.model;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ProgettoIngSW2019.model.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.model.modelView.PlayerView;
import it.polimi.ProgettoIngSW2019.utilities.Observable;

/**
 * Class that represents the player
 * @author Nicholas Magatti
 */
public class Player extends Observable <PlayerView>{

    private int idPlayer;
    private GameTable gameTable;
    private String charaName;
    private List<String> damageLine = new ArrayList<>();
    //every mark is represented by a string that
    //is the name of the palyer who marked this player
    private List<String> markLine = new ArrayList<>();
    //NumberOfkulls = times this player has been killed
    //necessary for counting scores after he/she gets killed
    //IMPORTANT: 'numberOfSkulls' doesn't increment right after the player has
    //been killed, but after the score updates
    private int numberOfSkulls;
    private boolean playerDown;
    private Square square;
    private int score = 0;
    private List<WeaponCard> loadedWeapons = new ArrayList<>();
    private List<WeaponCard> unloadedWeapons = new ArrayList<>();
    private List<PowerUp> powerUps = new ArrayList<>();
    private boolean startingPlayer;
    private AmmoBox ammoBox = new AmmoBox();
    private boolean active = true;

    /**
     * Get the game table
     * @return the game table
     */
    public GameTable getGameTable(){
        return gameTable;
    }

    /**
     * Get the id of the player
     * @return id of the player
     */
    public int getIdPlayer() {
        return idPlayer;
    }

    /**
     * Get the number of times the player has died (represented by skulls)
     * @return number of times the player has died
     */
    public int getNumberOfSkulls() {
        return numberOfSkulls;
    }

    /**
     * Return the damage line of the player
     * @return the damage line of the player
     */
    public List<String> getDamageLine(){
        return damageLine;
    }

    /**
     * Return true if the player is the starting player
     * @return true if the player is the starting player, false otherwise
     */
    public boolean isStartingPlayer(){
        return startingPlayer;
    }

    /**
     * This player deals damage to another player
     * @param nrDamage - number of damages
     * @param targetPlayer
     */
    public void dealDamage(int nrDamage, Player targetPlayer){
        for(int i=0; i < nrDamage; i++) {
            targetPlayer.damageLine.add(charaName);
        }
    }

    /**
     * This player marks another player
     * @param nrMark - number of marks
     * @param targetPlayer
     */
    public void markPlayer(int nrMark, Player targetPlayer){
        for(int i=0; i < nrMark; i++){
            targetPlayer.markLine.add(charaName);
        }
    }

    /**
     * Move the player to the specified square of the map
     * @param square - the square to move the player to
     */
    public void moveTo(Square square) {
        this.square = square;
    }

    /**
     * Return true if the player has already been killed and has not been respawned yet
     * @return true if the player has already been killed and has not been respawned yet
     */
    public boolean isPlayerDown(){
        return playerDown;
    }

    /**
     * Set the player as player down (it means he/she is dead)
     */
    public void putPlayerDown() {
        playerDown = true;
    }

    /**
     * Set the player as alive after he/she has been respawned
     */
    public void raisePlayerUp(){
        playerDown = false;
    }

    /**
     * Get the ammo box
     * @return ammo box
     */
    public AmmoBox getAmmoBox() {
        return ammoBox;
    }

    /**
     * Add the specified ammo to the ammo box
     * @param list - ammo to add
     */
    public void addAmmo(List<AmmoType> list){
        for(AmmoType element : list){
            ammoBox.addWhenPossible(element);
        }
    }

    /**
     * Get the ammo card on the square on which the player is, and add its ammo to the ammo box
     * (and also draw a power up if the ammo card give you one
     * @param ammoPoint - a square with an ammo card on it, that the player wants to grab
     */
    private void grabAmmoFromAmmoPoint(AmmoPoint ammoPoint){

        AmmoCard grabbedCard = ammoPoint.grabCard();
        addAmmo(grabbedCard.getAmmo());

        if(grabbedCard.hasPowerUp()){
            //TODO: draw a powerup card
        }
    }

    /**
     * Grab the ammo card from the square the player is on, adding ammo to the ammo box and also,
     * if specified on the ammo card, get the player
     */
    public void grabAmmoCardFromSquare(){
        if(square instanceof AmmoPoint) {
            grabAmmoFromAmmoPoint((AmmoPoint)square);
        }
        else{
            //TODO: throws exception
        }
    }


    /**
     * Remove the specified ammo unit spent/used from the ammo box
     * @param ammoToSpend
     */
    public void discardAmmo(AmmoType ammoToSpend){
        ammoBox.remove(ammoToSpend);
    }

    /**
     * Eliminate the specified ammo spent/used from the ammo box
     * @param ammoToSpend
     */
    public void discardAmmo(List<AmmoType> ammoToSpend){
        for(AmmoType element : ammoToSpend){
            ammoBox.remove(element);
        }
    }

    /**
     * Get loaded weapons (that are only visible to their own player)
     * @return loaded weapons
     */
    public List<WeaponCard> getLoadedWeapons() {
        return loadedWeapons;
    }

    /**
     * Get unloaded weapons of the player(that are visible to every player)
     * @return unloaded weapons
     */
    public List<WeaponCard> getUnloadedWeapons() {
        return unloadedWeapons;
    }

    /**
     * Get the damage that this player has received from a specified player
     * @param player - the specified player
     * @return the damage that this player has received from the specified player
     */
    public int damageReceivedFrom(Player player){
        int damage = 0;
        for(String name : damageLine){
            if(name.equals(player.charaName)){
                damage++;
            }
        }
        return damage;
    }

    /**
     * Get list of character names in order of first hit on the damage line of this player
     * @return list of character names in order of first hit on the damage line of this player
     */
    public List<String> charaNamesInOrderOfFirstHit(){
        List<String> list = new ArrayList<>();
        for(String name : damageLine){
            if(!list.contains(name)){
                list.add(name);
            }
        }
        return list;
    }

    /**
     * Reload a weapon
     * @param weapon - weapon to reload
     */
    public void reload(WeaponCard weapon){
        loadedWeapons.add(weapon);
        unloadedWeapons.remove(weapon);
    }

    /**
     * Get powerups on the player's hand
     * @return powerups of the player
     */
    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    /**
     * Get all the points gained by the player during the game
     * @return all the points gained by the player during the game
     */
    public int getScore() {
        return score;
    }

    /**
     * Add the specified ammount of points to the player
     * @param points - points to add
     */
    public void addPointsToScore(int points){
        score += points;
    }

    /**
     * Return true if the player is active
     * @return true if the player is active
     */
    public boolean isActive(){
        return active;
    }

    /**
     * Set the player as inactive
     */
    public void suspendPlayer(){
        active = false;
        gameTable.decreaseNumberOfActivePlayers();
    }

    /**
     * Set the player as active when he/she comes back after a period of inactivity
     */
    public void reactivatePlayer(){
        active = true;
        gameTable.increaseNumberOfActivePlayers();
    }
}
