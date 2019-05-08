package java.it.polimi.ProgettoIngSW2019.modelTest;

import static org.junit.Assert.*;

import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.model.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.model.enums.DeckType;
import it.polimi.ProgettoIngSW2019.model.powerup_effects.TagbackGrenadeEff;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class TestDeck {
    private Card ammoExp, powerUpExp, weaponExp;
    private Deck weaponDeck;
    private Deck powerUpDeck;
    private Deck ammoDeck;
    private int nCardsExp;

    @Before
    public void setUp() {
        weaponDeck = new Deck(DeckType.WEAPON_CARD);
        powerUpDeck = new Deck(DeckType.POWERUP_CARD);
        ammoDeck = new Deck(DeckType.AMMO_CARD);
    }


    @Test
    public void drawCardTest() {
        ammoExp = new AmmoCard(0, DeckType.AMMO_CARD, AmmoType.YELLOW, AmmoType.BLUE, AmmoType.BLUE);
        nCardsExp = ammoDeck.getCards().size()-1;
        assertEquals(ammoExp, ammoDeck.drawCard());
        assertEquals(nCardsExp, ammoDeck.getCards().size());

        powerUpExp = new PowerUp(36, DeckType.POWERUP_CARD, AmmoType.YELLOW, "TAGBACK GRENADE", "", new TagbackGrenadeEff());
        nCardsExp = powerUpDeck.getCards().size()-1;
        assertEquals(powerUpExp, powerUpDeck.drawCard());
        assertEquals(nCardsExp, powerUpDeck.getCards().size());

        weaponExp = new WeaponCard(60, DeckType.WEAPON_CARD, "LOCK RIFLE", "", Arrays.asList(AmmoType.BLUE, AmmoType.BLUE), "LockRifleEff.json");
        nCardsExp = weaponDeck.getCards().size()-1;
        assertEquals(weaponExp, weaponDeck.drawCard());
        assertEquals(nCardsExp, weaponDeck.getCards().size());
    }


    @Test
    public void getCardsTest() {
        assertNotEquals(null, ammoDeck.getCards());
        assertNotEquals(null, powerUpDeck.getCards());
        assertNotEquals(null, weaponDeck.getCards());
    }


    //TODO: test the gets null
}
