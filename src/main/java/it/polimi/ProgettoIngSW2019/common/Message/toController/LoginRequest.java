package it.polimi.ProgettoIngSW2019.common.Message.toController;

public class LoginRequest {
    String username;
    String password;
    String hostname;

    public LoginRequest(String username, String password, String hostname){
        this.username = username;
        this.password = password;
        this.hostname = hostname;
    }
    /**
     * @return the user's username chosen
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the user's password chosen
     */
    public String getpassword() {
        return password;
    }

    /**
     * @return the user's computer hostname
     */
    public String getHostname() {
        return hostname;
    }
}
