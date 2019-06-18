package it.polimi.ProgettoIngSW2019.common.enums;

import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;

/**
 * Enum AmmoType to add color for ammo and square
 * @author Priscilla Lo Bue
 */
public enum AmmoType {
    RED(GeneralInfo.RED_ROOM_ID),
    BLUE(GeneralInfo.BLUE_ROOM_ID),
    YELLOW(GeneralInfo.YELLOW_ROOM_ID);

    private int intValue;

    AmmoType(int intValue) {
        this.intValue = intValue;
    }


    /**
     * from the ammoType -> the corresponding value
     * @param ammoType      type of the Ammo(color)
     * @return              int of the Ammo
     */
    public static int intFromAmmoType(AmmoType ammoType) {
        return ammoType.intValue;
    }


    public static int contEnum() {
        int cont = 0;
        for(AmmoType ammoType: AmmoType.values()) {
            cont++;
        }

        return cont;
    }
}