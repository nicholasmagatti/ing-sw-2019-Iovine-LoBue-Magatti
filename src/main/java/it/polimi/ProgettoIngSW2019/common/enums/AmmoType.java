package it.polimi.ProgettoIngSW2019.common.enums;

/**
 * Enum AmmoType to add color for ammo and square
 * @author Priscilla Lo Bue
 */
public enum AmmoType {
    RED(0),
    BLUE(1),
    YELLOW(2);

    private int intValue;
    public static final int nAmmoType = contEnum();

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