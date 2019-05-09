package it.polimi.ProgettoIngSW2019.model.enums;

/**
 * Enum AmmoType to add color for ammo and square
 * @author Priscilla Lo Bue
 */
public enum AmmoType {
    RED(1),
    BLUE(3),
    YELLOW(4);

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
}