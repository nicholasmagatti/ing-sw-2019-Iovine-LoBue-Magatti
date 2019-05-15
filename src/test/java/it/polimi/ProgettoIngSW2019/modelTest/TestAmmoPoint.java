package it.polimi.ProgettoIngSW2019.modelTest;

import it.polimi.ProgettoIngSW2019.model.AmmoCard;
import it.polimi.ProgettoIngSW2019.model.AmmoPoint;
import it.polimi.ProgettoIngSW2019.model.Deck;
import it.polimi.ProgettoIngSW2019.model.DeckFactory;
import it.polimi.ProgettoIngSW2019.common.enums.DeckType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestAmmoPoint {
    AmmoPoint ammoPoint;

    @Before
    public void setup(){
        ammoPoint = new AmmoPoint(0, true,true,true,true);
        DeckFactory d = new DeckFactory();
        Deck ammoDeck = new Deck(DeckType.AMMO_CARD, d);
        ammoPoint.reset(ammoDeck);
    }

    @Test
    public void shouldReturnAmmoCardAndRemoveItFromSquare(){
        AmmoCard cardBeforeGrab = ammoPoint.getAmmoCard();
        AmmoCard a = ammoPoint.grabCard();

        assertEquals(a, cardBeforeGrab);
        assertTrue(ammoPoint.getAmmoCard() == null);
    }
}
