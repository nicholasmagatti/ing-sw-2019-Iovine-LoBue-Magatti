package it.polimi.ProgettoIngSW2019.model;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ProgettoIngSW2019.common.enums.*;

/**
 * Class used to represent the game table and everything on it
 * @author Nicholas Magatti
 */
public class GameTable{

    private Player[] players;
    //TODO: delete numberOfPlayers from uml
    private int activePlayers;
    //the decks are sill not shuffled: they will be shuffled in the constructor
    private Deck weaponDeck;
    private Deck powerUpDeck;
    private Deck ammoDeck;
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
     * Set the map and the number of skulls(spaces available on the killshotTrack for tokens) as the first user requested and
     * also shuffle the three decks on the table
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

        DeckFactory deckFactory = new DeckFactory();
        weaponDeck = new Deck(DeckType.WEAPON_CARD, deckFactory);
        powerUpDeck = new Deck(DeckType.POWERUP_CARD, deckFactory);
        ammoDeck = new Deck(DeckType.AMMO_CARD, deckFactory);

        weaponDeck.shuffle();
        powerUpDeck.shuffle();
        ammoDeck.shuffle();

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
        int numberOfPlayers = listOfNames.size();
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
     * @deprecated //TODO delete this but ONLY when I am sure that both my team mates removed it everywhere (in the code they haven't committed yet)
     * @return number of the players on the board
     */
    public int getNumberOfPlayers() {
        return players.length;
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
     * Add token on killshot track, after a player has been put down.
     * This method should be called when the player still has the damage on his/her damage line.
     * @param deadPlayer
     * @param killer
     */
    public void addTokenOnKillshotTrack(Player deadPlayer, Player killer){

        KillToken token;
        final int killDmg = 11;
        final int overkillDmg = 12;

        if(deadPlayer.getDamageLine().size() == overkillDmg){
            token = new KillToken(killer.getCharaName(), true);
        }
        else{
            if(deadPlayer.getDamageLine().size() == killDmg){
                token = new KillToken(killer.getCharaName(), false);
            }
            else {
                throw new RuntimeException("You are trying to use the method addTokenOnKillshotTrack using a dead player that has a number of damage smaller or greater that the number of damage for kill or overkill.");
            }
        }
        killshotTrack.add(token);
    }

    /**
     * Get list of character names in order of first hit on the killshot track
     * @return list of character names in order of first hit on the killshot track
     */
    //TODO: add to uml
    public List<String> namesOnKillshotTrackInOrderOfFirstHit(){
        List<String> list = new ArrayList<>();
        String tmp;
        for(KillToken element : killshotTrack){
            tmp = element.getCharacterName();
            if(!list.contains(tmp)){
                list.add(tmp);
            }
        }
        return list;
    }

    /**
     * Number of tokens on the killshot track belonging to the specified player,
     * considering that a KillToken with overkill worths as two tokens.
     * @param player - player whose this tokens belongs to
     * @return
     */
    //TODO: add to uml
    public int tokensOnKillshotTrackBelongingTo(Player player){
        int nrTokens = 0;
        for(KillToken element : killshotTrack){
            if(player.getCharaName().equals(element.getCharacterName())){
                if(element.isOverkill()){
                    nrTokens += 2;
                }
                else {
                    nrTokens += 1;
                }
            }
        }
        return nrTokens;
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
