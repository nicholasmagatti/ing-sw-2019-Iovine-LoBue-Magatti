package it.polimi.ProgettoIngSW2019.model;

/**
 * Class Session
 *
 * @author: Luca Iovine
 */
public class Session {
    private String username;
    private String sessionKey;

    /**
     * Constructor
     * Assign the param to class attribute
     *
     * @param username string value for username
     * @param sessionKey string value for the session key
     * @author: Luca Iovine
     */
    public Session(String username, String sessionKey){
        this.username = username;
        this.sessionKey = sessionKey;
    }

    /**
     * @return the username
     * @author: Luca Iovine
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the session key
     * @author: Luca Iovine
     */
    public String getSessionKey() {
        return sessionKey;
    }
}
