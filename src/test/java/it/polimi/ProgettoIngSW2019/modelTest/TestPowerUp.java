package it.polimi.ProgettoIngSW2019.modelTest;

import it.polimi.ProgettoIngSW2019.model.PowerUp;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.enums.DeckType;
import it.polimi.ProgettoIngSW2019.model.powerup_effects.NewtonEff;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestPowerUp {
    PowerUp pu1;


    @Test
    public void powerUpSetNameTest() {
        try {
            pu1 = new PowerUp(0, DeckType.POWERUP_CARD, AmmoType.YELLOW, null, "descrizione", new NewtonEff());
            fail();
        } catch (NullPointerException e) {
            System.out.println(e);
        }
    }


    @Test
    public void powerUpSetDescrTest() {
        try {
            pu1 = new PowerUp(0, DeckType.POWERUP_CARD, AmmoType.YELLOW, "powerUp1", null, new NewtonEff());
            fail();
        } catch (NullPointerException e) {
            System.out.println(e);
        }
    }


    @Test
    public void powerUpSetEffTest() {
        try {
            pu1 = new PowerUp(0, DeckType.POWERUP_CARD, AmmoType.YELLOW, "powerUp1", "descrizione", null);
            fail();
        } catch (NullPointerException e) {
            System.out.println(e);
        }
    }
}