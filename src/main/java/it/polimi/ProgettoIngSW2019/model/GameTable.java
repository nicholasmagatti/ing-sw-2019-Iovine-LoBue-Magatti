package it.polimi.ProgettoIngSW2019.model;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ProgettoIngSW2019.model.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.utilities.Observable;

/**
 * Class used to represent the game table and everything on it
 * @author Nicholas Magatti
 */
public class GameTable extends Observable <String>{

    private Player[] players;
    private int numberOfPlayers;
    private int activePlayers;
    private Deck weaponDeck;
    private Deck powerUpDeck;
    private Deck ammoDeck;
    private List<PowerUp> powerUpDiscarded = new ArrayList<>();
    private List<AmmoType> ammoDiscarded = new ArrayList<>();
    private Square[][] map = new Square[4][3];
    private List<KillToken> killshotTrack;
    private int spacesOnKillshotTrack; //between 5 and 8, chosen by the first user
    private boolean frenzyMode = false;

    //TODO: left and right board will probably be substituted by something else to create the board
    //set the whole board putting the two boards to form the map requested by the first user
    //parameters: 1 is side A, 0 is side B
    //and set the number of skull that you want to use for the game (requested by the first user)
    public GameTable(boolean leftBoard, boolean rightBoard, int spacesOnKillshotTrack){

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
        return killshotTrack.size() == spacesOnKillshotTrack;
    }

    //set left/right board A/B are used to create all the possible 4 faces of the two boards
    private void setLeftBoardA(){

    }

    private void setLeftBoardB(){

    }

    private void setRightBoardA(){

    }

    private void setRightBoardB(){

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
