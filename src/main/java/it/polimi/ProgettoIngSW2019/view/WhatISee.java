package it.polimi.ProgettoIngSW2019.view;

import it.polimi.ProgettoIngSW2019.common.LightModel.*;

import java.util.List;

/**
 * Class that contains what the client can see
 * @author Nicholas Magatti
 */
public class WhatISee {

    private int id;
    private String nickname;

    private PlayerDataLM[] players;
    private MyLoadedWeaponsLM myLoadedWeapons;
    private MyPowerUpLM myPowerUps;
    private MapLM map;
    //TODO: attribute for the killshot track


    public WhatISee(int id, String nickname, int numberOfPlayers){
        this.id = id;
        this.nickname = nickname;
        players = new PlayerDataLM[numberOfPlayers];
    }


}
