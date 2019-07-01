package it.polimi.ProgettoIngSW2019.common.enums;

import com.google.gson.annotations.SerializedName;

public enum WeaponEffectType {
    @SerializedName("general")
    GENERAL("general"),
    @SerializedName("flamethrower")
    FLAMETHROWER("flamethrower"),
    @SerializedName("vortex")
    VORTEX("vortex"),
    @SerializedName("one_per_square")
    ONE_PER_SQUARE("one_per_square"),
    @SerializedName("power_glove")
    POWER_GLOVE("power_glove"),
    @SerializedName("same_room")
    SAME_ROOM("same_room"),
    @SerializedName("shift_one_movement")
    SHIFT_ONE_MOVEMENT("shift_one_movement"),
    @SerializedName("tractor_beam")
    TRACTOR_BEAM("tractor_beam"),
    @SerializedName("hellion")
    HELLION("hellion"),
    @SerializedName("railgun")
    RAILGUN("railgun");


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
