package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.LightModel.PlayerDataLM;
import it.polimi.ProgettoIngSW2019.common.Message.toController.GrabChoiceRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toController.GrabWeaponChoiceRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toController.InfoRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.GrabInfoResponse;
import it.polimi.ProgettoIngSW2019.common.Message.toView.MessageDrawMyPowerUp;
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
import org.mockito.internal.matchers.Null;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

public class TestGrabController {
    private final String hostname0 = "PincoHost0";
    private final String hostname1 = "PincoHost1";

    private GrabController grabController;
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
    private PowerUp powerUp1, powerUp2, powerUp3;
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

        List<Session> sessions = new ArrayList<>();
        sessions.add(new Session("Priscilla", "pass0", hostname0));
        sessions.add(new Session("Luca", "pass1", hostname1));
        gameTable.setPlayersBeforeStart(sessions);

        //weaponCard1 = new WeaponCard(0, DeckType.WEAPON_CARD, "LOCK_RIFLE", "", Arrays.asList(AmmoType.BLUE, AmmoType.BLUE), "LockRifleEff.json", distanceDictionary);
        //weaponCard2 = new WeaponCard(1, DeckType.WEAPON_CARD, "ELECTROSCYTHE", "", Arrays.asList(AmmoType.RED), "ElectroSchyteEff.json", distanceDictionary);

        powerUp1 = new PowerUp(2, DeckType.POWERUP_CARD, AmmoType.BLUE, GeneralInfo.TAGBACK_GRENADE, "", new TagbackGrenadeEff());
        powerUp2 = new PowerUp(3, DeckType.POWERUP_CARD, AmmoType.YELLOW, GeneralInfo.TAGBACK_GRENADE, "", new TagbackGrenadeEff());
        powerUp3 = new PowerUp(3, DeckType.POWERUP_CARD, AmmoType.RED, GeneralInfo.TAGBACK_GRENADE, "", new TagbackGrenadeEff());

        //ammoCard1 = new AmmoCard(4, DeckType.AMMO_CARD, AmmoType.YELLOW, AmmoType.BLUE, AmmoType.BLUE);
        //ammoCard2 = new AmmoCard(5, DeckType.AMMO_CARD, AmmoType.YELLOW, AmmoType.BLUE);

        Player[] players = new Player[2];
        players[0] = player0;
        players[1] = player1;

        when(gameTable.getPlayers()).thenReturn(players);
        gameTable.getMap()[0][2].addPlayerOnSquare(player0);
        Square square = gameTable.getMap()[0][2];
        when(player0.getPosition()).thenReturn(square);
        when(player0.getUnloadedWeapons()).thenReturn(new ArrayList<>());
        when(player0.getPowerUps()).thenReturn(new ArrayList<>(Arrays.asList(powerUp2)));

        /*
        SpawningPoint spB = (SpawningPoint) gameTable.getMap()[0][2];
        when(spB.getWeaponCards()).thenReturn(new ArrayList<>(Arrays.asList(weaponCard1, weaponCard2)));

        AmmoPoint apB = (AmmoPoint) gameTable.getMap()[0][1];
        when(apB.getAmmoCard()).thenReturn(ammoCard1);

        AmmoPoint apP = (AmmoPoint) gameTable.getMap()[1][2];
        when(apP.getAmmoCard()).thenReturn(ammoCard2);
*/
        turnManager = new TurnManager(gameTable);
        virtualView = spy(VirtualView.class);
        idConverter = new IdConverter(gameTable);
        createJson = new CreateJson(turnManager);
        hostNameCreateList = new HostNameCreateList(turnManager);
        payAmmoController = new PayAmmoController(createJson);

        grabController = new GrabController(turnManager, virtualView, idConverter, createJson, hostNameCreateList, payAmmoController);

        virtualView.addObserver(grabController);
        virtualView.registerMessageReceiver(hostname0, clientMsgRcv);
        virtualView.registerMessageReceiver(hostname1, clientMsgRcv);
    }


    @Test
    public void checkGrabInfoRequestCorrectTest() {
        InfoRequest infoRequest = new InfoRequest(player0.getHostname(), player0.getIdPlayer());
        Event event = new Event(EventType.REQUEST_GRAB_INFO, new Gson().toJson(infoRequest));

        virtualView.forwardEvent(event);

        verify(virtualView, times(1)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());

        List<Event> allEvents = eventCapture.getAllValues();

        assertEquals(EventType.RESPONSE_REQUEST_GRAB_INFO, allEvents.get(0).getCommand());
        GrabInfoResponse grabInfoResponse = new Gson().fromJson(allEvents.get(0).getMessageInJsonFormat(), GrabInfoResponse.class);
        assertEquals(3, grabInfoResponse.getCoordinatesSquareToGrab().size());
    }


    @Test
    public void nullPointerException() {
        try {
            when(player0.getPosition()).thenReturn(null);
            InfoRequest infoRequest = new InfoRequest(player0.getHostname(), player0.getIdPlayer());
            Event event = new Event(EventType.REQUEST_GRAB_INFO, new Gson().toJson(infoRequest));

            virtualView.forwardEvent(event);

            verify(virtualView, times(1)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
            fail();
        }
        catch(NullPointerException e){
            System.out.println(e);
        }
    }


    @Test
    public void grabAmmoCorrectTest() {
        AmmoPoint square = (AmmoPoint) gameTable.getMap()[0][1];
        AmmoCard ammoCard = square.getAmmoCard();
        InfoRequest infoRequest = new InfoRequest(player0.getHostname(), player0.getIdPlayer());
        Event event1 = new Event(EventType.REQUEST_GRAB_INFO, new Gson().toJson(infoRequest));

        virtualView.forwardEvent(event1);

        GrabChoiceRequest grabChoiceRequest = new GrabChoiceRequest(player0.getHostname(), player0.getIdPlayer(), square.getCoordinates(gameTable.getMap()));
        Event event2 = new Event(EventType.REQUEST_GRAB, new Gson().toJson(grabChoiceRequest));

        virtualView.forwardEvent(event2);

        if(ammoCard.hasPowerUp()) {
            verify(virtualView, times(8)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
            List<Event> allEvents = eventCapture.getAllValues();
            assertEquals(EventType.UPDATE_PLAYER_INFO, allEvents.get(1).getCommand());
            assertEquals(EventType.MSG_DRAW_MY_POWERUP, allEvents.get(2).getCommand());
            assertEquals(EventType.MSG_ENEMY_DRAW_POWERUP, allEvents.get(3).getCommand());
            assertEquals(EventType.UPDATE_MY_POWERUPS, allEvents.get(4).getCommand());
            assertEquals(EventType.UPDATE_MAP, allEvents.get(5).getCommand());
            assertEquals(EventType.MSG_BEFORE_ENEMY_ACTION_OR_RELOAD, allEvents.get(6).getCommand());
            assertEquals(EventType.MSG_MY_N_ACTION_LEFT, allEvents.get(7).getCommand());

            MessageDrawMyPowerUp messageDrawMyPowerUp = new Gson().fromJson(allEvents.get(2).getMessageInJsonFormat(), MessageDrawMyPowerUp.class);
            assertEquals(1, messageDrawMyPowerUp.getNamePowerUps().size());
        }
        else {
            verify(virtualView, times(5)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
            List<Event> allEvents = eventCapture.getAllValues();
            assertEquals(EventType.UPDATE_PLAYER_INFO, allEvents.get(1).getCommand());
            assertEquals(EventType.UPDATE_MAP, allEvents.get(2).getCommand());
            assertEquals(EventType.MSG_BEFORE_ENEMY_ACTION_OR_RELOAD, allEvents.get(3).getCommand());
            assertEquals(EventType.MSG_MY_N_ACTION_LEFT, allEvents.get(4).getCommand());
        }
    }


    @Test
    public void wrongSquareToGrab() {
        AmmoPoint square = (AmmoPoint) gameTable.getMap()[0][0];
        InfoRequest infoRequest = new InfoRequest(player0.getHostname(), player0.getIdPlayer());
        Event event1 = new Event(EventType.REQUEST_GRAB_INFO, new Gson().toJson(infoRequest));

        virtualView.forwardEvent(event1);

        GrabChoiceRequest grabChoiceRequest = new GrabChoiceRequest(player0.getHostname(), player0.getIdPlayer(), square.getCoordinates(gameTable.getMap()));
        Event event2 = new Event(EventType.REQUEST_GRAB, new Gson().toJson(grabChoiceRequest));

        virtualView.forwardEvent(event2);

        verify(virtualView, times(2)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        List<Event> allEvents = eventCapture.getAllValues();

        assertEquals(EventType.ERROR, allEvents.get(1).getCommand());
    }
}
