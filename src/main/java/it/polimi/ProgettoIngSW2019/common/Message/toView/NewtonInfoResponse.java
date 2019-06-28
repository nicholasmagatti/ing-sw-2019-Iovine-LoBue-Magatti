package it.polimi.ProgettoIngSW2019.common.Message.toView;

import java.util.ArrayList;
import java.util.List;

/**
 * Message to view when a player wants to use Newton powerUp
 * @author Priscilla lO Bue
 */
public class NewtonInfoResponse extends InfoResponse {
    private List<EnemyInfo> enemyInfoMovement;


    /**
     * Constructor
     * @param idPlayer              idPlayer
     * @param enemyInfoMovement     list of enemy info the player can move
     */
    public NewtonInfoResponse(int idPlayer, List<EnemyInfo> enemyInfoMovement) {
        super(idPlayer);
        this.enemyInfoMovement = new ArrayList<>(enemyInfoMovement);
    }


    /**
     * get the list of enemy info the player can move in the map
     * @return      list of enemy info
     */
    public List<EnemyInfo> getEnemyInfoMovement() {
        return enemyInfoMovement;
    }
}
