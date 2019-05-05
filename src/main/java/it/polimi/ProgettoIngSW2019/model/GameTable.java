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
    private List<AmmoType> ammoDiscarded = new ArrayList<>();
    private Square[][] map = new Square[4][3];
    private List<KillToken> killshotTrack;
    private int numberOfSkullsForTheGame; //between 5 and 8, chosen by the first user
    private boolean frenzyMode = false;

    /**
     * Constructor
     * Set the map and the number of skulls(spaces available on the killshotTrack for tokens) as the first user requested
     * @param idMap - identifier of the map requested
     * @param initialNumberOfSkulls - number of skulls requested
     */
    public GameTable(int idMap, int initialNumberOfSkulls){

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
     * Get the coordinates of a square
     * @param square
     * @return position of the square
     */
    //TODO: if it turns out not to be useful, delete it
    public int[] getPositionOfSquare(Square square){
        int[] position = new int[2];
        for(int i=0; i < 4; i++){
            for (int j=0; j < 3; j++){
                if(map[i][j] == square){
                    position[0] = i;
                    position[1] = j;
                }
            }
        }
        return position;
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
