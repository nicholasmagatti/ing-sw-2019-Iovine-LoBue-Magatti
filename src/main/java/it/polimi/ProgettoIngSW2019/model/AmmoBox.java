package it.polimi.ProgettoIngSW2019.model;

import it.polimi.ProgettoIngSW2019.common.enums.*;

/**
 * Class used manage the ammo box of a player and its content
 * @author Nicholas Magatti
 */
public class AmmoBox {
    //number of the following attributes between 0 and 3
    private int redAmmo = 1;
    private int blueAmmo = 1;
    private int yellowAmmo = 1;

    /**
     * Get the number of red ammo units
     * @return the number of red ammo units
     */
    public int getRedAmmo() {
        return redAmmo;
    }

    /**
     * Get the number of blue ammo units
     * @return the number of blue ammo units
     */
    public int getBlueAmmo() {
        return blueAmmo;
    }

    /**
     * Get the number of yellow ammo units
     * @return the number of yellow ammo units
     */
    public int getYellowAmmo() {
        return yellowAmmo;
    }

    /**
     * Remove a unit of ammo of a specific color from the ammo box
     * @param ammoType
     */
    public void remove(AmmoType ammoType){
        switch (ammoType){
            case RED:
                redAmmo--;
                break;
            case BLUE:
                blueAmmo--;
                break;
            case YELLOW:
                yellowAmmo--;
                break;
        }
    }

    /**
     * Add a unit of ammo of a specific color to the ammo box
     * @param ammoType
     */
    public void addWhenPossible(AmmoType ammoType){
        switch (ammoType){
            case RED:
                if(redAmmo < 3){
                    redAmmo++;
                }
                break;
            case BLUE:
                if(blueAmmo < 3) {
                    blueAmmo++;
                }
                break;
            case YELLOW:
                if(yellowAmmo < 3) {
                    yellowAmmo++;
                }
                break;
        }
    }
}
