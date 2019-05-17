package it.polimi.ProgettoIngSW2019.common.Message;

import it.polimi.ProgettoIngSW2019.common.LightModel.EnemyPlayerLM;

import java.util.List;

/**
 * ShootChoice class
 * @author Priscilla Lo Bue
 */
public class ShootChoice {
    private int idWeaponUsed;
    private List<EnemyPlayerLM> enemyList;
    private boolean usePowerUp;



    /**
     * get the id of the weapon used
     * @return  id weapon used
     */
    public int getIdWeaponUsed() {
        return idWeaponUsed;
    }




    /**
     * get the enemy list
     * @return  enemy list
     */
    public List<EnemyPlayerLM> getEnemyList() {
        return enemyList;
    }


    /**
     * get if the player can use the powerUp
     * @return  can use powerUp
     */
    public boolean canUsePowerUp() {
        return usePowerUp;
    }
}
