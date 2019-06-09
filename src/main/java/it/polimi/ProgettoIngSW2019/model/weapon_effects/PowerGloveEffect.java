package it.polimi.ProgettoIngSW2019.model.weapon_effects;

import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;

import java.util.List;

public class PowerGloveEffect extends WeaponEffect {
    /**
     * Constructor class
     * Read the weapon effect file and associate to paramater
     *
     * @param jsonObj
     * @suthor: Luca Iovine
     */
    public PowerGloveEffect(JsonObject jsonObj, DistanceDictionary distance) {
        super(jsonObj, distance);
    }

    @Override
    public void activateEffect(Player player, List<Player> enemyList) {
        if(enemyList.size() <= 1) {
            super.activateEffect(player, enemyList);
            player.moveTo(enemyList.get(0).getPosition());
        }else{
            //TODO: deve lanciare messaggio d'errore generico (?)
        }
    }
}
