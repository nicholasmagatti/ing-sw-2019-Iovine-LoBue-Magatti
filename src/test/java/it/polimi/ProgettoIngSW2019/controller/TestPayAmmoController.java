package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.Message.toView.PayAmmoList;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.enums.DeckType;
import it.polimi.ProgettoIngSW2019.common.utilities.ClientMessageReceiver;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.custom_exception.IllegalAttributeException;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import it.polimi.ProgettoIngSW2019.model.powerup_effects.NewtonEff;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.spy;

public class TestPayAmmoController {
    private final String hostname0 = "PincoHost0";
    private final String hostname1 = "PincoHost1";

    private PayAmmoController payAmmoController;
    private CreateJson createJson;
    private TurnManager turnManager;
    private GameTable gameTable;
    private DistanceDictionary distanceDictionary;

    private Player player0, player1, player2;
    private WeaponCard weaponCard;
    private PowerUp powerUp;
    private Maps maps;


    @Before
    public void setUp() {
        maps = new Maps();
        gameTable = spy(new GameTable(maps.getMaps()[0], 5));
        when(gameTable.getMap()).thenReturn(maps.getMaps()[0]);

        player0 = spy(new Player(0, "Priscilla", gameTable, hostname0));
        player1 = spy(new Player(1, "Luca", gameTable, hostname1));
        player2 = mock(Player.class);
        weaponCard = new WeaponCard(0, DeckType.WEAPON_CARD, "LOCK_RIFLE", "", Arrays.asList(AmmoType.BLUE, AmmoType.BLUE), "LockRifleEff.json", distanceDictionary);
        powerUp = new PowerUp(1, DeckType.POWERUP_CARD, AmmoType.YELLOW, GeneralInfo.NEWTON, "", new NewtonEff());

        Player[] players = new Player[3];
        players[0] = player0;
        players[1] = player1;
        players[2] = player2;

        when(gameTable.getPlayers()).thenReturn(players);

        turnManager = new TurnManager(gameTable);
        createJson = new CreateJson(turnManager);
        payAmmoController = new PayAmmoController(createJson);
    }


    @Test
    public void ammoToPayTest() {
        List<AmmoType> weaponPay = weaponCard.getBuyCost();
        when(player0.getPowerUps()).thenReturn(new ArrayList<>(Arrays.asList()));
        when(player0.getBlueAmmo()).thenReturn(2);

        PayAmmoList payAmmoListCreated = payAmmoController.ammoToPay(player0, weaponCard, weaponPay);

        assertEquals(weaponCard.getIdCard(), payAmmoListCreated.getIdWeapon());
        assertTrue(payAmmoListCreated.getAmmoInPowerUp().isEmpty());
        assertEquals(2, payAmmoListCreated.getAmmoInAmmoBox()[1]);
    }


    @Test
    public void checkAmmoToPayFromViewCorrectAmmoTest() {
        List<AmmoType> ammoCost = new ArrayList<>(Arrays.asList(AmmoType.BLUE, AmmoType.YELLOW));
        int[] ammoToPay = new int[3];
        ammoToPay[GeneralInfo.RED_ROOM_ID] = 0;
        ammoToPay[GeneralInfo.YELLOW_ROOM_ID] = 1;
        ammoToPay[GeneralInfo.BLUE_ROOM_ID] = 1;

        List<PowerUp> powerUps = new ArrayList<>();

        assertTrue(payAmmoController.checkAmmoToPayFromView(ammoCost, powerUps, ammoToPay));
    }


    @Test
    public void checkAmmoToPayFromViewCorrectPowerUpTest() {
        List<AmmoType> ammoCost = new ArrayList<>(Arrays.asList(AmmoType.BLUE, AmmoType.YELLOW));
        int[] ammoToPay = new int[3];
        ammoToPay[GeneralInfo.RED_ROOM_ID] = 0;
        ammoToPay[GeneralInfo.YELLOW_ROOM_ID] = 0;
        ammoToPay[GeneralInfo.BLUE_ROOM_ID] = 1;

        List<PowerUp> powerUps = new ArrayList<>(Arrays.asList(powerUp));

        assertTrue(payAmmoController.checkAmmoToPayFromView(ammoCost, powerUps, ammoToPay));
    }


    @Test
    public void nullPointerException() {
        try {
            List<AmmoType> weaponPay = weaponCard.getBuyCost();
            payAmmoController.ammoToPay(null, weaponCard, weaponPay);
            fail();
        }
        catch(NullPointerException e){
            System.out.println(e);
        }

        try {
            List<AmmoType> weaponPay = weaponCard.getBuyCost();
            payAmmoController.ammoToPay(player0, null, weaponPay);
            fail();
        }
        catch(NullPointerException e){
            System.out.println(e);
        }

        try {
            payAmmoController.ammoToPay(player0, weaponCard, null);
            fail();
        }
        catch(NullPointerException e){
            System.out.println(e);
        }

        try {
            List<AmmoType> weaponPay = weaponCard.getBuyCost();
            when(player0.getPowerUps()).thenReturn(null);
            payAmmoController.ammoToPay(player0, weaponCard, weaponPay);
            fail();
        }
        catch(NullPointerException e){
            System.out.println(e);
        }
    }


    @Test
    public void hasNoEnoughAmmo() {
        try {
            List<AmmoType> weaponPay = new ArrayList<>(Arrays.asList(AmmoType.RED, AmmoType.RED));
            when(player0.getBlueAmmo()).thenReturn(0);
            when(player0.getYellowAmmo()).thenReturn(0);
            when(player0.getRedAmmo()).thenReturn(0);
            when(player0.getPowerUps()).thenReturn(new ArrayList<>());
            payAmmoController.ammoToPay(player0, weaponCard, weaponPay);
            fail();
        }
        catch(IllegalAttributeException e){
            System.out.println(e);
        }
    }
}
