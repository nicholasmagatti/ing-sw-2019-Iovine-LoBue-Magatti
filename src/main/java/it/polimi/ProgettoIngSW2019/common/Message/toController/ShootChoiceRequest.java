package it.polimi.ProgettoIngSW2019.common.Message.toController;

/**
 * ShootChoiceRequest class
 * @author Priscilla Lo Bue
 */
public class ShootChoiceRequest extends PlayerChoiceRequest {
    private int idWeaponUsed;
    //private List<EnemyPlayerLM> enemyList;
    private boolean usePowerUp;


    /**
     * Constructor for view
     * @param idPlayer          id player
     * @param idWeaponUsed      id weapon used
     */
    public ShootChoiceRequest(int idPlayer, int idWeaponUsed) {
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
