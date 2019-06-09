package it.polimi.ProgettoIngSW2019.model.weapon_effects;

import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.common.enums.AreaOfEffect;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;

import java.util.List;

public class SameSquareEffect extends WeaponEffect {
    /**
     * Constructor class
     * Read the weapon effect file and associate to paramater
     *
     * @param jsonObj
     * @suthor: Luca Iovine
     */
    public SameSquareEffect(JsonObject jsonObj, DistanceDictionary distance) {
        super(jsonObj, distance);
    }

    @Override
    public boolean checkValidityEnemy(Player weaponUser, List<Player> enemyChosenList) {
        boolean result = super.checkValidityEnemy(weaponUser, enemyChosenList);
        if(result){
            Square squareToCheckSameSquare = enemyChosenList.get(0).getPosition();
            List<Player> sameSquareEnemyList = getEnemyList(squareToCheckSameSquare, AreaOfEffect.SAME_SQUARE);

            for(Player enemy: enemyChosenList){
                if(!sameSquareEnemyList.contains(enemy)){
                    result = false;
                    break;
                }
            }
        }

        return result;
    }
}
