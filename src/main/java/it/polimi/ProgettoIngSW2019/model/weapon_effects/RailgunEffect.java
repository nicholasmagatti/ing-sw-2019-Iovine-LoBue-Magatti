package it.polimi.ProgettoIngSW2019.model.weapon_effects;

import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.common.enums.AreaOfEffect;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;

import java.util.ArrayList;
import java.util.List;

public class RailgunEffect extends WeaponEffect {

    /**
     * Constructor class
     * Read the weapon effect configuration json file and setup the effect
     *
     * @param jsonObj
     * @param distance
     * @suthor: Luca Iovine
     */
    public RailgunEffect(JsonObject jsonObj, DistanceDictionary distance) {
        super(jsonObj, distance);
    }

    @Override
    public List<Player> getEnemyList(Player fromPlayer) {
        List<Player> enemyList = new ArrayList<>();

        List<Square> northPosition = distance.getTargetPosition(AreaOfEffect.NORTH_DIRECTION, fromPlayer.getPosition());
        List<Square> southPosition = distance.getTargetPosition(AreaOfEffect.SOUTH_DIRECTION, fromPlayer.getPosition());
        List<Square> eastPosition = distance.getTargetPosition(AreaOfEffect.EAST_DIRECTION, fromPlayer.getPosition());
        List<Square> westPosition = distance.getTargetPosition(AreaOfEffect.WEST_DIRECTION, fromPlayer.getPosition());

        enemyList.addAll(calculateEnemy(northPosition));
        enemyList.remove(fromPlayer);
        enemyList.addAll(calculateEnemy(southPosition));
        enemyList.remove(fromPlayer);
        enemyList.addAll(calculateEnemy(eastPosition));
        enemyList.remove(fromPlayer);
        enemyList.addAll(calculateEnemy(westPosition));
        enemyList.remove(fromPlayer);

        return enemyList;
    }

    private List<Player> calculateEnemy(List<Square> direction){
        List<Player> enemyInDirection = new ArrayList<>();

        for(Square s: direction)
            enemyInDirection.addAll(s.getPlayerOnSquare());

        return enemyInDirection;
    }
}
