package it.polimi.ProgettoIngSW2019.common.Message.toView;

public class MessageConnection {
    String username;
    String hostname;

    public MessageConnection(String username, String hostname){
        this.username = username;
        this.hostname = hostname;
    }

    public String getUsername() {
        return username;
    }

    public String getHostname() {
        return hostname;
    }
}
