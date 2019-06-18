package it.polimi.ProgettoIngSW2019.model;

/**
 * Class Session
 *
 * @author: Luca Iovine
 */
public class Session {
    private String username;
    private String pwd;
    private String hostname;

    /**
     * Constructor
     * Assign the param to class attribute
     *
     * @param username string value for username
     * @param pwd one time password used to login first time and reconnect
     * @param hostname string value for the session key
     * @author: Luca Iovine
     */
    public Session(String username, String pwd, String hostname){
        this.username = username;
        this.pwd = pwd;
        this.hostname = hostname;
    }

    /**
     * @return the username
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    public String getUsername() {
        return username;
    }

    /**
     * @return the session key
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    public String gethostname() {
        return hostname;
    }

    /**
     * @return the password
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    public String getPassword() {
        return pwd;
    }
}
