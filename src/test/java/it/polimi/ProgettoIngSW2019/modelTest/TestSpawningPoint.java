package it.polimi.ProgettoIngSW2019.modelTest;

import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.common.enums.DeckType;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import org.junit.*;
import static org.junit.Assert.*;

public class TestSpawningPoint {
    private SpawningPoint spawn;
    private Deck weaponDeck;
    private List<WeaponCard> weaponCardOnHand;
    private DistanceDictionary distanceDictionary;

    @Before
    public void setup(){
        Maps maps = new Maps();
        distanceDictionary = new DistanceDictionary(maps.getMaps()[0]);
        weaponCardOnHand = new ArrayList<>();
        DeckFactory d = new DeckFactory(distanceDictionary);
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
