package it.polimi.ProgettoIngSW2019.model;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Nicholas Magatti
 */
public class TurnManager{

    private GameTable gameTable;
    private Player currentPlayer;
    //TODO:add to uml
    private int actionsLeftForThisTurn;

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
     * Set the number of actions available to this player on this turn
     * @param num - number of actions available to this player on this turn
     */
    //TODO: add to uml
    public void setNrActionsForTurn(int num){
        actionsLeftForThisTurn = num;
    }

    /**
     * Decrease the number of actions left to this player for the current turn
     */
    //TODO: add to uml
    public void decreaseActionsLeft(){
        actionsLeftForThisTurn--;
    }

    /**
     * Return the player whom this turn is
     * @return the player whom this turn is
     */
    //TODO: add to uml
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Return the number of actions left to the current player for this turn
     * @return number of actions left to the current player for this turn
     */
    //TODO: add to uml
    public int getActionsLeft(){
        return actionsLeftForThisTurn;
    }
    /**
     * Convert the name of the character in the actual player if present,
     * return null otherwise
     * @param charaName - Name of the character
     * @return the player with that character name if there is one, null otherwise
     */
    private Player getPlayerFromCharaName(String charaName){
        for(Player player : gameTable.getPlayers()) {
            if (player.getCharaName().equals(charaName)) {
                return player;
            }
        }
        //if there is no player with such name, return null
        return null;
    }

    /**
     * Return an array of points to assign to the players who damaged the player to score, according to the specific circumstance
     * @param playerToScore
     * @return array of points to assign to the players
     */
    private int[] pointsToAssignWhenScoringAPlayer(Player playerToScore){

        //succession of points to assign without considering the number of skulls
        final int lengthOfSuccessionOfPoints = 5;
        int[] successionOfPoints = new int[lengthOfSuccessionOfPoints];

        successionOfPoints[0] = 8;
        successionOfPoints[1] = 6;
        successionOfPoints[2] = 4;
        successionOfPoints[3] = 2;
        successionOfPoints[4] = 1;

        //excluding the player to score
        int lengthOfPointsToAssign = gameTable.getNumberOfPlayers()-1;

        //succession of points to assign considering the number of skulls on the player to score
        int[] pointsToAssign = new int[lengthOfPointsToAssign];

        for(int i=0, j = playerToScore.getNumberOfSkulls(); i < lengthOfPointsToAssign; i++, j++){
            if(j < lengthOfSuccessionOfPoints) {
                pointsToAssign[i] = successionOfPoints[j];
            }
            else{
                //assign the last element of successionOfPoints
                pointsToAssign[i] = successionOfPoints[lengthOfPointsToAssign-1];
            }
        }
        return pointsToAssign;
    }

    /**
     * Given a list of integers, return a list of integers of the positions of the elements with the greater integer value
     * @param list
     * @return list of integers of the positions of the elements with the greater integer value
     */
    private List<Integer> indexesOfGreaterValue(List<Integer> list){
        List<Integer> positionsOfGreaterValue = new ArrayList<>();
        int max = max(list);
        for(Integer elem : list){
            if(elem.equals(max)) {
                positionsOfGreaterValue.add(elem);
            }
        }
        return positionsOfGreaterValue;
    }

    /**
     * Return the greater value contained in a list of integers
     * @param list
     * @return greater value contained in a list of integers
     */
    private int max(List<Integer> list){
        int max = 0;
        for(Integer elem : list){
            if (elem > max){
                max = elem;
            }
        }
        return max;
    }

    /**
     * Get the id of the player who hit first between the given ids
     * @param ids
     * @param playersInOrderOfFirstHit
     * @return id of the player who hit first between the given ids
     */
    private int IdOfThePlayerWhoHitFirstBetweenThese(List<Integer> ids, List<Player> playersInOrderOfFirstHit) {
        for (Player p : playersInOrderOfFirstHit) {
            for (Integer id : ids) {
                if (id.equals(p.getIdPlayer())) {
                    return id;
                }
            }
        }
        return -1;
    }

    /**
     * Assign points to players after the death of the specified player(the player to score), from his/her damage line;
     * or at the end of the game (in witch every player with damage get scored)
     * @param playerToScore - killed player to score, or any player at the end of the game
     */
    //TODO: make this method use as much general methods as possible that will be used by both updateScores(Player) and scoreKillshotTrack()
    public void updateScore(Player playerToScore) {

        //list of damages from players in order of who hit first:
        List<String> charaNamesInOrderOfFirstHit = playerToScore.charaNamesInOrderOfFirstHit();
        //convert list of names in list of players
        List<Player> playersInOrderOfFistHit = new ArrayList<>();
        for(String name : charaNamesInOrderOfFirstHit){
            playersInOrderOfFistHit.add(getPlayerFromCharaName(name));
        }
        //assign 1 point for first blood
        playersInOrderOfFistHit.get(0).addPointsToScore(1);

        /*
        determine how many points the first get, the second etc (create a method that,
        given the number of skulls on a player, returns an array of the number of points his/her killers get)
         */
        int[] pointsToAssign = pointsToAssignWhenScoringAPlayer(playerToScore);

        int numberOfPlayers = gameTable.getNumberOfPlayers();

        //create array of damages from players in order of player ids (it is still empty now)
        List<Integer> listOfDamagesFromPlayers = new ArrayList<>();
        Player[] allPlayers = gameTable.getPlayers();
        //fill the array of damages from players
        for (int i = 0; i < numberOfPlayers; i++) {
            //TODO: remove this if so that can be used for the
            if (playerToScore == allPlayers[i]) {
                listOfDamagesFromPlayers.add(0);
            }
            else {
                listOfDamagesFromPlayers.add(playerToScore.damageReceivedFrom(allPlayers[i]));
            }
        }
        //find the greater and if more than one, find the one that hit first and assign pointsToAssign[i] to that one
        //then make it =0 in listOfDamagesFromPlayer and do all of this again
        int tmpId;
        for(int i=0; max(listOfDamagesFromPlayers) > 0; i++) {
            tmpId = IdOfThePlayerWhoHitFirstBetweenThese
                    (indexesOfGreaterValue(listOfDamagesFromPlayers), playersInOrderOfFistHit);
            allPlayers[tmpId].addPointsToScore(pointsToAssign[i]);
            listOfDamagesFromPlayers.set(tmpId, 0);
        }
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
