package it.polimi.ProgettoIngSW2019.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class LoginHandler {
    int playerId = 0;
    private final GameTable gameTable;
    private final List<Player> playerConnected;
    //Value pair: <username, session>
    private final HashMap<String, Session> sessions;
    //Value pair: <username, hostname>
    private final HashMap<String, String> hostnames;
    //Value pair: <username, player>
    private final HashMap<String, Player> players;

    /**
     * Constructor
     *
     * @param gameTable instance of "GameTable" to have access to all the game resources
     * @author: Luca Iovine
     */
    public LoginHandler(GameTable gameTable){
        sessions = new HashMap<>();
        hostnames = new HashMap<>();
        players = new HashMap<>();
        playerConnected = new ArrayList<>();
        this.gameTable = gameTable;
    }

    /**
     * Check if a certain user is alredy connected to the server.
     * It use the username and the hostname as identifier.
     *
     * @param username username of the player
     * @param hostname hostname of the player's client
     * @return true if alredy logged, false otherwise
     * @author: Luca Iovine
     */
    public boolean isLogged(String username, String hostname){
        if(hostnames.containsKey(username)) {
            if (hostnames.get(username).equals(hostname))
                return true;
        }

        return false;
    }

    /**
     * @param username of the player
     * @return the session associate to a player
     * @author: Luca Iovine
     */
    public Session getSession(String username){
        return sessions.get(username);
    }

    /**
     * Create a new session when a client connect to the server
     *
     * @param username of the player
     * @param hostname of the player's client
     * @return session created
     * @author: Luca Iovine
     */
    public Session newSession(String username, String hostname){
        hostnames.put(username, hostname);
        Session session = new Session(username, generateSessionKey());
        sessions.put(username, session);

        createNewPlayer(username);

        return session;
    }

    /**
     * Create a random session key to associate it to a player's client
     *
     * @return the string conatining the session key
     * @author: Luca Iovine
     */
    private String generateSessionKey(){
        char[] chars = ("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789").toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            sb.append(chars[random.nextInt(chars.length)]);
        }
        return sb.toString();
    }

    /**
     * Create a new player when the client connect succesfully to the server
     *
     * @param username of the player
     * @author: Luca Iovine
     */
    private void createNewPlayer(String username){
        Player p = new Player(playerId, username, gameTable);
        incrementPlayerId();

        players.put(username, p);
        playerConnected.add(p);
    }

    /**
     * Increment the id.
     *
     * @return player id
     */
    private int incrementPlayerId(){
        return playerId++;
    }

    /**
     * @param username of the player
     * @return the instance of "Player" associated to the username
     */
    public Player getPlayerByUsername(String username){
        return players.get(username);
    }

    /**
     * Remove the player from the list that keep track of player currently connected
     *
     * @param player to remove
     */
    public void disconnectPlayer(Player player){
        playerConnected.remove(player);
    }

    /**
     * Check if the username is aviable before create a new session
     *
     * @param username of the player
     * @return true if the name can be used, false otherwise
     */
    public boolean isUsernameAviable(String username){
        if(sessions.containsKey(username))
            return false;
        else
            return true;
    }
}
