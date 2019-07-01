package it.polimi.ProgettoIngSW2019.modelTest;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.spy;

import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.enums.DeckType;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import it.polimi.ProgettoIngSW2019.model.powerup_effects.TagbackGrenadeEff;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class TestDeck {
    private Deck weaponDeck;
    private Deck powerUpDeck;
    private Deck ammoDeck;
    private int nCardsExp;
    private DeckFactory deckFactory;
    private DistanceDictionary distanceDictionary;


    @Before
    public void setUp() {
        Maps maps = new Maps();
        distanceDictionary = new DistanceDictionary(maps.getMaps()[0]);
        deckFactory = new DeckFactory(distanceDictionary);
        ammoDeck = new Deck(DeckType.AMMO_CARD, deckFactory);
        powerUpDeck = new Deck(DeckType.POWERUP_CARD, deckFactory);
        weaponDeck = new Deck(DeckType.WEAPON_CARD, deckFactory);
    }


    @Test
    public void drawAmmoCardTest() {
        /*
        AmmoCard expected:
            idCard = 0
            DeckType = AMMO_CARD
            Ammo: YELLOW, BLUE, BLUE
         */
        nCardsExp = ammoDeck.getCards().size()-1;
        AmmoCard ammoCardDrawn = (AmmoCard) ammoDeck.drawCard();
        List<AmmoType> ammo = Arrays.asList(AmmoType.YELLOW, AmmoType.BLUE, AmmoType.BLUE);

        assertEquals(0, ammoCardDrawn.getIdCard());
        assertEquals(DeckType.AMMO_CARD, ammoCardDrawn.getCardType());
        assertEquals(ammo, ammoCardDrawn.getAmmo());
        assertEquals(nCardsExp, ammoDeck.getCards().size());
        assertEquals(0, DeckFactory.getFirstIdAmmo());
        assertEquals(35, DeckFactory.getLastIdAmmo());
    }



    @Test
    public void drawPowerCardTest() {
        /*
        PowerUp expected:
            idCard = 36
            cardType = POWERUP_CARD
            ammo = YELLOW
            name = TAGBACK GRENADE
            description = ""
            powerUpEff = new TagbackGrenadeEff()
         */
        nCardsExp = powerUpDeck.getCards().size()-1;
        PowerUp powerUpCardDrawn = (PowerUp) powerUpDeck.drawCard();
        System.out.println(nCardsExp);

        assertEquals("must be 36",36, powerUpCardDrawn.getIdCard());
        assertEquals("must be POWERUP_CARD", DeckType.POWERUP_CARD, powerUpCardDrawn.getCardType());
        assertEquals("must be YELLOW", AmmoType.YELLOW, powerUpCardDrawn.getGainAmmoColor());
        assertEquals("must be TAGBACK GRENADE", "TAGBACK GRENADE", powerUpCardDrawn.getName());
        assertEquals("", powerUpCardDrawn.getDescription());
        assertTrue(powerUpCardDrawn.getPowerUpEffect() instanceof TagbackGrenadeEff);
        assertEquals(nCardsExp, powerUpDeck.getCards().size());
        assertEquals(36, DeckFactory.getFirstIdPowerUp());
        assertEquals(59, DeckFactory.getLastIdPowerUp());
    }



    @Test
    public void drawWeaponCardTest() {
        /*
        WeaponCard expected:
            idCard = 60
            cardType = WEAPON_CARD
            name = LOCK RIFLE
            description = ""
            reloadCost = BLUE, BLUE
            pathOfEffectFile = LockRifleEff.json
         */
        nCardsExp = weaponDeck.getCards().size()-1;
        WeaponCard weaponCardDrawn = (WeaponCard) weaponDeck.drawCard();
        List<AmmoType> reload = Arrays.asList(AmmoType.BLUE, AmmoType.BLUE);

        assertEquals(60, weaponCardDrawn.getIdCard());
        assertEquals(DeckType.WEAPON_CARD, weaponCardDrawn.getCardType());
        assertEquals("LOCK RIFLE", weaponCardDrawn.getName());
        assertEquals("", weaponCardDrawn.getDescription());
        assertEquals(reload, weaponCardDrawn.getreloadCost());
        assertEquals(nCardsExp, weaponDeck.getCards().size());
        assertEquals(60, DeckFactory.getFirstIdWeapon());
        assertEquals(80, DeckFactory.getLastIdWeapon());
    }



    @Test
    public void getCardsTest() {
        assertNotEquals(null, ammoDeck.getCards());
        assertNotEquals(null, powerUpDeck.getCards());
        assertNotEquals(null, weaponDeck.getCards());
    }


    //TODO: test the gets null
}
