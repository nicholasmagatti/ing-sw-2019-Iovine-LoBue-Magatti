package it.polimi.ProgettoIngSW2019.model.weapon_effects;

import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.common.enums.AreaOfEffect;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Effect of the vortex
 * @author Luca Iovine
 */
public class VortexEffect extends WeaponEffect {
    /**
     * Constructor class
     * Read the weapon effect file and associate to parameter
     *
     * @param jsonObj
     * @suthor: Luca Iovine
     */
    public VortexEffect(JsonObject jsonObj, DistanceDictionary distance) {
        super(jsonObj, distance);
    }

    @Override
    public List<Player> getEnemyList(Player fromPlayer) {
        List<Player> enemyList = new ArrayList<>();
        List<Square> possibleVortexSpawn = distance.getTargetPosition(AreaOfEffect.CAN_SEE_ATLEAST_ONE, fromPlayer.getPosition());
        List<Square> vortexSurrandedArea;

        for(Square vortexSpawn: possibleVortexSpawn){
            vortexSurrandedArea = distance.getTargetPosition(AreaOfEffect.UP_TO_ONE, vortexSpawn);
            for(Square vortexNearSquare: vortexSurrandedArea){
                if(!enemyList.containsAll(vortexNearSquare.getPlayerOnSquare()))
                    enemyList.addAll(vortexNearSquare.getPlayerOnSquare());
            }
        }

        if(enemyList.contains(fromPlayer))
            enemyList.remove(fromPlayer);

        return enemyList;
    }

    /**
     * Get the list of the square the user can see at least distante one square and also the squares that surrounds the target
     */
    @Override
    public List<Square> getMovementList(Player player, Player enemy) {
        /*
            Semplicemente è l'intersezione tra ciò che lo user può vedere ad almeno un movimento di distanza
            ed i quadrati che circondano il nemico stesso.
         */
        List<Square> possibleEnemyMovement = new ArrayList<>();
        List<Square> possibleVortexSpawn = distance.getTargetPosition(AreaOfEffect.CAN_SEE_ATLEAST_ONE, player.getPosition());
        List<Square> enemySurrondedArea = distance.getTargetPosition(AreaOfEffect.UP_TO_ONE, enemy.getPosition());

        for(Square vortexSpawn: possibleVortexSpawn){
            if(enemySurrondedArea.contains(vortexSpawn))
                possibleEnemyMovement.add(vortexSpawn);
        }

        return possibleEnemyMovement;
    }
}
