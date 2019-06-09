package it.polimi.ProgettoIngSW2019.model.weapon_effects;

import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;

public class VortexEffect extends WeaponEffect {
    /**
     * Constructor class
     * Read the weapon effect file and associate to paramater
     *
     * @param jsonObj
     * @suthor: Luca Iovine
     */
    public VortexEffect(JsonObject jsonObj, DistanceDictionary distance) {
        super(jsonObj, distance);
    }
}
