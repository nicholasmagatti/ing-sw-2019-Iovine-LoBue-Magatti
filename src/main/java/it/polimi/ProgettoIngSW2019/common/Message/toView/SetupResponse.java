package it.polimi.ProgettoIngSW2019.common.Message.toView;

import it.polimi.ProgettoIngSW2019.common.LightModel.*;

public class SetupResponse {
    String startingPlayerUsername;
    String myHostname;
    int myId;
    PlayerDataLM[] players;
    MyLoadedWeaponsLM myLoadedWeaponsLM;
    MyPowerUpLM myPowerUpLM;
    MapLM map;
    KillshotTrackLM killshotTrack;

    public SetupResponse(String startingPlayerUsername, int myId, PlayerDataLM[] players,
                         MapLM map, KillshotTrackLM killshotTrack, String myHostnamem,
                         MyLoadedWeaponsLM myLoadedWeaponsLM, MyPowerUpLM myPowerUpLM){
        this.startingPlayerUsername = startingPlayerUsername;
        this.myId = myId;
        this.players = players;
        this.map = map;
        this.killshotTrack = killshotTrack;
        this.myHostname = myHostnamem;
        this.myLoadedWeaponsLM = myLoadedWeaponsLM;
        this.myPowerUpLM = myPowerUpLM;
    }

    public String getStartingPlayerUsername() {
        return startingPlayerUsername;
    }

    public int getMyId() {
        return myId;
    }

    public KillshotTrackLM getKillshotTrack() {
        return killshotTrack;
    }

    public MapLM getMap() {
        return map;
    }

    public PlayerDataLM[] getPlayers() {
        return players;
    }

    public MyLoadedWeaponsLM getMyLoadedWeaponsLM() {
        return myLoadedWeaponsLM;
    }

    public MyPowerUpLM myPowerUpLM() {
        return myPowerUpLM;
    }

    public String getMyHostname() {
        return myHostname;
    }
}
