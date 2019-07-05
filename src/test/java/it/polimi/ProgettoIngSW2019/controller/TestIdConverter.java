package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.enums.DeckType;
import it.polimi.ProgettoIngSW2019.common.utilities.ClientMessageReceiver;
import it.polimi.ProgettoIngSW2019.custom_exception.IllegalIdException;
import it.polimi.ProgettoIngSW2019.custom_exception.NotPartOfBoardException;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import it.polimi.ProgettoIngSW2019.model.powerup_effects.TagbackGrenadeEff;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class TestIdConverter {
    private IdConverter idConverter;
    private GameTable gameTable;
    private TurnManager turnManager;
    private Maps maps;
    private Player player0, player1;
    private WeaponCard weaponCard1, weaponCard2;
    private PowerUp powerUp;
    private DistanceDictionary distanceDictionary;


    @Before
    public void setUp() {
        maps = new Maps();

        gameTable = spy(new GameTable(maps.getMaps()[0],5));
        when(gameTable.getMap()).thenReturn(maps.getMaps()[0]);

        player0 = spy(new Player(0, "Priscilla", gameTable, "host0"));
        player1 = new Player(1, "Luca", gameTable, "host1");

        weaponCard1 = new WeaponCard(1, DeckType.WEAPON_CARD, "Nome", "", Arrays.asList(AmmoType.RED), "LockRifleEff.json", distanceDictionary);
        weaponCard2 = new WeaponCard(2, DeckType.WEAPON_CARD, "Nome", "", Arrays.asList(AmmoType.RED), "LockRifleEff.json", distanceDictionary);

        powerUp = new PowerUp(3, DeckType.POWERUP_CARD, AmmoType.YELLOW, "Nome", "", new TagbackGrenadeEff());

        when(player0.getLoadedWeapons()).thenReturn(new ArrayList<>(Arrays.asList(weaponCard1)));
        when(player0.getUnloadedWeapons()).thenReturn(new ArrayList<>(Arrays.asList(weaponCard2)));
        when(player0.getPowerUps()).thenReturn(new ArrayList<>(Arrays.asList(powerUp)));

        Player[] players = new Player[2];
        players[0] = player0;
        players[1] = player1;

        when(gameTable.getPlayers()).thenReturn(players);

        turnManager = new TurnManager(gameTable);
        idConverter = new IdConverter(gameTable);
    }


    @Test
    public void notPartOfTheBoardException() {
        try {
            int[] coordinates = new int[2];
            coordinates[0] = 1;
            coordinates[1] = 5;
            Square square = idConverter.getSquareByCoordinates(coordinates);
            fail();
        }
        catch(NotPartOfBoardException e){
            System.out.println(e);
        }
    }


    @Test
    public void illegalIdException() {
        try {
            Player player = idConverter.getPlayerById(5);
            fail();
        }
        catch(IllegalIdException e){
            System.out.println(e);
        }


        try {
            WeaponCard weaponCard = idConverter.getUnloadedWeaponById(0, 100);
            fail();
        }
        catch(IllegalIdException e){
            System.out.println(e);
        }


        try {
            PowerUp powerUp = idConverter.getPowerUpCardById(0, 100);
            fail();
        }
        catch(IllegalIdException e){
            System.out.println(e);
        }
    }
}
