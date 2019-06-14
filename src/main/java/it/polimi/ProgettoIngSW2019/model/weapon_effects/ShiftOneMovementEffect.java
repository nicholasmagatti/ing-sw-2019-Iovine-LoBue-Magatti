package it.polimi.ProgettoIngSW2019.model.weapon_effects;

import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.common.enums.AreaOfEffect;
import it.polimi.ProgettoIngSW2019.custom_exception.EnemySizeLimitExceededException;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;

import javax.naming.SizeLimitExceededException;
import java.util.List;

public class ShiftOneMovementEffect extends WeaponEffect {
    Square chosenPosition;
    /**
     * Constructor class
     * Read the weapon effect file and associate to paramater
     *
     * @param jsonObj
     * @suthor: Luca Iovine
     */
    public ShiftOneMovementEffect(JsonObject jsonObj, DistanceDictionary distance) {
        super(jsonObj, distance);
    }

    /**
     * It activate the effect of the weapon doing damage and marking the player in the enemyChosenList,
     * which has to be one.
     * After the damage it move the enemy in a position up to 1 movement away from the enemy itself.
     *
     * @param player user player
     * @param enemyList list of the enemy chosen from the user
     * @throws SizeLimitExceededException whenever the enemy size list is greater than expected
     * @suthor: Luca Iovine
     */
    //NOT TO BE TESTED
    @Override
    public void activateEffect(Player player, List<Player> enemyList) throws SizeLimitExceededException{
        if(enemyList.size() <= 1) {
            try {
                super.activateEffect(player, enemyList);
                enemyList.get(0).moveTo(chosenPosition);
            }catch(SizeLimitExceededException e){
                throw e;
            }
        }else{
            throw new SizeLimitExceededException();
        }
    }

    /**
     * @param player who has to move
     * @return the list of position the player can move
     * @suthor: Luca Iovine
     */
    //NOT TO BE TESTED
    @Override
    public List<Square> getMovementList(Player player, Player enemy) {
        List<Square> positionList = distance.getTargetPosition(AreaOfEffect.UP_TO_ONE, enemy.getPosition());

        return positionList;
    }

    /**
     * It assert that the movement that the enemy has to do is actually a correct selection.
     *
     * @param chosenPosition position where the user/enemy should go
     * @param enemyList is the enemy hitted by the weapon. It must be one target only
     * @return true if the position is good to go, false otherwise
     * @suthor: Luca Iovine
     */
    /*
        TESTED --> checkValidityMoveEnemyTest
                   checkValidityMoveEnemyWrongTest
     */
    @Override
    public boolean checkValidityMoveEnemy(Square chosenPosition, List<Player> enemyList) throws EnemySizeLimitExceededException {
        boolean result = false;
        if(enemyList.size() <= 1) {
            List<Square> positionList = distance.getTargetPosition(AreaOfEffect.UP_TO_ONE, enemyList.get(0).getPosition());

            if (positionList.contains(chosenPosition)) {
                this.chosenPosition = chosenPosition;
                result = true;
            }
        }else
            throw new EnemySizeLimitExceededException();

        return result;
    }
}
