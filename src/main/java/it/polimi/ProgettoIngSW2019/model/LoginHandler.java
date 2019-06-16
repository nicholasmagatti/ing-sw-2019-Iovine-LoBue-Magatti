package it.polimi.ProgettoIngSW2019.model;

import java.util.HashMap;
import java.util.Random;

public class LoginHandler {
    //Value pair: <username, session>
    private final HashMap<String, Session> sessions;
    private boolean gameStarted = false;
    private int nrOfPlayerConnected = 0;

    public LoginHandler(){
        sessions = new HashMap<>();
    }

    /**
     * It generate a new session based on the username and password pair.
     *
     * @param username chosen by the user
     * @param password one time password in order to reconnect
     * @author: Luca Iovine
     */

    public void generateNewLogin(String username, String password, String hostname) {
        Session s = new Session(username, password, hostname);
        sessions.put(username, s);
        nrOfPlayerConnected++;
        //TODO: check se nr giocatori >= 3 per far partire il time e se =5 per far partire la partita
    }

    /**
     * Check if the user alredy exist or not
     *
     * @param username chosen by the user
     * @return true if the user exist, false otherwise
     * @author: Luca Iovine
     */
    public boolean checkUserExist(String username){
        boolean result = false;

        if (!sessions.containsKey(username)) {
            result = true;
        }

        return result;
    }

    /**
     * @return ture if the game is started, false otherwise
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    public boolean isGameStarted() {
        return gameStarted;
    }

    /**
     * Check that the username/password is a valid combination and it's associated to the hostname
     * which have made the request.
     *
     * @param username chosen for the login
     * @param password chosen for the login
     * @param hostname of the client which sent the request
     * @return true if the parameters match, false otherwise.
     * @author: Luca Iovine
     */
    public boolean checkLoginValidity(String username, String password, String hostname){
        boolean result = false;
        Session sessionToCheck = sessions.get(hostname);

        if(sessionToCheck != null) {
            if (username.equals(sessionToCheck.getUsername()) &&
                    password.equals(sessionToCheck.getPassword()))
                result = true;
        }

        return result;
    }

    //TODO: pinger per verificare se i client che si sono già connessi sono ancora vivi
}
