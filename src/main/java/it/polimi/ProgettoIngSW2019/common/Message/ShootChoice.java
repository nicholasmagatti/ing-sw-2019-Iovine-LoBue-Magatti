package it.polimi.ProgettoIngSW2019.common.Message;

import java.util.List;

/**
 * ShootChoice class
 * @author Priscilla Lo Bue
 */
public class ShootChoice extends PlayerChoice{
    private int idWeaponUsed;
    //private List<EnemyPlayerLM> enemyList;
    private boolean usePowerUp;


    /**
     * Constructor for view
     * @param idPlayer          id player
     * @param idWeaponUsed      id weapon used
     */
    public ShootChoice(int idPlayer, int idWeaponUsed) {
        super(idPlayer);
        if(idWeaponUsed < 0)
            throw new IllegalArgumentException("The weapon id cannot be negative");
        this.idWeaponUsed = idWeaponUsed;
    }


    //TODO: constructor for controller?


    /**
     * get the id of the weapon used
     * @return  id weapon used
     */
    public int getIdWeaponUsed() {
        return idWeaponUsed;
    }


    /**
     * get if the player can use the powerUp
     * @return  can use powerUp
     */
    public boolean canUsePowerUp() {
        return usePowerUp;
    }
}
