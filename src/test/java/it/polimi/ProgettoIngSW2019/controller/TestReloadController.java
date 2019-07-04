package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.Message.toController.InfoRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toController.ReloadChoiceRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.WeaponsCanPayResponse;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.enums.DeckType;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.ClientMessageReceiver;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import it.polimi.ProgettoIngSW2019.model.powerup_effects.TagbackGrenadeEff;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.spy;

public class TestReloadController {
    private final String hostname0 = "PincoHost0";
    private final String hostname1 = "PincoHost1";

    private ReloadController reloadController;
    private ClientMessageReceiver clientMsgRcv;
    private GameTable gameTable;
    private TurnManager turnManager;
    private VirtualView virtualView;
    private IdConverter idConverter;
    private CreateJson createJson;
    private HostNameCreateList hostNameCreateList;
    private PayAmmoController payAmmoController;

    private ArgumentCaptor<Event> eventCapture = ArgumentCaptor.forClass(Event.class);
    private ArgumentCaptor<List<String>> hostnameListCapture = ArgumentCaptor.forClass(List.class);

    private Player player0, player1;
    private WeaponCard weaponCard1, weaponCard2;
    private PowerUp powerUp1, powerUp2;
    private Maps maps;
    private DistanceDictionary distanceDictionary;


    @Before
    public void setUp() {
        maps = new Maps();

        clientMsgRcv = mock(ClientMessageReceiver.class);

        gameTable = spy(new GameTable(maps.getMaps()[0], 5));
        when(gameTable.getMap()).thenReturn(maps.getMaps()[0]);

        player0 = spy(new Player(0, "Priscilla", gameTable, hostname0));
        player1 = spy(new Player(1, "Luca", gameTable, hostname1));

        weaponCard1 = new WeaponCard(0, DeckType.WEAPON_CARD, "LOCK_RIFLE", "", Arrays.asList(AmmoType.BLUE, AmmoType.BLUE), "LockRifleEff.json", distanceDictionary);
        weaponCard2 = new WeaponCard(1, DeckType.WEAPON_CARD, "ELECTROSCYTHE", "", Arrays.asList(AmmoType.RED), "ElectroSchyteEff.json", distanceDictionary);

        powerUp1 = new PowerUp(2, DeckType.POWERUP_CARD, AmmoType.BLUE, GeneralInfo.TAGBACK_GRENADE, "", new TagbackGrenadeEff());
        powerUp2 = new PowerUp(3, DeckType.POWERUP_CARD, AmmoType.YELLOW, GeneralInfo.TAGBACK_GRENADE, "", new TagbackGrenadeEff());

        when(player0.getUnloadedWeapons()).thenReturn(new ArrayList<>(Arrays.asList(weaponCard1, weaponCard2)));
        when(player0.getPowerUps()).thenReturn(new ArrayList<>(Arrays.asList(powerUp2)));

        Player[] players = new Player[2];
        players[0] = player0;
        players[1] = player1;

        when(gameTable.getPlayers()).thenReturn(players);

        turnManager = new TurnManager(gameTable);
        virtualView = spy(VirtualView.class);
        idConverter = new IdConverter(gameTable);
        createJson = new CreateJson(turnManager);
        hostNameCreateList = new HostNameCreateList(turnManager);
        payAmmoController = new PayAmmoController(createJson);

        reloadController = new ReloadController(turnManager, virtualView, idConverter, createJson, hostNameCreateList, payAmmoController);

        virtualView.addObserver(reloadController);
        virtualView.registerMessageReceiver(hostname0, clientMsgRcv);
        virtualView.registerMessageReceiver(hostname1, clientMsgRcv);

        turnManager.decreaseActionsLeft();
        turnManager.decreaseActionsLeft();
    }


    @Test
    public void weaponsCanReloadCorrectTest() {
        InfoRequest infoRequest = new InfoRequest(player0.getHostname(), player0.getIdPlayer());
        Event event = new Event(EventType.REQUEST_WEAPONS_CAN_RELOAD, new Gson().toJson(infoRequest));

        virtualView.forwardEvent(event);

        verify(virtualView, times(1)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());

        List<Event> allEvents = eventCapture.getAllValues();
        WeaponsCanPayResponse weaponsCanPayResponse = new Gson().fromJson(allEvents.get(0).getMessageInJsonFormat(), WeaponsCanPayResponse.class);
        assertEquals(EventType.RESPONSE_REQUEST_WEAPONS_CAN_RELOAD, allEvents.get(0).getCommand());
        assertEquals(weaponCard2.getIdCard(), weaponsCanPayResponse.getWeaponsCanReload().get(0).getIdWeapon());
        assertEquals(1, weaponsCanPayResponse.getWeaponsCanReload().size());
    }


    @Test
    public void reloadCorrectTest() {
        InfoRequest infoRequest = new InfoRequest(player0.getHostname(), player0.getIdPlayer());
        Event event = new Event(EventType.REQUEST_WEAPONS_CAN_RELOAD, new Gson().toJson(infoRequest));

        virtualView.forwardEvent(event);

        int[] ammoToSpend = new int[3];
        ammoToSpend[GeneralInfo.RED_ROOM_ID] = 1;
        List<Integer> powerUpToDiscard = new ArrayList<>();

        ReloadChoiceRequest reloadChoiceRequest = new ReloadChoiceRequest(player0.getHostname(), player0.getIdPlayer(), weaponCard2.getIdCard(), ammoToSpend, powerUpToDiscard);
        Event event1 = new Event(EventType.REQUEST_RELOAD, new Gson().toJson(reloadChoiceRequest));

        virtualView.forwardEvent(event1);

        verify(virtualView, times(5)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());

        List<Event> allEvents = eventCapture.getAllValues();

        assertEquals(EventType.MSG_ALL_RELOAD_WEAPON, allEvents.get(1).getCommand());
        assertEquals(EventType.UPDATE_PLAYER_INFO, allEvents.get(2).getCommand());
        assertEquals(EventType.UPDATE_MY_LOADED_WEAPONS, allEvents.get(3).getCommand());
        assertEquals(EventType.RESPONSE_REQUEST_WEAPONS_CAN_RELOAD, allEvents.get(4).getCommand());

        WeaponsCanPayResponse weaponsCanPayResponse1 = new Gson().fromJson(allEvents.get(0).getMessageInJsonFormat(), WeaponsCanPayResponse.class);
        assertEquals(weaponCard2.getIdCard(), weaponsCanPayResponse1.getWeaponsCanReload().get(0).getIdWeapon());
        assertEquals(weaponCard2.getIdCard(), player0.getLoadedWeapons().get(0).getIdCard());

        WeaponsCanPayResponse weaponsCanPayResponse2 = new Gson().fromJson(allEvents.get(4).getMessageInJsonFormat(), WeaponsCanPayResponse.class);
        assertTrue(weaponsCanPayResponse2.getWeaponsCanReload().isEmpty());
    }
}
