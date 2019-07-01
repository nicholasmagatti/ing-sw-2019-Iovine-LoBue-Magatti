package it.polimi.ProgettoIngSW2019.modelTest;

import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.common.enums.DeckType;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestAmmoPoint {
    AmmoPoint ammoPoint;
    private DistanceDictionary distanceDictionary;

    @Before
    public void setup(){
        Maps maps = new Maps();
        distanceDictionary = new DistanceDictionary(maps.getMaps()[0]);
        DeckFactory d = new DeckFactory(distanceDictionary);

        ammoPoint = new AmmoPoint(0, true,true,true,true);
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
