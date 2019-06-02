package it.polimi.ProgettoIngSW2019.common.Message.toView;

import it.polimi.ProgettoIngSW2019.common.LightModel.MyPowerUpLM;
import it.polimi.ProgettoIngSW2019.common.LightModel.PlayerDataLM;

/**
 * Spawn Draw Cards Response
 */
public class DrawCardsInfo extends InfoResponse {
    private PlayerDataLM playerLM;
    private MyPowerUpLM myPowerUpLM;


    /**
     * Constructor
     * @param idPlayer          id spawn player
     * @param playerLM     spawn playerLM
     * @param myPowerUpLM       powerUps LM spawn Player
     */
    public DrawCardsInfo(int idPlayer, PlayerDataLM playerLM, MyPowerUpLM myPowerUpLM) {
        super(idPlayer);

        if(playerLM == null)
            throw new NullPointerException("Player cannot be null");

        if(myPowerUpLM == null)
            throw new NullPointerException("myPowerUpLM cannot be null");

        this.playerLM = playerLM;
        this.myPowerUpLM = myPowerUpLM;
    }


    /**
     * get Spawn Player LM
     * @return  spawn Player
     */
    public PlayerDataLM getSpawnPlayerLM() {
        return playerLM;
    }


    /**
     * get The powerUps list LM spawn Player LM
     * @return  powerUps list LM spawn Player LM
     */
    public MyPowerUpLM getMyPowerUpLM() {
        return myPowerUpLM;
    }
}
