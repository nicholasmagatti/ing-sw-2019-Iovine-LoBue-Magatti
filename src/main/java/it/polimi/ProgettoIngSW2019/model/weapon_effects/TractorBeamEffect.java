package it.polimi.ProgettoIngSW2019.model.weapon_effects;

import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.common.enums.AreaOfEffect;
import it.polimi.ProgettoIngSW2019.custom_exception.EnemySizeLimitExceededException;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;

import javax.naming.SizeLimitExceededException;
import java.util.List;

public class TractorBeamEffect extends WeaponEffect {
    List<Square> positionList;
    Square chosenPosition;

    /**
     * Constructor class
     * Read the weapon effect file and associate to paramater
     *
     * @param jsonObj
     * @suthor: Luca Iovine
     */
    public TractorBeamEffect(JsonObject jsonObj, DistanceDictionary distance) {
        super(jsonObj, distance);
    }

    /**
     * It activate the effect of the weapon doing damage and marking the player in the enemyChosenList,
     * which has to be one.
     * After the damage it move the enemy in a position up to 2 movement away from the user based on its choice.
     *
     * @param player user player
     * @param enemyList list of the enemy chosen from the user
     * @throws SizeLimitExceededException whenever the enemy size list is greater than expected
     * @suthor: Luca Iovine
     */
    //NOT TO BE TESTED
    @Override
    public void activateEffect(Player player, List<Player> enemyList) throws EnemySizeLimitExceededException{
        if(enemyList.size() <= 1) {
             super.activateEffect(player, enemyList);
             enemyList.get(0).moveTo(chosenPosition);
        }else{
            throw new EnemySizeLimitExceededException();
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
        positionList = distance.getTargetPosition(AreaOfEffect.UP_TO_TWO, player.getPosition());

        return positionList;
    }

    /**
     * It assert that the movement that the user or the enemy has to do is actually a correct selection.
     *
     * @param chosenPosition position where the user/enemy should go
     * @param enemyList is the enemy hitted by the weapon. It must be one target only
     * @return true if the position is good to go, false otherwise
     * @suthor: Luca Iovine
     */
    /*
        TESTED --> checkMovementTractorBeam
                   checkWRONGMovementTractorBeam

     */
    @Override
    public boolean checkValidityMoveEnemy(Square chosenPosition, List<Player> enemyList) throws EnemySizeLimitExceededException{
        boolean result = false;

        if(enemyList.size() <= 1) {
            if (positionList.contains(chosenPosition)) {
                this.chosenPosition = chosenPosition;
                result = true;
            }
        }else
            throw new EnemySizeLimitExceededException();

        return result;
    }
}
