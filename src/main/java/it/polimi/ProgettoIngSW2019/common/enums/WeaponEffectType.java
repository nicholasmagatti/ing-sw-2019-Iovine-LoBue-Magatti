package it.polimi.ProgettoIngSW2019.common.enums;

import com.google.gson.annotations.SerializedName;

public enum WeaponEffectType {
    @SerializedName("general")
    GENERAL("general"),
    FLAMETHROWER("flamethrower"),
    VORTEX("vortex"),
    ONE_PER_SQUARE("one_per_square"),
    POWER_GLOVE("power_glove"),
    SAME_ROOM("same_room"),
    SAME_SQUARE("same_square"),
    TRACTOR_BEAM("tractor_beam");

    private String stringValue;

    WeaponEffectType(String stringValue){
        this.stringValue = stringValue;
    }

    public static WeaponEffectType fromString(String value) {
        for(WeaponEffectType tps: values()){
            String tpsStringVal = tps.stringValue;
            if(tpsStringVal.equals(value)){
                return tps;
            }
        }
        return null;
    }
}
