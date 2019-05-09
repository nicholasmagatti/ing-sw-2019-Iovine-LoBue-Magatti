package it.polimi.ProgettoIngSW2019.common.message;

public class ConnectionMessage {
    private String playerName;
    private String ip;

    public ConnectionMessage(String playerName, String ip){
        this.playerName = playerName;
        this.ip = ip;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getIp() {
        return ip;
    }
}
