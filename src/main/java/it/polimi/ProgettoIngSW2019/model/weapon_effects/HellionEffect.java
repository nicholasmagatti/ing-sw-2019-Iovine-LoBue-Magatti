package it.polimi.ProgettoIngSW2019.model.weapon_effects;

import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.custom_exception.EnemySizeLimitExceededException;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;

import javax.naming.SizeLimitExceededException;
import java.util.List;

public class HellionEffect extends  WeaponEffect {
    private final int nrOfMarkToGiveAfterEffect = 1;
    /**
     * Constructor class
     * Read the weapon effect configuration json file and setup the effect
     *
     * @param jsonObj
     * @param distance
     * @suthor: Luca Iovine
     */
    public HellionEffect(JsonObject jsonObj, DistanceDictionary distance) {
        super(jsonObj, distance);
    }

    /**
     * It activate the effect of the weapon doing damage and marking the player in the enemyChosenList,
     * which has to be one and no more.
     * After the damage, it mark all the player on the same square of the enemy.
     *
     * @param weaponUser user player
     * @param enemyChosenList list of the enemy chosen from the user
     * @throws SizeLimitExceededException whenever the enemy size list is greater than expected
     * @suthor: Luca Iovine
     */
    //NOT TO BE TESTED
    @Override
    public void activateEffect(Player weaponUser, List<Player> enemyChosenList) throws EnemySizeLimitExceededException {
        super.activateEffect(weaponUser, enemyChosenList);
        for(Player target: enemyChosenList.get(0).getPosition().getPlayerOnSquare()){
            if(weaponUser.getIdPlayer() != enemyChosenList.get(0).getIdPlayer())
                weaponUser.markPlayer(nrOfMarkToGiveAfterEffect, target);
        }
    }
}
