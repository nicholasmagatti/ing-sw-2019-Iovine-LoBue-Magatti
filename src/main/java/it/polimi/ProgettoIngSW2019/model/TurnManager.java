package it.polimi.ProgettoIngSW2019.model;

import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * Manage change of turn, points and keep track of actions left per turn.
 * @author Nicholas Magatti
 */
public class TurnManager{

    private GameTable gameTable;
    private Player currentPlayer;
    private int actionsLeftForThisTurn;


    /**
     * Constructor that sets the game table, the first player as current player and the number of actions
     * available for the turn of that player
     * @param gameTable
     */
    public TurnManager(GameTable gameTable){
        this.gameTable = gameTable;
        currentPlayer = gameTable.getPlayers()[0];
        actionsLeftForThisTurn = GeneralInfo.ACTIONS_PER_TURN;
    }

    /**
     * Return the game table
     * @return the game table
     */
    public GameTable getGameTable() {
        return gameTable;
    }

    /**
     * Set the next active player as the 'current player' to refer in this turn and
     * reset the number of actions available.
     */
    public void changeCurrentPlayer(){

        resetActionsLeftForNextTurn();

        int idNextPlayer = currentPlayer.getIdPlayer();

        //first, go to the next player
        //check if the player is active, otherwise go to the next one and repeat
        do{
            /*
              check if the id player corresponds to the last one:
              in that case, the next is the id 0 (otherwise id++)
             */
            if(idNextPlayer < gameTable.getPlayers().length - 1) {
                idNextPlayer++;
            }
            else{
                idNextPlayer = 0;
            }
        }while(!gameTable.getPlayers()[idNextPlayer].isActive());
        currentPlayer = gameTable.getPlayers()[idNextPlayer];
    }

    /**
     * Reset the number of actions available for the player of the next turn
     */
    private void resetActionsLeftForNextTurn(){
        actionsLeftForThisTurn = GeneralInfo.ACTIONS_PER_TURN;
    }

    /**
     * Decrease the number of actions left to this player for the current turn
     */
    public void decreaseActionsLeft(){
        actionsLeftForThisTurn--;
    }

    /**
     * Return the player whom this turn is
     * @return the player whom this turn is
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Return the number of actions left to the current player for this turn
     * @return number of actions left to the current player for this turn
     */
    public int getActionsLeft(){
        return actionsLeftForThisTurn;
    }
    /**
     * Convert the name of the character in the actual player if present,
     * throw exception otherwise
     * @param charaName - Name of the character
     * @return the player with that character name if there is one
     * @throws IllegalArgumentException if a player with such a name does not exist
     */
    public Player getPlayerFromCharaName(String charaName) throws IllegalArgumentException{
        for(Player player : gameTable.getPlayers()) {
            if (player.getCharaName().equals(charaName)) {
                return player;
            }
        }
        //if there is no player with such name:
        throw new IllegalArgumentException("There is no player with the name '" + charaName + "'.");
    }

    /**
     * Get the array of points to assign when scoring either a player or the killshot track
     * @param numberOfSkulls - zero if scoring a killshot track, otherwise number of skulls on the player,
     *                       representing the number of times he/she has died
     * @return array of points to assign when scoring either a player or the killshot track
     */
    private int[] pointsToAssignScoringSomething(int numberOfSkulls){
        //succession of points to assign without considering the number of skulls
        int [] successionOfPoints = {8, 6, 4, 2, 1};

        int lengthOfPointsToAssign = gameTable.getPlayers().length;

        //succession of points to assign considering the number of skulls on the player to score
        int[] pointsToAssign = new int[lengthOfPointsToAssign];

        for(int i=0, j = numberOfSkulls; i < lengthOfPointsToAssign; i++, j++){
            if(j < successionOfPoints.length) {
                pointsToAssign[i] = successionOfPoints[j];
            }
            else{
                //assign the last element of successionOfPoints
                pointsToAssign[i] = successionOfPoints[successionOfPoints.length - 1];
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

        for(int i=0; i < list.size(); i++){
            if(list.get(i).equals(max)) {
                positionsOfGreaterValue.add(i);
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
     * @param ids - list of ids of the player of which we want to get the one who hit first, given the order of
     *              first hits
     * @param playersInOrderOfFirstHit
     * @return id of the player who hit first between the given ids
     * @throws RuntimeException if none of the ids of the parameters corresponds to the id of any player in the game
     */
    private int IdOfThePlayerWhoHitFirstBetweenThese(List<Integer> ids, List<Player> playersInOrderOfFirstHit)
            throws RuntimeException{
        if (ids.size() == 1){
            return ids.get(0);
        }
        //if the list 'ids' contains more than 1 element:
        for (Player p : playersInOrderOfFirstHit) {
            for (Integer id : ids) {
                if (id.equals(p.getIdPlayer())) {
                    return id;
                }
            }
        }
        throw new RuntimeException("Something went wrong with the parameters of this method.");
    }

    /**
     * From a list of names of players, return the list of the respective players, in the same order
     * @param names - list of names
     * @return list of the respective players
     */
    private List<Player> listOfPlayersFromNames(List<String> names){
        List<Player> players = new ArrayList<>();
        for(String name : names){
            players.add(getPlayerFromCharaName(name));
        }
        return players;
    }

    /**
     * Assign the extra point for first blood and return the player that got that extra point.
     * Warning: this method should be called only when you can assign points for first blood.
     * @param damagedPlayer
     * @return the player who got points for first blood
     */
    public Player assignFirstBlood(Player damagedPlayer){
        String charaName = damagedPlayer.getDamageLine().get(0);
        Player whoGetsFirstBlood = getPlayerFromCharaName(charaName);
        whoGetsFirstBlood.addPointsToScore(1);
        return whoGetsFirstBlood;
    }

    /**
     * Assign points for double kill if possible and return true if it is, false otherwise
     * @return true if there has been a double kill, false otherwise
     */
    public boolean assignDoubleKillPoint(){
        if(checkDeadPlayers().size() > 1){
            currentPlayer.addPointsToScore(1);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Assign points to players after the death of the specified player(the player to score), from his/her damage line,
     * or at the end of the game (in witch every player with damage get scored, even if still alive),
     * except for the points for first blood and double kill
     * @param playerToScore - killed player to score, or any player at the end of the game
     * @return array of points just assigned to the players in order of id (zero for those who did not receive any points)
     */
    public int[] scoreDamageLineOf(Player playerToScore) {

        //list of damages from players in order of who hit first:
        List<String> charaNamesInOrderOfFirstHit = playerToScore.charaNamesInOrderOfFirstHit();
        //convert list of names to list of players
        List<Player> playersInOrderOfFistHit = listOfPlayersFromNames(charaNamesInOrderOfFirstHit);

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
        //assign points and return them in order of player id
        return assignPoints(listOfDamagesFromPlayers, pointsToAssign, playersInOrderOfFistHit, allPlayers);
    }

    /**
     * Assign points to players from the killshot track at the end of the game and return them in order of player id
     * @return array of points just assigned to the players in order of id (zero for those who did not receive any points)
     */
    public int[] scoreKillshotTrack(){
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
        //assign points and return them in order of player id
        return assignPoints(listOfTokensFromPlayers, pointsToAssign, playersInOrderOfFistHit, allPlayers);
    }

    /**
     * Assign points to players when scoring either a damage line of a player or the killshot track, given the following parameters,
     * without considering extra points (for first blood and double kill) and return them in order of player id
     * @param listOfDamagesFromPlayers - list of tokens on the object to score, ordered by idPlayer
     * @param pointsToAssign - array of points to assign, ordered by grater value
     * @param playersInOrderOfFistHit - list of players ordered by first hit (the players without tokens on the object we are scoring
     *                                will not be present in this list)
     * @param allPlayers - array of all players, ordered by id
     * @return array of points just assigned to the players in order of id (zero for those who did not receive any points)
     */
    private int[] assignPoints
            (List<Integer> listOfDamagesFromPlayers, int[] pointsToAssign, List<Player> playersInOrderOfFistHit, Player[] allPlayers){

        //array to return
        int[] pointsAssignedInOrderOfId = new int[allPlayers.length];
        //at first, every cell is set to zero

        int idIndex;
        //now assign the points to the players
        for(int i=0; max(listOfDamagesFromPlayers) > 0; i++) {
            idIndex = IdOfThePlayerWhoHitFirstBetweenThese(indexesOfGreaterValue(listOfDamagesFromPlayers), playersInOrderOfFistHit);
            //assign points to this player

            allPlayers[idIndex].addPointsToScore(pointsToAssign[i]);
            //set it to 0
            listOfDamagesFromPlayers.set(idIndex, 0);
            //add these points to the array to return at the correct index
            pointsAssignedInOrderOfId[idIndex] = pointsToAssign[i];
        }

        return pointsAssignedInOrderOfId;
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

    /**
     * If one of the ammo card deck or the powerup deck is empty, reset that deck using the
     * respective deck of discarded cards.
     */
    public void resetDecksIfNecessary(){
        resetDeckIfNecessary(getGameTable().getAmmoDeck(), getGameTable().getAmmoDiscarded());
        resetDeckIfNecessary(getGameTable().getPowerUpDeck(), getGameTable().getPowerUpDiscarded());
    }

    /**
     * If the given deck is empty, rest it shuffling its discarded cards.
     * @param deck
     * @param discardedCards
     */
    private void resetDeckIfNecessary(Deck deck, List discardedCards){
        if(deck.getCards().isEmpty()){
            deck.getCards().addAll(discardedCards);
            deck.shuffle();
            discardedCards.clear();
        }
    }
}
