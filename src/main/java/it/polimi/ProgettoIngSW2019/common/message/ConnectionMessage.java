package it.polimi.ProgettoIngSW2019.common.message;

public class ConnectionMessage {
    String playerName;

    public ConnectionMessage(String playerName){
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }
}
