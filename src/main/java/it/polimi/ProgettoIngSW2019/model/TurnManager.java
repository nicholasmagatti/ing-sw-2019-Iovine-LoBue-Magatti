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
        int idNextPlayer = currentPlayer.getIdPlayer() + 1;
        //check if the player is active, otherwise go to the next one and repeat
        while(!gameTable.getPlayers()[idNextPlayer].isActive()){
            if(idNextPlayer < gameTable.getPlayers().length - 1) {
                idNextPlayer++;
            }
            else{
                idNextPlayer = 0;
            }
        }
        currentPlayer = gameTable.getPlayers()[idNextPlayer];
    }

    //TODO: delete method action() from uml

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
     * Get the array of points to assign when scoring either a player or the killshot track
     * @param numberOfSkulls - zero if scoring a killshot track, otherwise number of skulls on the player,
     *                       representing the number of times he/she has died
     * @return array of points to assign when scoring either a player or the killshot track
     */
    private int[] pointsToAssignScoringSomething(int numberOfSkulls){
        //succession of points to assign without considering the number of skulls
        final int lengthOfSuccessionOfPoints = 5;
        int[] successionOfPoints = new int[lengthOfSuccessionOfPoints];

        successionOfPoints[0] = 8;
        successionOfPoints[1] = 6;
        successionOfPoints[2] = 4;
        successionOfPoints[3] = 2;
        successionOfPoints[4] = 1;

        //excluding the player to score
        int lengthOfPointsToAssign = gameTable.getPlayers().length-1;

        //succession of points to assign considering the number of skulls on the player to score
        int[] pointsToAssign = new int[lengthOfPointsToAssign];

        for(int i=0, j = numberOfSkulls; i < lengthOfPointsToAssign; i++, j++){
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
     * Return an array of points to assign to the players who damaged the player to score, according to the specific circumstance
     * @param playerToScore
     * @return array of points to assign to the players
     */
    private int[] pointsToAssignScoringAPlayer(Player playerToScore){

        return pointsToAssignScoringSomething(playerToScore.getNumberOfSkulls());
    }

    private int[] pointsToAssignScoringTheKillshotTrack(){

        return pointsToAssignScoringSomething(0);
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
     * From a list of names of players, return the list of the respective players, in the same order
     * @param names - list of names
     * @return list of the respective players
     */
    //TODO: add to uml
    private List<Player> listOfPlayersFromNames(List<String> names){
        List<Player> players = new ArrayList<>();
        for(String name : names){
            players.add(getPlayerFromCharaName(name));
        }
        return players;
    }

    /**
     * At the end of the turn, score the damage line of all the dead players
     */
    //TODO: add to uml
    public void scoreDamageLineOfAllDeadPlayers(){

        List<Player> deadPlayers = checkDeadPlayers();

        if(deadPlayers.isEmpty()){
            throw new RuntimeException("There is no dead player on the table. This method should not have been called.");
        }

        for(Player player : deadPlayers){
            scoreDamageLineOf(player);
        }
        //assign 1 extra point for more than one kill in the same turn
        if (deadPlayers.size() > 1){
            currentPlayer.addPointsToScore(1);
        }
    }

    /**
     * At the end of the game, sore the damage line of all the players on the game with damage on them
     */
    //TODO: add to uml
    public void scoreDamageLineOfAllPlayersWithDamage(){
        Player[] players = gameTable.getPlayers();

        for(Player player : players){
            if(!player.getDamageLine().isEmpty()) {
                scoreDamageLineOf(player);
            }
        }
    }
    /**
     * Assign points to players after the death of the specified player(the player to score), from his/her damage line;
     * or at the end of the game (in witch every player with damage get scored, even if still alive)
     * @param playerToScore - killed player to score, or any player at the end of the game
     */
    private void scoreDamageLineOf(Player playerToScore) {

        //list of damages from players in order of who hit first:
        List<String> charaNamesInOrderOfFirstHit = playerToScore.charaNamesInOrderOfFirstHit();
        //convert list of names to list of players
        List<Player> playersInOrderOfFistHit = listOfPlayersFromNames(charaNamesInOrderOfFirstHit);
        //assign 1 point for first blood
        playersInOrderOfFistHit.get(0).addPointsToScore(1);

        /*
        determine how many points the first get, the second etc (use a method that,
        given the number of skulls on a player, returns an array of the number of points his/her killers get)
         */
        int[] pointsToAssign = pointsToAssignScoringAPlayer(playerToScore);

        int numberOfPlayers = gameTable.getPlayers().length;

        //create array of damages from players in order of player ids (it is still empty now)
        List<Integer> listOfDamagesFromPlayers = new ArrayList<>();
        Player[] allPlayers = gameTable.getPlayers();
        //fill the array of damages from players
        for (int i = 0; i < numberOfPlayers; i++) {
            if (playerToScore == allPlayers[i]) {
                listOfDamagesFromPlayers.add(0);
            }
            else {
                listOfDamagesFromPlayers.add(playerToScore.damageReceivedFrom(allPlayers[i]));
            }
        }
        assignPoints(listOfDamagesFromPlayers, pointsToAssign, playersInOrderOfFistHit, allPlayers);
    }

    /**
     * Assign points to players from the killshot track at the end of the game
     */
    public void scoreKillshotTrack(){
        //TODO
        //list of damages from players in order of who hit first:
        List<String> tmp = gameTable.namesOnKillshotTrackInOrderOfFirstHit();
        //convert list of names to list of players
        List<Player> playersInOrderOfFistHit = listOfPlayersFromNames(tmp);

        //determine how many points the first get, the second etc
        int[] pointsToAssign = pointsToAssignScoringTheKillshotTrack();


        int numberOfPlayers = gameTable.getPlayers().length;

        //create array of damages from players in order of player ids (it is still empty now)
        List<Integer> listOfTokensFromPlayers = new ArrayList<>();
        Player[] allPlayers = gameTable.getPlayers();
        //fill the array of damages from players ordered by idPlayer
        for (int i = 0; i < numberOfPlayers; i++) {
            listOfTokensFromPlayers.add(gameTable.tokensOnKillshotTrackBelongingTo(allPlayers[i]));
        }
        //find the greater and if more than one, find the one that hit first and assign pointsToAssign[i] to that one
        //then make it =0 in listOfDamagesFromPlayer and do all of this again
        assignPoints(listOfTokensFromPlayers, pointsToAssign, playersInOrderOfFistHit, allPlayers);
    }

    /**
     * Assign points to players after scoring either a damage line from a player or the killshot track, given the following parameters
     * @param listOfDamagesFromPlayers - list of tokens on the object to score, ordered by idPlayer
     * @param pointsToAssign - array of points to assign, ordered by grater value
     * @param playersInOrderOfFistHit - list of players oredered by first hit (the players without tokens on the object we are scoring
     *                                will not be present in this list)
     * @param allPlayers - array of all players, ordered by id
     */
    private void assignPoints
            (List<Integer> listOfDamagesFromPlayers, int[] pointsToAssign, List<Player> playersInOrderOfFistHit, Player[] allPlayers){
        int tmpId;
        for(int i=0; max(listOfDamagesFromPlayers) > 0; i++) {
            tmpId = IdOfThePlayerWhoHitFirstBetweenThese(indexesOfGreaterValue(listOfDamagesFromPlayers), playersInOrderOfFistHit);
            allPlayers[tmpId].addPointsToScore(pointsToAssign[i]);
            listOfDamagesFromPlayers.set(tmpId, 0);
        }
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
