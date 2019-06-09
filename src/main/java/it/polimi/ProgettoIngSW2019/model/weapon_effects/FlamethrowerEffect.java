package it.polimi.ProgettoIngSW2019.model.weapon_effects;

import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;

import java.util.List;

public class FlamethrowerEffect extends WeaponEffect {
    /**
     * Constructor class
     * Read the weapon effect file and associate to paramater
     *
     * @param jsonObj
     * @suthor: Luca Iovine
     */
    public FlamethrowerEffect(JsonObject jsonObj, DistanceDictionary distance) {
        super(jsonObj, distance);
    }

    @Override
    public boolean checkValidityEnemy(Player weaponUser, List<Player> enemyChosenList) {
        boolean result = super.checkValidityEnemy(weaponUser, enemyChosenList);
        Square northSquare = weaponUser.getPosition().getNorthSquare();
        Square southSquare = weaponUser.getPosition().getSouthSquare();
        Square eastSquare = weaponUser.getPosition().getEastSquare();
        Square westSquare = weaponUser.getPosition().getWestSquare();
        if(result){
            result = false;

            if(northSquare != null) {
                for(Player firstEnemy: northSquare.getPlayerOnSquare()) {
                    if (enemyChosenList.contains(firstEnemy)) {
                        for (Player secondEnemy : northSquare.getNorthSquare().getPlayerOnSquare()) {
                            if (enemyChosenList.contains(secondEnemy)) {
                                result = true;
                                break;
                            }
                        }
                    }
                }
            }
            //SOUTH CHECK
            if(northSquare != null) {
                for(Player firstEnemy: southSquare.getPlayerOnSquare()) {
                    if (enemyChosenList.contains(firstEnemy)) {
                        for (Player secondEnemy : southSquare.getSouthSquare().getPlayerOnSquare()) {
                            if (enemyChosenList.contains(secondEnemy)) {
                                result = true;
                                break;
                            }
                        }
                    }
                }
            }
            //EAST CHECK
            if(northSquare != null) {
                for(Player firstEnemy: eastSquare.getPlayerOnSquare()) {
                    if (enemyChosenList.contains(firstEnemy)) {
                        for (Player secondEnemy : eastSquare.getEastSquare().getPlayerOnSquare()) {
                            if (enemyChosenList.contains(secondEnemy)) {
                                result = true;
                                break;
                            }
                        }
                    }
                }
            }
            //WEST CHECK
            if(northSquare != null) {
                for(Player firstEnemy: westSquare.getPlayerOnSquare()) {
                    if (enemyChosenList.contains(firstEnemy)) {
                        for (Player secondEnemy : westSquare.getWestSquare().getPlayerOnSquare()) {
                            if (enemyChosenList.contains(secondEnemy)) {
                                result = true;
                                break;
                            }
                        }
                    }
                }
            }
        }

        return result;

    }
}
