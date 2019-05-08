package it.polimi.ProgettoIngSW2019.modelTest;

import it.polimi.ProgettoIngSW2019.model.AmmoCard;
import it.polimi.ProgettoIngSW2019.model.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.model.enums.DeckType;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;


public class TestAmmoCard {
    private AmmoCard ammoCard1;
    private AmmoCard ammoCard2;

    @Before
    public void setUpTest(){
        ammoCard1 = new AmmoCard(0,DeckType.AMMO_CARD, AmmoType.YELLOW, AmmoType.BLUE, AmmoType.BLUE);
        ammoCard2 = new AmmoCard(1,DeckType.AMMO_CARD, AmmoType.YELLOW, AmmoType.YELLOW);
    }


    @Test
    public void getAmmo3Test() {
        assertEquals("list ammo: YELLOW, BLUE, BLUE", (Arrays.asList(AmmoType.YELLOW, AmmoType.BLUE, AmmoType.BLUE)), ammoCard1.getAmmo());
    }


    @Test
    public void getAmmo2Test(){
        assertEquals("List ammo: YELLOW, YELLOW", (Arrays.asList(AmmoType.YELLOW, AmmoType.YELLOW)), ammoCard2.getAmmo());
    }


    @Test
    public void hasPowerUpTest(){
        assertFalse("non ha powerUp", ammoCard1.hasPowerUp());
    }


    @Test
    public void hasNotPowerUpTest(){
        assertTrue("ha powerUp", ammoCard2.hasPowerUp());
    }

}