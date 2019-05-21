package it.polimi.ProgettoIngSW2019.model;


import java.util.ArrayList;
import java.util.List;
import it.polimi.ProgettoIngSW2019.common.enums.*;

/**
 * Class that represents the player
 * @author Nicholas Magatti
 */
public class Player{

    private int idPlayer;
    private String charaName;
    private GameTable gameTable;
    private List<String> damageLine = new ArrayList<>();
    //every mark is represented by a string that
    //is the name of the palyer who marked this player
    private List<String> markLine = new ArrayList<>();
    //NumberOfkulls = times this player has been killed
    //necessary for counting scores after he/she gets killed
    //IMPORTANT: 'numberOfSkulls' doesn't increment right after the player has
    //been killed, but after the score updates
    private int numberOfSkulls = 0;
    private boolean playerDown = false;
    private Square square;
    private int score = 0;
    private List<WeaponCard> loadedWeapons = new ArrayList<>();
    private List<WeaponCard> unloadedWeapons = new ArrayList<>();
    private List<PowerUp> powerUps = new ArrayList<>();
    private AmmoBox ammoBox = new AmmoBox();
    private boolean active = true;


    /**
     * Constructor
     */
    public Player(int idPlayer, String charaName, GameTable gameTable){
        this.idPlayer = idPlayer;
        this.charaName = charaName;
        this.gameTable = gameTable;
        if(charaName == null) {
            throw new NullPointerException();
        }
        if(charaName == ""){
            throw new RuntimeException("The name of a player cannot be an empty string.");
        }
    }

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
     * Get the name of the character
     * @return name of the character
     */
    public String getCharaName() {
        return charaName;
    }

    /**
     * Get the number of times the player has died (represented by skulls)
     * @return number of times the player has died
     */
    public int getNumberOfSkulls() {
        return numberOfSkulls;
    }

    /**
     * Increase by one the number of skulls of this player, that represent the number of times he/she has died.
     * WARNING: this has to be done only after scoring the damage line of this player
     */
    public void increaseNumberOfSkulls(){
        numberOfSkulls++;
    }

    /**
     * Reset to zero the number of skulls on the player
     */
    public void resetNumberOfSkullsToZero(){
        numberOfSkulls = 0;
    }

    /**
     * Return the damage line of the player
     * @return the damage line of the player
     */
    public List<String> getDamageLine(){
        return damageLine;
    }

    /**
     * Return the mark line of the player
     * @return the damage line of the player
     */
    public List<String> getMarkLine(){
        return markLine;
    }

    /**
     * Return true if the player is the starting player
     * @return true if the player is the starting player, false otherwise
     */
    public boolean isStartingPlayer(){
        if(idPlayer == 0){
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Get the number of weapons owned by the player
     * @return number of weapons owned by the player
     */
    public int getNumberOfWeapons(){
        return loadedWeapons.size() + unloadedWeapons.size();
    }

    /**
     * Get the number of powerups owned by the player
     * @return number of powerups owned by the player
     */
    public int getNumberOfPoweUps(){
        return powerUps.size();
    }

    /**
     * Get the number of times the name occurs in the list
     * @param name
     * @param list
     * @return the number of times the name occurs in the list
     */
    private int occurrenceOfNameInList(String name, List<String> list){
        int occurrence = 0;
        for(String element : list){
            if(element.equals(name)){
                occurrence++;
            }
        }
        return occurrence;
    }

    /**
     * Remove the marks previously placed by this player on the target and return their number
     * @param targetPlayer
     * @return number of marks previously placed by this player on the target
     */
    private int removeMyMarksOnTargetPlayerAndReturnNumber(Player targetPlayer){
        int removedMarks = 0;
        while(targetPlayer.getMarkLine().remove(charaName)){
            removedMarks++;
        }
        return removedMarks;
    }

    /**
     * This player deals damage to another player, converts marks that already are on the target into damages
     * and also set that player down if he/she dies as a result of this attack
     * @param nrDamage - number of damages
     * @param targetPlayer
     */
    public void dealDamage(int nrDamage, Player targetPlayer){

        if(targetPlayer == null){
            throw new NullPointerException("The target of a damage cannot be null.");
        }

        if(targetPlayer == this){
            throw new IllegalArgumentException("A player cannot damage himself/herself.");
        }
        if(nrDamage < 0){
            throw new IllegalArgumentException("Damage cannot be negative.");
        }
        //marks that will be converted into damage
        int marksJustRemoved = removeMyMarksOnTargetPlayerAndReturnNumber(targetPlayer);
        final int TOT_DAMAGE_TO_KILL = 11;
        final int TOT_DAMAGE_TO_OVERKILL = 12;
        int totDamageOnTarget = targetPlayer.getDamageLine().size();

        for(int i=0; i < (nrDamage + marksJustRemoved) && totDamageOnTarget < TOT_DAMAGE_TO_OVERKILL; i++, totDamageOnTarget++) {
            targetPlayer.getDamageLine().add(charaName);
        }
        if(totDamageOnTarget >= TOT_DAMAGE_TO_KILL){
            targetPlayer.putPlayerDown();
        }
    }

    /**
     * This player marks another player, but wasting the marks that exceed the limit of three marks from this player
     * @param nrMarks - number of marks
     * @param targetPlayer
     */
    public void markPlayer(int nrMarks, Player targetPlayer){
        if(nrMarks < 0){
            throw new IllegalArgumentException("Number of marks to assign cannot be negative.");
        }
        if(targetPlayer == this){
            throw new IllegalArgumentException("A player cannot mark him/herself");
        }
        if(targetPlayer == null){
            throw new NullPointerException("A player is trying to mark but the target is null.");
        }
        int myMarksOnTarget = 0;
        for(String mark : targetPlayer.getMarkLine()){
            if(mark.equals(charaName)){
                myMarksOnTarget++;
            }
        }
        for(int i=0; i < nrMarks && myMarksOnTarget < 3; i++, myMarksOnTarget++){
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
    public void risePlayerUp(){
        playerDown = false;
    }

    /**
     * Get the quantity of blue ammo in the ammo box
     * @return quantity of blue ammo in the ammo box
     */
    public int getBlueAmmo(){
        return ammoBox.getBlueAmmo();
    }
    /**
     * Get the quantity of red ammo in the ammo box
     * @return quantity of red ammo in the ammo box
     */
    public int getRedAmmo(){
        return ammoBox.getRedAmmo();
    }

    /**
     * Get the quantity of yellow ammo in the ammo box
     * @return quantity of yellow ammo in the ammo box
     */
    public int getYellowAmmo(){
        return ammoBox.getYellowAmmo();
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
    private void grabAmmoCardFromAmmoPoint(AmmoPoint ammoPoint){

        AmmoCard grabbedCard = ammoPoint.grabCard();
        if(grabbedCard == null){
            throw new NullPointerException("The player should not even try to grab an ammo card from an Ammo point without a card on it");
        }
        addAmmo(grabbedCard.getAmmo());

        if(grabbedCard.hasPowerUp()){
            //draw a powerup card if you can
            if(powerUps.size() < 3){
                powerUps.add((PowerUp) gameTable.getPowerUpDeck().drawCard());
            }
        }
    }

    /**
     * Grab the ammo card from the square the player is on, adding ammo to the ammo box and also,
     * if specified on the ammo card, get the player
     */
    public void grabAmmoCardFromThisSquare(){
        if(square.getSquareType() == SquareType.AMMO_POINT) {
            grabAmmoCardFromAmmoPoint((AmmoPoint)square);
        }
        else{
            throw new RuntimeException("Trying to grab an ammo card from a spawn point");
        }
    }

    /**
     * Get a weapon from a spawnpoint, paying its cost
     * @param cardToGet - weapon to buy
     * @param cardToLeave - weapon to leave if the player already has one, it is null otherwise
     */
    public void grabWeapon(WeaponCard cardToGet, WeaponCard cardToLeave){
        if(square.getSquareType() == SquareType.SPAWNING_POINT){
            loadedWeapons.add(cardToGet);
            discardAmmo(cardToGet.getBuyCost());
            if(cardToLeave == null) {
                ((SpawningPoint) square).removeWeaponFromSpawnPoint(cardToGet);
            }
            else{
                ((SpawningPoint) square).swapWeaponsOnSpawnPoint(cardToGet, cardToLeave);
            }
        }
        else{
            throw new RuntimeException("Trying to grab a weapon from a square that is not a spawn point.");
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
     * Check if the player can spend the required quantity of ammo
     * @param ammoToSpend - the specified quantity of ammo
     * @return true if the player can spend that ammo, false otherwise
     */
    public boolean hasEnoughAmmo(List<AmmoType> ammoToSpend){
        //if the ammoToSpend is zero, it can indicated as null or as an empty list
        if (ammoToSpend == null || ammoToSpend.isEmpty()){
            return true;
        }
        //otherwise:
        int blueToSpend = 0;
        int redToSpend = 0;
        int yellowToSpend = 0;
        for(AmmoType element : ammoToSpend){
            switch (element){
                case BLUE:
                    blueToSpend++;
                    break;
                case RED:
                    redToSpend++;
                    break;
                case YELLOW:
                    yellowToSpend++;
                    break;
            }
        }
        if(ammoBox.getBlueAmmo() >= blueToSpend &&
                ammoBox.getRedAmmo() >= redToSpend &&
                ammoBox.getYellowAmmo() >= yellowToSpend){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Reload a weapon
     * @param weapon - weapon to reload
     */
    public void reload(WeaponCard weapon){
        discardAmmo(weapon.getreloadCost());
        loadedWeapons.add(weapon);
        unloadedWeapons.remove(weapon);
    }

    /**
     * Right after using the effect of a weapon, set that weapon as unloaded.
     * @param weapon
     */
    public void unloadWeaponAfterUse(WeaponCard weapon){
        unloadedWeapons.add(weapon);
        loadedWeapons.remove(weapon);
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
        return occurrenceOfNameInList(player.getCharaName(), damageLine);
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
     * Remove all damage from the damage line
     */
    public void emptyDamageLine(){
        damageLine = new ArrayList<>();
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

    /**
     * Return the square on which the player is
     * @return the square
     */
    public Square getPosition() {
        return square;
    }

    /**
     * Class used to manage the ammo box of a player and its content
     * @author Nicholas Magatti
     */
    private class AmmoBox {
        //number of the following attributes between 0 and 3
        private int redAmmo = 1;
        private int blueAmmo = 1;
        private int yellowAmmo = 1;

        /**
         * Get the number of red ammo units
         * @return the number of red ammo units
         */
        public int getRedAmmo() {
            return redAmmo;
        }

        /**
         * Get the number of blue ammo units
         * @return the number of blue ammo units
         */
        public int getBlueAmmo() {
            return blueAmmo;
        }

        /**
         * Get the number of yellow ammo units
         * @return the number of yellow ammo units
         */
        public int getYellowAmmo() {
            return yellowAmmo;
        }

        /**
         * Remove a unit of ammo of a specific color from the ammo box
         * @param ammoType
         */
        public void remove(AmmoType ammoType){
            switch (ammoType){
                case RED:
                    redAmmo--;
                    break;
                case BLUE:
                    blueAmmo--;
                    break;
                case YELLOW:
                    yellowAmmo--;
                    break;
            }
        }

        /**
         * Add a unit of ammo of a specific color to the ammo box
         * @param ammoType
         */
        public void addWhenPossible(AmmoType ammoType){
            switch (ammoType){
                case RED:
                    if(redAmmo < 3){
                        redAmmo++;
                    }
                    break;
                case BLUE:
                    if(blueAmmo < 3) {
                        blueAmmo++;
                    }
                    break;
                case YELLOW:
                    if(yellowAmmo < 3) {
                        yellowAmmo++;
                    }
                    break;
            }
        }
    }
}

