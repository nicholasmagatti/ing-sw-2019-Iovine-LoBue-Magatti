package it.polimi.ProgettoIngSW2019.model;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ProgettoIngSW2019.model.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.model.enums.DeckType;

/**
 * Class used to represent the game table and everything on it
 * @author Nicholas Magatti
 */
public class GameTable{

    private Player[] players;
    private int numberOfPlayers;
    private int activePlayers;
    private Deck weaponDeck = new Deck(DeckType.WEAPON_CARD);
    private Deck powerUpDeck = new Deck(DeckType.POWERUP_CARD);
    private Deck ammoDeck = new Deck(DeckType.AMMO_CARD);
    private List<PowerUp> powerUpDiscarded = new ArrayList<>();
    //TODO: update on uml
    private List<AmmoCard> ammoDiscarded = new ArrayList<>();
    //[][] indicate row and column respectively ([row][column])
    private Square[][] map;
    private List<KillToken> killshotTrack = new ArrayList<>();
    private int numberOfSkullsForTheGame; //between 5 and 8, chosen by the first user
    private boolean frenzyMode = false;

    /**
     * Constructor
     * Set the map and the number of skulls(spaces available on the killshotTrack for tokens) as the first user requested
     * @param chosenMap - requested map
     * @param initialNumberOfSkulls - number of skulls requested
     */
    //TODO: update on uml
    public GameTable(Square[][] chosenMap, int initialNumberOfSkulls){
        if(initialNumberOfSkulls < 5 || initialNumberOfSkulls > 8){
            throw new IllegalArgumentException("The number of skulls for the game is " + initialNumberOfSkulls +
                    " but should be between 5 and 8.");
        }
        numberOfSkullsForTheGame = initialNumberOfSkulls;
        map = chosenMap;
        //set dependencies for every square of the map
        for(int i=0; i < 3; i++){
            for(int j=0; j < 4; j++){
                if(map[i][j] != null){
                    map[i][j].setDependency(map);
                }
            }
        }
    }

    /**
     * Set the players for the game, before its begin
      * @param listOfNames - ordered list of names of the players to create
     */
    //TODO: add to UML
    public void setPlayersBeforeStart(List<String> listOfNames) {
        numberOfPlayers = listOfNames.size();
        activePlayers = numberOfPlayers;
        players = new Player[numberOfPlayers];
        for(int i=0; i < numberOfPlayers; i++){
            players[i] = new Player(i, listOfNames.get(i), this);
        }
    }

    /**
     * Get the powerup deck on the table
     * @return the powerup deck
     */
    public Deck getPowerUpDeck() {
        return powerUpDeck;
    }

    /**
     * Get the weapon deck on the table
     * @return the weapon deck
     */
    public Deck getWeaponDeck() {
        return weaponDeck;
    }

    /**
     * Get the ammo deck on the table
     * @return the ammo deck
     */
    public Deck getAmmoDeck() {
        return ammoDeck;
    }

    /**
     * Get the list of discarded powerups
     * @return list of discarded powerups
     */
    //TODO: add to uml
    public List<PowerUp> getPowerUpDiscarded() {
        return powerUpDiscarded;
    }

    /**
     * Get the list of discarded ammo cards
     * @return list of discarded ammo cards
     */
    //TODO: add to uml
    public List<AmmoCard> getAmmoDiscarded() {
        return ammoDiscarded;
    }

    /**
     * Get the players on the board (all the players: also the inactive ones)
     * @return players
     */
    public Player[] getPlayers(){
        return players;
    }

    /**
     * Get the number of the players on the board (inactive players included)
     * @return number of the players on the board
     */
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /**
     * Get the killshot track
     * @return killshot track
     */
    public List<KillToken> getKillshotTrack() {
        return killshotTrack;
    }

    /**
     * Return true if the the killshot track is full of tokens
     * @return true if the killshot track is full of tokens,
     *         false otherwise
     */
    public boolean isKillshotTrackFull(){
        return killshotTrack.size() == numberOfSkullsForTheGame;
    }


    /**
     * Get the map of the game table(composed by squares)
     * @return map of the game table
     */
    public Square[][] getMap () {
        return map;
    }

    /**
     * Get the number of active players
     * @return number of active players
     */
    public int getNumberOfActivePlayers() {
        return activePlayers;
    }

    /**
     * Increase (of one) the number of active players
     */
    public void increaseNumberOfActivePlayers(){
        activePlayers++;
    }

    /**
     * Decrease (of one) the number of active players
     */
    public void decreaseNumberOfActivePlayers(){
        activePlayers--;
    }

    /**
     * Return true if is frenzyMode is on, false otherwise
     * @return true if is frenzyMode is on
     */
    public boolean isFrenzyMode() {
        return frenzyMode;
    }

    public void setFrenzyModeOn(){
        frenzyMode = true;
    }
}
