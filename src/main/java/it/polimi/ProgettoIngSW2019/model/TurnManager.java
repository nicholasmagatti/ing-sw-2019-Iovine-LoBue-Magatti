package it.polimi.ProgettoIngSW2019.model;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ProgettoIngSW2019.utilities.Observable;

/**
 * @author Nicholas Magatti
 */
public class TurnManager extends Observable <String>{

    private GameTable gameTable;
    private Player currentPlayer;

    /**
     * Constructor
     * @param gameTable
     */
    public TurnManager(GameTable gameTable){
        this.gameTable = gameTable;
    }

    /**
     * Set the next active player as the 'current player' to refer in this turn
     */
    public void changeCurrentPlayer(){
        int i = 1;
        while(!gameTable.getPlayers()[i].isActive()){
            i++;
        }
        currentPlayer = gameTable.getPlayers()[currentPlayer.getIdPlayer()+i];
    }

    //TODO: should I delete this method?
    //a single action of the player
    //a reload DOESN'T count as an action!
    public void action(){

    }

    /**
     * Return an array of points to assign to the players according to the specific circumstance
     * @param skulls - number of skulls on the player to score,
     *                 or 0 if we are scoring the killshot track
     * @return array of points to assign to the players
     */
    private int[] pointsToAssign(int skulls){

        final int lengthOfPointsWithNoSkulls = 5;

        int[] pointsWithNoSkulls = new int[lengthOfPointsWithNoSkulls];

        pointsWithNoSkulls[0] = 8;
        pointsWithNoSkulls[1] = 6;
        pointsWithNoSkulls[2] = 4;
        pointsWithNoSkulls[3] = 2;
        pointsWithNoSkulls[4] = 1;

        int[] pointsToAssign = new int[gameTable.getNumberOfPlayers()-1];

        for(int i=0, j = skulls; i < gameTable.getNumberOfPlayers(); i++, j++){
            if(j < lengthOfPointsWithNoSkulls) {
                pointsToAssign[i] = pointsWithNoSkulls[j];
            }
            else{
                //assign the last element of arrayOfPointsToAssign
                pointsToAssign[i] = pointsWithNoSkulls[lengthOfPointsWithNoSkulls-1];
            }
        }
        return pointsToAssign;
    }

    /**
     * Assign points to players after the death of the specified player, from his/her damage line;
     * or at the end of the game (in witch every player with damage get scored)
     * @param killedPlayer - killed player to score, or any player at the end of the game
     */
    public void scorePlayer(Player killedPlayer){

        int numberOfPlayers = gameTable.getNumberOfPlayers();

        //list of damages from players in order of who hit first
        List<String> charaNamesInOrderOfFirstHit = killedPlayer.charaNamesInOrderOfFirstHit();
        //array of damages from players in order of player ids
        int [] arrayOfDamagesFromPlayers = new int[numberOfPlayers];
        Player[] arrayOfPlayers = gameTable.getPlayers();

        for(int i=0; i < numberOfPlayers; i++){
            if(killedPlayer == arrayOfPlayers[i]){
                arrayOfDamagesFromPlayers[i] = 0;
            }
            else{
                arrayOfDamagesFromPlayers[i] = killedPlayer.damageReceivedFrom(arrayOfPlayers[i]);
            }
        }
        //put in a list the first, the second etc (in order of who dealt more damage)
        //TODO
        List<Player> orderedByGreaterDamage = new ArrayList<>();

        //determine how many points the first get, the second etc (create a method that,
        // given the number of skulls on a player, returns a list with points his/her killers get)
        int[] pointsToAssign = pointsToAssign(killedPlayer.getNumberOfSkulls());
        //Then assign the points to the players, handling possible ties
        //TODO
        //then if we are not in frenzy mode, assign first blood
        //TODO
    }

    /**
     * Assign points to players from the killshot track at the end of the game
     */
    public void scoreKillshotTrack(){
        //TODO
    }

    /**
     * At the end of a turn, check if any player has just died and return a list with those players
     * @return a list of the players down
     */
    public List<Player> checkDeadPlayers(){

        List<Player> playersDown = new ArrayList<>();
        Player[] allPlayers = gameTable.getPlayers();

        for(Player player : allPlayers){
            if (player.isPlayerDown()){
                playersDown.add(player);
            }
        }
        return playersDown;
    }

}
