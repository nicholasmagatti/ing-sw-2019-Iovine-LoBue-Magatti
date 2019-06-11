package it.polimi.ProgettoIngSW2019.view;

import it.polimi.ProgettoIngSW2019.common.LightModel.*;

import java.util.List;

/**
 * Class that contains what the client can see
 * @author Nicholas Magatti
 */
public class InfoOnView {

    private int myId;
    private String myNickname;

    private PlayerDataLM[] players;
    private MyLoadedWeaponsLM myLoadedWeapons;
    private MyPowerUpLM myPowerUps;
    private MapLM map;
    private KillshotTrackLM killshotTrack;


    public InfoOnView(int id, String nickname, int numberOfPlayers){
        myId = id;
        myNickname = nickname;
        players = new PlayerDataLM[numberOfPlayers];
    }

    public int getMyId() {
        return myId;
    }
}
