package it.polimi.ProgettoIngSW2019.common.Message.toController;

import java.util.List;

/**
 * ShootChoiceRequest class
 * @author Luca Iovine
 */
public class ShootChoiceRequest extends PlayerChoiceRequest {
    private int idWeaponUsed;
    private List<Integer> enemyChosenListId;
    private int[] positionChosen;


    /**
     * Constructor for view
     * @param idPlayer          id player
     * @param idWeaponUsed      id weapon used
     */
    public ShootChoiceRequest(int idPlayer, int idWeaponUsed, List<Integer> enemyChosenListId, int[] positionChosen) {
        super(idPlayer);
        if (idWeaponUsed < 0)
            throw new IllegalArgumentException("The weapon id cannot be negative");
        this.idWeaponUsed = idWeaponUsed;
        this.enemyChosenListId = enemyChosenListId;
        this.positionChosen = positionChosen;

    }

    /**
     * get the id of the weapon used
     * @return  id weapon used
     */
    public int getIdWeaponUsed() {
        return idWeaponUsed;
    }

    public List<Integer> getEnemyChosenListId() {
        return enemyChosenListId;
    }

    public int[] getPositionChosen() {
        return positionChosen;
    }
}
