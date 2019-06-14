package it.polimi.ProgettoIngSW2019.model.weapon_effects;

import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.common.enums.AreaOfEffect;
import it.polimi.ProgettoIngSW2019.custom_exception.EnemySizeLimitExceededException;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;

import java.util.List;

public class SameRoomEffect extends WeaponEffect{
    /**
     * Constructor class
     * Read the weapon effect file and associate to paramater
     *
     * @param jsonObj
     * @suthor: Luca Iovine
     */
    public SameRoomEffect(JsonObject jsonObj, DistanceDictionary distance) {
        super(jsonObj, distance);
    }

    /**
     * It assert that the enemy chosen by the user are actually a correct selection.
     *
     * @param weaponUser player who used the weapon
     * @param enemyChosenList list of player chosen by the user to be hitted
     * @return true if the enemy list is good to go, false otherwise
     * @throws EnemySizeLimitExceededException whenever the enemy size list is greater than expected
     * @suthor: Luca Iovine
     */
    //TESTED --> checkValidityEnemySameRoomTest
    @Override
    public boolean checkValidityEnemy(Player weaponUser, List<Player> enemyChosenList) throws EnemySizeLimitExceededException {
        boolean result;

        try {
            result  = super.checkValidityEnemy(weaponUser, enemyChosenList);
        } catch (EnemySizeLimitExceededException e) {
            throw e;
        }

        if(result){
            Player playerToCheckRoom = enemyChosenList.get(0);
            List<Player> sameRoomEnemyList = getPlayerInTheArea(playerToCheckRoom, AreaOfEffect.SAME_ROOM);

            if(!enemyChosenList.containsAll(sameRoomEnemyList))
                result = false;
        }

        return result;
    }
}
