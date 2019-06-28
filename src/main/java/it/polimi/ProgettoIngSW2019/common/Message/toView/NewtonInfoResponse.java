package it.polimi.ProgettoIngSW2019.common.Message.toView;

import java.util.ArrayList;
import java.util.List;

public class NewtonInfoResponse extends InfoResponse {
    private List<EnemyInfo> enemyInfoMovement;


    public NewtonInfoResponse(int idPlayer, List<EnemyInfo> enemyInfoMovement) {
        super(idPlayer);
        this.enemyInfoMovement = enemyInfoMovement;
    }


    public List<EnemyInfo> getEnemyInfoMovement() {
        return enemyInfoMovement;
    }
}
