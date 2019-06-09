package it.polimi.ProgettoIngSW2019.model.weapon_effects;

import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.common.enums.AreaOfEffect;
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

    @Override
    public boolean checkValidityEnemy(Player weaponUser, List<Player> enemyChosenList) {
        boolean result = super.checkValidityEnemy(weaponUser, enemyChosenList);
        if(result){
            Square squareToCheckRoom = enemyChosenList.get(0).getPosition();
            List<Player> sameRoomEnemyList = getEnemyList(squareToCheckRoom, AreaOfEffect.SAME_ROOM);

            for(Player enemy: enemyChosenList){
                if(!sameRoomEnemyList.contains(enemy)){
                    result = false;
                    break;
                }
            }
        }

        return result;
    }
}
