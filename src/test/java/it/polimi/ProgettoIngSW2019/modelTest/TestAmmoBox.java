package it.polimi.ProgettoIngSW2019.modelTest;

import it.polimi.ProgettoIngSW2019.model.AmmoBox;
import it.polimi.ProgettoIngSW2019.model.enums.AmmoType;
import org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;

/**
 * @author Nicholas Magatti
 */
public class TestAmmoBox {

    @Test
    public void testMaxCapacity(){
        AmmoBox ammoBox  = new AmmoBox();
        for(int i=0; i < 7; i++) {
            ammoBox.addWhenPossible(AmmoType.BLUE);
            ammoBox.addWhenPossible(AmmoType.RED);
            ammoBox.addWhenPossible(AmmoType.YELLOW);
        }

        assertTrue(ammoBox.getBlueAmmo() == 3);
        assertTrue(ammoBox.getRedAmmo() == 3);
        assertTrue(ammoBox.getYellowAmmo() == 3);

    }

}
