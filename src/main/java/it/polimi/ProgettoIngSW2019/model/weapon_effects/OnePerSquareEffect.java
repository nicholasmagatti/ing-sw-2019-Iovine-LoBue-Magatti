package it.polimi.ProgettoIngSW2019.model.weapon_effects;

import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;

import java.util.List;

public class OnePerSquareEffect extends WeaponEffect {
    /**
     * Constructor class
     * Read the weapon effect file and associate to paramater
     *
     * @param jsonObj
     * @suthor: Luca Iovine
     */
    public OnePerSquareEffect(JsonObject jsonObj, DistanceDictionary distance) {
        super(jsonObj, distance);
    }

    @Override
    public boolean checkValidityEnemy(Player weaponUser, List<Player> enemyChosenList) {
        boolean result = super.checkValidityEnemy(weaponUser, enemyChosenList);
        if(result){
            Square squareToCheckOnePerSquare = enemyChosenList.get(0).getPosition();
            List<Player> enemyList = getEnemyList(squareToCheckOnePerSquare);

            for(Player enemy: enemyChosenList){
                for(Player e: enemyList){
                    if(!enemy.equals(e)){
                        if(enemy.getPosition().equals(e.getPosition())){
                            result = false;
                            break;
                        }
                    }
                }
            }
        }

        return result;
    }
}
