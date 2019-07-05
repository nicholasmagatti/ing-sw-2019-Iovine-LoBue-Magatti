package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.Message.toController.InfoRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.Message;
import it.polimi.ProgettoIngSW2019.common.Message.toView.MessageActionLeft;
import it.polimi.ProgettoIngSW2019.common.Message.toView.MessageScorePlayer;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.enums.DeckType;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.ClientMessageReceiver;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.model.powerup_effects.TagbackGrenadeEff;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class TestEndTurnController {
    private final String hostname0 = "PincoHost0";
    private final String hostname1 = "PincoHost1";
    private final String hostname2 = "PincoHost2";

    private EndTurnController endTurnController;
    private ClientMessageReceiver clientMsgRcv;
    private GameTable gameTable;
    private TurnManager turnManager;
    private VirtualView virtualView;
    private IdConverter idConverter;
    private CreateJson createJson;
    private HostNameCreateList hostNameCreateList;
    private SpawnController spawnController;

    private ArgumentCaptor<Event> eventCapture = ArgumentCaptor.forClass(Event.class);
    private ArgumentCaptor<List<String>> hostnameListCapture = ArgumentCaptor.forClass(List.class);
    private Maps maps;

    private Player player0, player1, player2;


    @Before
    public void setUp() {
        maps = new Maps();

        clientMsgRcv = mock(ClientMessageReceiver.class);

        gameTable = spy(new GameTable(maps.getMaps()[0],5));
        when(gameTable.getMap()).thenReturn(maps.getMaps()[0]);


        player0 = spy(new Player(0, "Priscilla", gameTable, hostname0));
        player1 = spy(new Player(1, "Luca", gameTable, hostname1));
        player2 = spy(new Player(2, "Nick", gameTable, hostname2));

        when(player1.isPlayerDown()).thenReturn(true);
        when(player1.getDamageLine()).thenReturn(new ArrayList<>(Arrays.asList("Nick", "Priscilla", "Nick", "Priscilla", "Nick", "Priscilla", "Nick", "Priscilla", "Nick", "Priscilla", "Nick", "Priscilla")));

        //powerUp0 = new PowerUp(0, DeckType.POWERUP_CARD, AmmoType.YELLOW, GeneralInfo.TAGBACK_GRENADE, "", new TagbackGrenadeEff());
        //powerUp1 = new PowerUp(1, DeckType.POWERUP_CARD, AmmoType.RED, GeneralInfo.TAGBACK_GRENADE, "", new TagbackGrenadeEff());

        //when(player0.getPowerUps()).thenReturn(new ArrayList<>(Arrays.asList(powerUp0, powerUp1)));

        Player[] players = new Player[3];
        players[0] = player0;
        players[1] = player1;
        players[2] = player2;

        when(gameTable.getPlayers()).thenReturn(players);
        when(gameTable.isKillshotTrackFull()).thenReturn(false);
        when(gameTable.getNumberOfActivePlayers()).thenReturn(3);

        turnManager = new TurnManager(gameTable);
        virtualView = spy(VirtualView.class);
        idConverter = new IdConverter(gameTable);
        createJson = new CreateJson(turnManager);
        hostNameCreateList = new HostNameCreateList(turnManager);
        spawnController = new SpawnController(turnManager, virtualView, idConverter, createJson, hostNameCreateList);
        endTurnController = new EndTurnController(turnManager, virtualView, idConverter, createJson, hostNameCreateList, spawnController);

        virtualView.addObserver(endTurnController);
        virtualView.registerMessageReceiver(hostname0, clientMsgRcv);
        virtualView.registerMessageReceiver(hostname1, clientMsgRcv);
        virtualView.registerMessageReceiver(hostname2, clientMsgRcv);

        turnManager.decreaseActionsLeft();
        turnManager.decreaseActionsLeft();
    }


    @Test
    public void endTurnNoDoubleTest() {
        InfoRequest infoRequest = new InfoRequest(player0.getHostname(), player0.getIdPlayer());
        Event event = new Event(EventType.REQUEST_ENDTURN_INFO, new Gson().toJson(infoRequest));

        virtualView.forwardEvent(event);

        verify(virtualView, times(6)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());

        List<Event> allEvents = eventCapture.getAllValues();
        List<List<String>> allHost = hostnameListCapture.getAllValues();

        assertEquals(EventType.SCORE_DEAD_PLAYERS, allEvents.get(0).getCommand());
        assertEquals(EventType.UPDATE_KILLSHOTTRACK, allEvents.get(1).getCommand());
        assertEquals(EventType.MSG_PLAYER_SPAWN, allEvents.get(2).getCommand());
        assertEquals(EventType.UPDATE_MAP, allEvents.get(3).getCommand());
        assertEquals(EventType.MSG_NEW_TURN, allEvents.get(4).getCommand());
        assertEquals(EventType.MSG_FIRST_TURN_PLAYER, allEvents.get(5).getCommand());

        Type listType = new TypeToken<ArrayList<MessageScorePlayer>>(){}.getType();
        List<MessageScorePlayer> messageScorePlayerList = new Gson().fromJson(allEvents.get(0).getMessageInJsonFormat(), listType);

        assertEquals(1, messageScorePlayerList.size());
        assertEquals(player1.getCharaName(), messageScorePlayerList.get(0).getDeadNamePlayer());
        assertEquals(player0.getCharaName(), messageScorePlayerList.get(0).getKillerNamePlayer());
        assertEquals(player2.getCharaName(), messageScorePlayerList.get(0).getFirstBloodNamePlayer());

        assertEquals(hostname1, allHost.get(2).get(0));

        assertEquals(hostname1, allHost.get(5).get(0));
    }


    @Test
    public void endTurnWithDoubleTest() {
        when(player2.isPlayerDown()).thenReturn(true);
        when(player1.getDamageLine()).thenReturn(new ArrayList<>(Arrays.asList("Nick", "Priscilla", "Nick", "Priscilla", "Nick", "Priscilla", "Nick", "Priscilla", "Nick", "Priscilla", "Nick", "Priscilla")));
        when(player2.getDamageLine()).thenReturn(new ArrayList<>(Arrays.asList("Luca", "Luca", "Luca", "Priscilla", "Luca", "Priscilla", "Priscilla", "Priscilla", "Luca", "Priscilla", "Luca", "Priscilla")));

        InfoRequest infoRequest = new InfoRequest(player0.getHostname(), player0.getIdPlayer());
        Event event = new Event(EventType.REQUEST_ENDTURN_INFO, new Gson().toJson(infoRequest));

        virtualView.forwardEvent(event);

        verify(virtualView, times(8)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());

        List<Event> allEvents = eventCapture.getAllValues();

        assertEquals(EventType.MSG_DOUBLEKILL, allEvents.get(0).getCommand());
        assertEquals(EventType.SCORE_DEAD_PLAYERS, allEvents.get(1).getCommand());
        assertEquals(EventType.UPDATE_KILLSHOTTRACK, allEvents.get(2).getCommand());
        assertEquals(EventType.MSG_PLAYER_SPAWN, allEvents.get(3).getCommand());
        assertEquals(EventType.MSG_PLAYER_SPAWN, allEvents.get(4).getCommand());
        assertEquals(EventType.UPDATE_MAP, allEvents.get(5).getCommand());
        assertEquals(EventType.MSG_NEW_TURN, allEvents.get(6).getCommand());
        assertEquals(EventType.MSG_FIRST_TURN_PLAYER, allEvents.get(7).getCommand());

        Type listType = new TypeToken<ArrayList<MessageScorePlayer>>(){}.getType();
        List<MessageScorePlayer> messageScorePlayerList = new Gson().fromJson(allEvents.get(1).getMessageInJsonFormat(), listType);

        Message message = new Gson().fromJson(allEvents.get(0).getMessageInJsonFormat(), Message.class);
        assertEquals(player0.getIdPlayer(), message.getIdPlayer());

        assertEquals(2, messageScorePlayerList.size());

        Message message1 = new Gson().fromJson(allEvents.get(6).getMessageInJsonFormat(), Message.class);
        assertEquals(player1.getIdPlayer(), message1.getIdPlayer());
    }


    @Test
    public void noDeadPlayers() {
        when(player1.isPlayerDown()).thenReturn(false);
        when(player2.isPlayerDown()).thenReturn(false);

        gameTable.getMap()[0][2].addPlayerOnSquare(player0);
        Square square = gameTable.getMap()[0][2];
        when(player1.getPosition()).thenReturn(square);

        InfoRequest infoRequest = new InfoRequest(player0.getHostname(), player0.getIdPlayer());
        Event event = new Event(EventType.REQUEST_ENDTURN_INFO, new Gson().toJson(infoRequest));

        virtualView.forwardEvent(event);

        verify(virtualView, times(4)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());

        List<Event> allEvents = eventCapture.getAllValues();

        assertEquals(EventType.UPDATE_MAP, allEvents.get(0).getCommand());
        assertEquals(EventType.MSG_NEW_TURN, allEvents.get(1).getCommand());
        assertEquals(EventType.MSG_BEFORE_ENEMY_ACTION_OR_RELOAD, allEvents.get(2).getCommand());
        assertEquals(EventType.MSG_MY_N_ACTION_LEFT, allEvents.get(3).getCommand());

        Message message = new Gson().fromJson(allEvents.get(1).getMessageInJsonFormat(), Message.class);
        assertEquals(player1.getIdPlayer(), message.getIdPlayer());

        MessageActionLeft messageActionLeft = new Gson().fromJson(allEvents.get(3).getMessageInJsonFormat(), MessageActionLeft.class);
        assertEquals(2, messageActionLeft.getnActionsLeft());
        assertTrue(messageActionLeft.getPowerUpsCanUse().isEmpty());
    }


    @Test
    public void endTurnDisconnectedNoDead() {
        when(player0.isActive()).thenReturn(false);
        when(player1.isPlayerDown()).thenReturn(false);
        when(player2.isPlayerDown()).thenReturn(false);

        gameTable.getMap()[0][2].addPlayerOnSquare(player0);
        Square square = gameTable.getMap()[0][2];
        when(player1.getPosition()).thenReturn(square);

        InfoRequest infoRequest = new InfoRequest(player0.getHostname(), player0.getIdPlayer());
        Event event = new Event(EventType.END_TURN_DUE_USER_DISCONNECTION, new Gson().toJson(infoRequest));

        virtualView.forwardEvent(event);

        verify(virtualView, times(4)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());

        List<Event> allEvents = eventCapture.getAllValues();

        assertEquals(EventType.UPDATE_MAP, allEvents.get(0).getCommand());
        assertEquals(EventType.MSG_NEW_TURN, allEvents.get(1).getCommand());
        assertEquals(EventType.MSG_BEFORE_ENEMY_ACTION_OR_RELOAD, allEvents.get(2).getCommand());
        assertEquals(EventType.MSG_MY_N_ACTION_LEFT, allEvents.get(3).getCommand());

        Message message = new Gson().fromJson(allEvents.get(1).getMessageInJsonFormat(), Message.class);
        assertEquals(player1.getIdPlayer(), message.getIdPlayer());

        MessageActionLeft messageActionLeft = new Gson().fromJson(allEvents.get(3).getMessageInJsonFormat(), MessageActionLeft.class);
        assertEquals(2, messageActionLeft.getnActionsLeft());
        assertTrue(messageActionLeft.getPowerUpsCanUse().isEmpty());
    }


    @Test
    public void finalScoreTest() {
        when(gameTable.isKillshotTrackFull()).thenReturn(true);
        when(player1.getDamageLine()).thenReturn(new ArrayList<>(Arrays.asList("Nick", "Priscilla", "Nick", "Priscilla", "Nick", "Priscilla", "Nick")));
        when(player0.getDamageLine()).thenReturn(new ArrayList<>());
        when(player2.getDamageLine()).thenReturn(new ArrayList<>());
        when(player0.isPlayerDown()).thenReturn(false);
        when(player1.isPlayerDown()).thenReturn(false);
        when(player2.isPlayerDown()).thenReturn(false);
        when(player0.getScore()).thenReturn(52);
        when(player2.getScore()).thenReturn(52);
        when(player1.getScore()).thenReturn(40);


        InfoRequest infoRequest = new InfoRequest(player0.getHostname(), player0.getIdPlayer());
        Event event = new Event(EventType.REQUEST_ENDTURN_INFO, new Gson().toJson(infoRequest));

        virtualView.forwardEvent(event);

        verify(virtualView, times(4)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());

        List<Event> allEvents = eventCapture.getAllValues();

        assertEquals(EventType.MSG_CONCLUSION, allEvents.get(0).getCommand());
        assertEquals(EventType.MSG_SCORE_ALIVE_PLAYER, allEvents.get(1).getCommand());
        assertEquals(EventType.MSG_SCORE_KILLSHOT_TRACK, allEvents.get(2).getCommand());
        assertEquals(EventType.MSG_FINAL_RESULTS, allEvents.get(3).getCommand());
    }
}
