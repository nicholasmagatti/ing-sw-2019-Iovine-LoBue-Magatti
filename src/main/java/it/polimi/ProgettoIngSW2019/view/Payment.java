package it.polimi.ProgettoIngSW2019.view;

import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;

/**
 * @author Nicholas Magatti
 */
public class Payment {

    static String costToString(int[] cost){
        //example of output: 2 red, 1 blue, 0 yellow
        return cost[AmmoType.intFromAmmoType(AmmoType.RED)] + " red, " +
                cost[AmmoType.intFromAmmoType(AmmoType.BLUE)] + " blue, " +
                cost[AmmoType.intFromAmmoType(AmmoType.YELLOW)] + " yellow";
    }

}
