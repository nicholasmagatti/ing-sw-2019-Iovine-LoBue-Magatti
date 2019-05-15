package it.polimi.ProgettoIngSW2019.modelTest;

import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.common.enums.DeckType;

import java.util.ArrayList;
import java.util.List;

import org.junit.*;
import static org.junit.Assert.*;

public class TestSpawningPoint {
    private SpawningPoint spawn;
    private Deck weaponDeck;
    private List<WeaponCard> weaponCardOnHand;

    @Before
    public void setup(){
        weaponCardOnHand = new ArrayList<>();
        DeckFactory d = new DeckFactory();
        weaponDeck = new Deck(DeckType.WEAPON_CARD, d);
        spawn = new SpawningPoint(0, true,true,true,true);
        spawn.reset(weaponDeck);
        weaponCardOnHand.add((WeaponCard)weaponDeck.drawCard());
        weaponCardOnHand.add((WeaponCard)weaponDeck.drawCard());
        weaponCardOnHand.add((WeaponCard)weaponDeck.drawCard());
    }

    @Test
    public void ShouldSwapTheTwoCard(){
        WeaponCard weaponInHandBefore = weaponCardOnHand.get(0);
        WeaponCard weaponOnTableBefore = spawn.getWeaponCards().get(0);

        spawn.swapWeaponsOnSpawnPoint(weaponOnTableBefore, weaponInHandBefore);

        assertTrue(spawn.getWeaponCards().contains(weaponInHandBefore));
        assertFalse(spawn.getWeaponCards().contains(weaponOnTableBefore));
    }
}
