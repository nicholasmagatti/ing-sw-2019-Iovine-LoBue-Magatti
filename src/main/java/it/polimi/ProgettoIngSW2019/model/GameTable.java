package it.polimi.ProgettoIngSW2019.model;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ProgettoIngSW2019.common.enums.*;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;

/**
 * Class used to represent the game table and everything on it
 * @author Nicholas Magatti
 */
public class GameTable{

    private Player[] players;
    private int activePlayers;
    //the decks are sill not shuffled: they will be shuffled in the constructor
    private Deck weaponDeck;
    private Deck powerUpDeck;
    private Deck ammoDeck;
    private List<PowerUp> powerUpDiscarded = new ArrayList<>();
    private List<AmmoCard> ammoDiscarded = new ArrayList<>();
    //[][] indicate row and column respectively ([row][column])
    private Square[][] map;
    private List<KillToken> killshotTrack = new ArrayList<>();
    private int numberOfSkullsForTheGame; //chosen by the first user

    /**
     * Constructor
     * Set the map and the number of skulls(spaces available on the killshotTrack for tokens) as the first user requested and
     * also shuffle the three decks on the table
     * @param chosenMap - requested map
     * @param initialNumberOfSkulls - number of skulls requested
     * @throws IllegalArgumentException the number of skulls indicated is not in the allowed range
     */
    public GameTable(Square[][] chosenMap, int initialNumberOfSkulls) throws IllegalArgumentException{
        if(initialNumberOfSkulls < GeneralInfo.MIN_SKULLS || initialNumberOfSkulls > GeneralInfo.MAX_SKULLS){
            throw new IllegalArgumentException("The number of skulls for the game is " + initialNumberOfSkulls +
                    " but should be between" + GeneralInfo.MIN_SKULLS + " and " + GeneralInfo.MAX_SKULLS + ".");
        }
        numberOfSkullsForTheGame = initialNumberOfSkulls;

        map = chosenMap;
        //set dependencies for every square of the map
        for(int i = 0; i < GeneralInfo.ROWS_MAP; i++){
            for(int j=0; j < GeneralInfo.COLUMNS_MAP; j++){
                if(map[i][j] != null){
                    map[i][j].setDependency(map);
                }
            }
        }

        DeckFactory deckFactory = new DeckFactory(new DistanceDictionary(map));
        weaponDeck = new Deck(DeckType.WEAPON_CARD, deckFactory);
        powerUpDeck = new Deck(DeckType.POWERUP_CARD, deckFactory);
        ammoDeck = new Deck(DeckType.AMMO_CARD, deckFactory);
        weaponDeck.shuffle();
        powerUpDeck.shuffle();
        ammoDeck.shuffle();

        setCardsOnGameTable();
    }

    /**
     * Set the players for the game, before its begin
     * @param sessions - list of objects that contain the data to insert in the players to create (name and hostname)
     */
    public void setPlayersBeforeStart(List<Session> sessions) {
        int numberOfPlayers = sessions.size();
        activePlayers = numberOfPlayers;
        players = new Player[numberOfPlayers];
        for(int i=0; i < numberOfPlayers; i++){
            String username = sessions.get(i).getUsername();
            String hostname = sessions.get(i).gethostname();
            players[i] = new Player(i, username, this, hostname);
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
    public List<PowerUp> getPowerUpDiscarded() {
        return powerUpDiscarded;
    }

    /**
     * Get the list of discarded ammo cards
     * @return list of discarded ammo cards
     */
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
        return killshotTrack.size() >= numberOfSkullsForTheGame;
    }

    /**
     * Get the initial number of skulls chosen for this game
     * @return initial number of skulls chosen for this game
     */
    public int initialNumberOfSkulls() {
        return numberOfSkullsForTheGame;
    }


    /**
     * Add token on killshot track, after a player has been put down.
     * This method should be called when the player still has the damage on his/her damage line.
     * could not have if they were actually dead
     * @param deadPlayer
     * @param killer
     * @throws RuntimeException when the player declared as dead has a number of damages that they
     */
    public void addTokenOnKillshotTrack(Player deadPlayer, Player killer) throws RuntimeException{

        KillToken token;
        final int DAMAGE_TO_KILL = 11;
        final int DAMAGE_TO_OVERKILL = 12;

        if(deadPlayer.getDamageLine().size() == DAMAGE_TO_OVERKILL){
            token = new KillToken(killer.getCharaName(), true);
        }
        else{
            if(deadPlayer.getDamageLine().size() == DAMAGE_TO_KILL){
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
     * considering that a KillToken with overkill counts as two tokens.
     * @param player - player whose this tokens belongs to
     * @return
     */
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
    void increaseNumberOfActivePlayers(){
        activePlayers++;
    }

    /**
     * Decrease (of one) the number of active players
     */
    void decreaseNumberOfActivePlayers(){
        activePlayers--;
    }

    /**
     * Set the cards on the table before starting: the weapons on the
     * spawn points and the ammo cards on the spawn points.
     * @throws RuntimeException if the type of a square on the game table is not acceptable
     */
    void setCardsOnGameTable() throws RuntimeException{
        //for each square that is not null
        for(Square[]row : map) {
            for(Square square : row) {
                if (square != null) {
                    Deck respectiveDeck;
                    switch (square.getSquareType()){
                        case SPAWNING_POINT:
                            respectiveDeck = weaponDeck;
                            break;
                        case AMMO_POINT:
                            respectiveDeck = ammoDeck;
                            break;
                            default:
                                throw new RuntimeException("This square is not a spawn point or a ammo point but not even null. This should never happened.");
                    }
                    square.reset(respectiveDeck);

                }
            }
        }
    }
}
