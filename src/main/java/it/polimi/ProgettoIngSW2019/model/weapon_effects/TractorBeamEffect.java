package it.polimi.ProgettoIngSW2019.model.weapon_effects;

import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.common.enums.AreaOfEffect;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;

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

    @Override
    public void activateEffect(Player player, List<Player> enemyList){
        if(enemyList.size() <= 1) {
            super.activateEffect(player, enemyList);
            enemyList.get(0).moveTo(chosenPosition);
        }else{
            //TODO: deve lanciare messaggio d'errore generico (?)
        }
    }

    @Override
    public List<Square> getMovementList(Player player) {
        positionList = distance.getTargetPosition(AreaOfEffect.EXACTLY_TWO, player.getPosition());

        return positionList;
    }

    @Override
    public boolean checkValidityMoveUserPlayer(Square chosenPosition){
        boolean result = false;

        if(positionList.contains(chosenPosition)) {
            this.chosenPosition = chosenPosition;
            result = true;
        }

        return result;
    }
}
