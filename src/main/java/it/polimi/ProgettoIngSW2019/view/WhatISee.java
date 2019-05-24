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

    private List<PlayerDataLM> players;
    private MyLoadedWeaponsLM myLoadedWeapons;
    private MyPowerUpLM myPowerUps;
    //TODO: attribute for the map
    //TODO: attribute for the killshot track


    public WhatISee(int id, String nickname){
        this.id = id;
        this.nickname = nickname;
    }


}
