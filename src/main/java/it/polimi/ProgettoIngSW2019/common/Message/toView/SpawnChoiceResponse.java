package it.polimi.ProgettoIngSW2019.common.Message.toView;

import it.polimi.ProgettoIngSW2019.common.LightModel.MapLM;
import it.polimi.ProgettoIngSW2019.common.LightModel.PlayerDataLM;
import it.polimi.ProgettoIngSW2019.common.LightModel.PowerUpLM;

/**
 * Message with all the spawn info
 * @author Priscilla Lo Bue
 */
public class SpawnChoiceResponse extends PlayerChoiceResponse {
    private PowerUpLM powerUpDiscardedInfo;
    private MapLM mapLM;
    private PlayerDataLM spawnPlayerLM;


    /**
     * Constructor
     * @param idPlayer                  id spawn Player
     * @param powerUpDiscardedInfo      powerUp spawn player discarded
     * @param mapLM                     updated map LM
     * @param spawnPlayerLM             spawn player
     */
    public SpawnChoiceResponse(int idPlayer, PowerUpLM powerUpDiscardedInfo, MapLM mapLM, PlayerDataLM spawnPlayerLM) {
        super(idPlayer);

        if(powerUpDiscardedInfo == null)
            throw new NullPointerException("PowerUp cannot be null");

        if(mapLM == null)
            throw new NullPointerException("MapLM cannot be null");

        if(spawnPlayerLM == null)
            throw new NullPointerException("spawnPlayer cannot be null");

        this.powerUpDiscardedInfo = powerUpDiscardedInfo;
        this.mapLM = mapLM;
        this.spawnPlayerLM = spawnPlayerLM;
    }


    /**
     * get updated map LM
     * @return  map LM
     */
    public MapLM getMapLM() {
        return mapLM;
    }


    /**
     * get the powerUp card discarded info
     * @return  powerUpLM
     */
    public PowerUpLM getPowerUpDiscardedInfo() {
        return powerUpDiscardedInfo;
    }


    /**
     * get the spawn Player LM
     * @return  player LM
     */
    public PlayerDataLM getSpawnPlayerLM() {
        return spawnPlayerLM;
    }
}
