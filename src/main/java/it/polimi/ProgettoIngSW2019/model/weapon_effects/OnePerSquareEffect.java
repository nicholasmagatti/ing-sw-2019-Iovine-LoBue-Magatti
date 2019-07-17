package it.polimi.ProgettoIngSW2019.model.weapon_effects;

import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.custom_exception.EnemySizeLimitExceededException;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;

import java.util.List;

/**
 * Effects that regard one target per square
 * @author Luca Iovine
 */
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

    /**
     * It assert that the enemy chosen by the user are actually a correct selection.
     *
     * @param weaponUser player who used the weapon
     * @param enemyChosenList list of player chosen by the user to be hitted
     * @return true if the enemy list is good to go, false otherwise
     * @throws EnemySizeLimitExceededException whenever the enemy size list is greater than expected
     * @suthor: Luca Iovine
     */
    /*
        TESTED --> generalCheckEnemyValidityTest
                   generalCheckEnemyValidityWrongEnemyTest
     */
    @Override
    public boolean checkValidityEnemy(Player weaponUser, List<Player> enemyChosenList) throws EnemySizeLimitExceededException {
        boolean result;

        try {
            result = super.checkValidityEnemy(weaponUser, enemyChosenList);
        }catch(EnemySizeLimitExceededException e){
            throw e;
        }

        if(result){
            Player playerToCheckOnePerSquare = enemyChosenList.get(0);
            List<Player> enemyList = getEnemyList(playerToCheckOnePerSquare);

            for(int i = 0; i < enemyChosenList.size(); i++){
                for(int j = i+1; j < enemyChosenList.size(); j++){
                    if(enemyChosenList.get(i).getPosition().equals(enemyChosenList.get(j).getPosition())){
                        result = false;
                        break;
                    }
                }
            }
        }

        return result;
    }
}
