package it.polimi.ProgettoIngSW2019.controller;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.LightModel.MapLM;
import it.polimi.ProgettoIngSW2019.common.LightModel.MyPowerUpLM;
import it.polimi.ProgettoIngSW2019.common.LightModel.PlayerDataLM;
import it.polimi.ProgettoIngSW2019.common.Message.toController.InfoRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toController.SpawnChoiceRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toController.TagBackGrenadeRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.*;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestSpawningPoint {
    private final String hostname0 = "PincoHost0";
    private final String hostname1 = "PincoHost1";

    private SpawnController spawnController;
    private ClientMessageReceiver clientMsgRcv;
    private GameTable gameTable;
    private TurnManager turnManager;
    private VirtualView virtualView;
    private IdConverter idConverter;
    private CreateJson createJson;
    private HostNameCreateList hostNameCreateList;

    private ArgumentCaptor<Event> eventCapture = ArgumentCaptor.forClass(Event.class);
    private ArgumentCaptor<List<String>> hostnameListCapture = ArgumentCaptor.forClass(List.class);

    private Player player0, player1;
    private PowerUp powerUp0, powerUp1;
    private Maps maps;


    @Before
    public void setUp() {
        maps = new Maps();

        clientMsgRcv = mock(ClientMessageReceiver.class);

        gameTable = spy(new GameTable(maps.getMaps()[0],5));
        when(gameTable.getMap()).thenReturn(maps.getMaps()[0]);

        player0 = spy(new Player(0, "Priscilla", gameTable, hostname0));
        player1 = new Player(1, "Luca", gameTable, hostname1);

        powerUp0 = new PowerUp(0, DeckType.POWERUP_CARD, AmmoType.YELLOW, GeneralInfo.TAGBACK_GRENADE, "", new TagbackGrenadeEff());
        powerUp1 = new PowerUp(1, DeckType.POWERUP_CARD, AmmoType.RED, GeneralInfo.TAGBACK_GRENADE, "", new TagbackGrenadeEff());

        when(player0.getPowerUps()).thenReturn(new ArrayList<>(Arrays.asList(powerUp0, powerUp1)));

        Player[] players = new Player[2];
        players[0] = player0;
        players[1] = player1;

        when(gameTable.getPlayers()).thenReturn(players);

        turnManager = new TurnManager(gameTable);
        virtualView = spy(VirtualView.class);
        idConverter = new IdConverter(gameTable);
        createJson = new CreateJson(turnManager);
        hostNameCreateList = new HostNameCreateList(turnManager);

        spawnController = new SpawnController(turnManager, virtualView, idConverter, createJson, hostNameCreateList);

        virtualView.addObserver(spawnController);
        virtualView.registerMessageReceiver(hostname0, clientMsgRcv);
        virtualView.registerMessageReceiver(hostname1, clientMsgRcv);
    }


    @Test
    public void checkInfoFromViewCorrectTest() {
        InfoRequest infoRequest = new InfoRequest(player0.getHostname(), player0.getIdPlayer());
        String infoRequestJson = new Gson().toJson(infoRequest);
        assertTrue(spawnController.checkInfoFromView(infoRequestJson));
    }

    @Test
    public void checkInfoFromViewWrongTest() {
        InfoRequest infoRequest = new InfoRequest(player1.getHostname(), player0.getIdPlayer());
        String infoRequestJson = new Gson().toJson(infoRequest);
        assertFalse(spawnController.checkInfoFromView(infoRequestJson));
    }


    @Test
    public void requestInitialSpawnDrawCardsTest() {
        when(player0.getPosition()).thenReturn(null);

        InfoRequest infoRequest = new InfoRequest(player0.getHostname(), player0.getIdPlayer());
        EventType eventTypeIS = EventType.REQUEST_INITIAL_SPAWN_CARDS;
        String infoRequestJson = new Gson().toJson(infoRequest);
        Event infoRequestEvent = new Event(eventTypeIS, infoRequestJson);

        PowerUp powerUp1 = (PowerUp) gameTable.getPowerUpDeck().getCards().get(0);
        PowerUp powerUp2 = (PowerUp) gameTable.getPowerUpDeck().getCards().get(1);
        List<PowerUp> powerUps = new ArrayList<>();
        powerUps.add(powerUp1);
        powerUps.add(powerUp2);

        virtualView.forwardEvent(infoRequestEvent);

        verify(virtualView, times(3)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        List<Event> allEvents = eventCapture.getAllValues();

        MessageDrawMyPowerUp messageDrawMyPowerUp = new Gson().fromJson(allEvents.get(0).getMessageInJsonFormat(), MessageDrawMyPowerUp.class);
        MessageEnemyDrawPowerUp messageEnemyDrawPowerUp = new Gson().fromJson(allEvents.get(1).getMessageInJsonFormat(), MessageEnemyDrawPowerUp.class);
        DrawCardsInfoResponse drawCardsInfoResponse = new Gson().fromJson(allEvents.get(2).getMessageInJsonFormat(), DrawCardsInfoResponse.class);

        assertEquals(EventType.MSG_DRAW_MY_POWERUP, allEvents.get(0).getCommand());
        assertEquals(EventType.MSG_ENEMY_DRAW_POWERUP, allEvents.get(1).getCommand());
        assertEquals(EventType.RESPONSE_REQUEST_INITIAL_SPAWN_CARDS, allEvents.get(2).getCommand());

        assertEquals(powerUp1.getIdCard(), messageDrawMyPowerUp.getIdPowerUp()[0]);
        assertEquals(powerUp2.getIdCard(), messageDrawMyPowerUp.getIdPowerUp()[1]);
        assertEquals(powerUp1.getName(), messageDrawMyPowerUp.getNamePowerUps().get(0));
        assertEquals(powerUp2.getName(), messageDrawMyPowerUp.getNamePowerUps().get(1));

        assertEquals(2, messageEnemyDrawPowerUp.getnCards());
        assertEquals(player0.getIdPlayer(), messageEnemyDrawPowerUp.getIdPlayer());

        assertEquals(player0.getIdPlayer(), drawCardsInfoResponse.getIdPlayer());
        for(int i = 0; i < powerUps.size(); i++) {
            assertEquals(powerUps.get(i).getIdCard(), drawCardsInfoResponse.getDrawnPowerUps().get(i).getIdPowerUp());
        }
    }


    @Test
    public void requestSpawnDrawCardTest() {
        gameTable.getMap()[0][2].addPlayerOnSquare(player0);
        Square square = gameTable.getMap()[0][2];
        when(player0.getPosition()).thenReturn(square);
        when(player0.isPlayerDown()).thenReturn(true);

        List<PowerUp> powerUps = new ArrayList<>();

        InfoRequest infoRequest = new InfoRequest(player0.getHostname(), player0.getIdPlayer());
        EventType eventType = EventType.REQUEST_SPAWN_CARDS;
        String infoRequestJson = new Gson().toJson(infoRequest);
        Event infoRequestEvent = new Event(eventType, infoRequestJson);

        PowerUp powerUp = (PowerUp) gameTable.getPowerUpDeck().getCards().get(0);
        powerUps.add(powerUp);
        powerUps.add(powerUp0);
        powerUps.add(powerUp1);

        virtualView.forwardEvent(infoRequestEvent);

        verify(virtualView, times(4)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());

        List<Event> allEvents = eventCapture.getAllValues();

        PlayerDataLM playerDataLM = new Gson().fromJson(allEvents.get(0).getMessageInJsonFormat(), PlayerDataLM.class);
        MessageDrawMyPowerUp messageDrawMyPowerUp = new Gson().fromJson(allEvents.get(1).getMessageInJsonFormat(), MessageDrawMyPowerUp.class);
        MessageEnemyDrawPowerUp messageEnemyDrawPowerUp = new Gson().fromJson(allEvents.get(2).getMessageInJsonFormat(), MessageEnemyDrawPowerUp.class);
        DrawCardsInfoResponse drawCardsInfoResponse = new Gson().fromJson(allEvents.get(3).getMessageInJsonFormat(), DrawCardsInfoResponse.class);

        assertEquals(EventType.UPDATE_PLAYER_INFO, allEvents.get(0).getCommand());
        assertEquals(EventType.MSG_DRAW_MY_POWERUP, allEvents.get(1).getCommand());
        assertEquals(EventType.MSG_ENEMY_DRAW_POWERUP, allEvents.get(2).getCommand());
        assertEquals(EventType.RESPONSE_REQUEST_SPAWN_CARDS, allEvents.get(3).getCommand());

        assertEquals(player0.getIdPlayer(), playerDataLM.getIdPlayer());
        assertEquals(player0.getDamageLine(), player0.getDamageLine());

        assertEquals(powerUp.getIdCard(), messageDrawMyPowerUp.getIdPowerUp()[0]);
        assertEquals(powerUp.getName(), messageDrawMyPowerUp.getNamePowerUps().get(0));
        assertEquals(player0.getIdPlayer(), messageDrawMyPowerUp.getIdPlayer());

        assertEquals(1, messageEnemyDrawPowerUp.getnCards());
        assertEquals(player0.getIdPlayer(), messageEnemyDrawPowerUp.getIdPlayer());

        assertEquals(player0.getIdPlayer(), drawCardsInfoResponse.getIdPlayer());
        for(int i = 0; i < powerUps.size(); i++) {
            assertEquals(powerUps.get(i).getIdCard(), drawCardsInfoResponse.getDrawnPowerUps().get(i).getIdPowerUp());
        }
    }


    @Test
    public void requestInitialSpawnDrawCardsWrongTest() {
        gameTable.getMap()[0][2].addPlayerOnSquare(player0);
        Square square = gameTable.getMap()[0][2];
        when(player0.getPosition()).thenReturn(square);

        InfoRequest infoRequest = new InfoRequest(player0.getHostname(), player0.getIdPlayer());
        EventType eventTypeIS = EventType.REQUEST_INITIAL_SPAWN_CARDS;
        String infoRequestJson = new Gson().toJson(infoRequest);
        Event infoRequestEvent = new Event(eventTypeIS, infoRequestJson);

        virtualView.forwardEvent(infoRequestEvent);

        verify(virtualView).sendMessage(eventCapture.capture(), hostnameListCapture.capture());

        String messageError = eventCapture.getValue().getMessageInJsonFormat();

        assertEquals(EventType.ERROR, eventCapture.getValue().getCommand());
        assertEquals(GeneralInfo.MSG_ERROR, messageError);
    }


    @Test
    public void requestSpawnDrawCardsWrongTest() {
        gameTable.getMap()[0][2].addPlayerOnSquare(player0);
        Square square = gameTable.getMap()[0][2];
        when(player0.getPosition()).thenReturn(square);
        when(player0.isPlayerDown()).thenReturn(false);

        InfoRequest infoRequest = new InfoRequest(player0.getHostname(), player0.getIdPlayer());
        EventType eventTypeIS = EventType.REQUEST_SPAWN_CARDS;
        String infoRequestJson = new Gson().toJson(infoRequest);
        Event infoRequestEvent = new Event(eventTypeIS, infoRequestJson);

        virtualView.forwardEvent(infoRequestEvent);

        verify(virtualView).sendMessage(eventCapture.capture(), hostnameListCapture.capture());

        String messageError = eventCapture.getValue().getMessageInJsonFormat();

        assertEquals(EventType.ERROR, eventCapture.getValue().getCommand());
        assertEquals(GeneralInfo.MSG_ERROR, messageError);
    }


    @Test
    public void respawnCorrectTest() {
        gameTable.getMap()[0][2].addPlayerOnSquare(player0);
        Square square = gameTable.getMap()[0][2];
        when(player0.getPosition()).thenReturn(square);
        when(player0.isPlayerDown()).thenReturn(true);


        List<PowerUp> powerUps = new ArrayList<>();
        PowerUp powerUp = (PowerUp) gameTable.getPowerUpDeck().getCards().get(0);
        powerUps.add(powerUp1);
        powerUps.add(powerUp);

        InfoRequest infoRequest = new InfoRequest(player0.getHostname(), player0.getIdPlayer());
        EventType eventTypeIS = EventType.REQUEST_SPAWN_CARDS;
        String infoRequestJson = new Gson().toJson(infoRequest);
        Event infoRequestEvent = new Event(eventTypeIS, infoRequestJson);

        virtualView.forwardEvent(infoRequestEvent);

        SpawnChoiceRequest spawnChoiceRequest = new SpawnChoiceRequest(player0.getHostname(), player0.getIdPlayer(), powerUp0.getIdCard());
        EventType eventType = EventType.REQUEST_SPAWN;
        Event event = new Event(eventType, new Gson().toJson(spawnChoiceRequest));

        virtualView.forwardEvent(event);

        verify(virtualView, times(7)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        List<Event> allEvents = eventCapture.getAllValues();

        MessagePowerUpsDiscarded messagePowerUpsDiscarded = new Gson().fromJson(allEvents.get(4).getMessageInJsonFormat(), MessagePowerUpsDiscarded.class);
        String sss = allEvents.get(5).getMessageInJsonFormat();
        MapLM updatemapLM = new Gson().fromJson(sss, MapLM.class);
        MyPowerUpLM updateMyPowerUpLM = new Gson().fromJson(allEvents.get(6).getMessageInJsonFormat(), MyPowerUpLM.class);

        assertEquals(EventType.MSG_POWERUP_DISCARDED_TO_SPAWN, allEvents.get(4).getCommand());
        assertEquals(player0.getIdPlayer(), messagePowerUpsDiscarded.getIdPlayer());
        assertEquals(player0.getCharaName(), messagePowerUpsDiscarded.getNamePlayer());
        assertEquals(powerUp0.getIdCard(), messagePowerUpsDiscarded.getPowerUpsToDiscard().get(0).getIdPowerUp());
        assertEquals(powerUp0.getName(), messagePowerUpsDiscarded.getPowerUpsToDiscard().get(0).getName());
        reset(player0);
        assertEquals(player0.getPosition().getSquareType(), gameTable.getMap()[2][3].getSquareType());
        assertEquals(player0.getPosition().getIdRoom(), gameTable.getMap()[2][3].getIdRoom());

        assertEquals(EventType.UPDATE_MAP, allEvents.get(5).getCommand());

        assertEquals(EventType.UPDATE_MY_POWERUPS, allEvents.get(6).getCommand());
        for(int i = 0; i < powerUps.size(); i++) {
            assertEquals(powerUps.get(i).getName(), updateMyPowerUpLM.getPowerUps().get(i).getName());
            assertEquals(powerUps.get(i).getIdCard(), updateMyPowerUpLM.getPowerUps().get(i).getIdPowerUp());
        }
    }


    @Test
    public void respawnWrongPPTest() {
        gameTable.getMap()[0][2].addPlayerOnSquare(player0);
        Square square = gameTable.getMap()[0][2];
        when(player0.getPosition()).thenReturn(square);
        when(player0.isPlayerDown()).thenReturn(true);

        PowerUp powerUp2 = mock(PowerUp.class);
        when(powerUp2.getIdCard()).thenReturn(2);

        List<PowerUp> powerUps = new ArrayList<>();
        PowerUp powerUp = (PowerUp) gameTable.getPowerUpDeck().getCards().get(0);
        powerUps.add(powerUp1);
        powerUps.add(powerUp);

        InfoRequest infoRequest = new InfoRequest(player0.getHostname(), player0.getIdPlayer());
        EventType eventTypeIS = EventType.REQUEST_SPAWN_CARDS;
        String infoRequestJson = new Gson().toJson(infoRequest);
        Event infoRequestEvent = new Event(eventTypeIS, infoRequestJson);

        virtualView.forwardEvent(infoRequestEvent);

        SpawnChoiceRequest spawnChoiceRequest = new SpawnChoiceRequest(player0.getHostname(), player0.getIdPlayer(), powerUp2.getIdCard());
        EventType eventType = EventType.REQUEST_SPAWN;
        Event event = new Event(eventType, new Gson().toJson(spawnChoiceRequest));

        virtualView.forwardEvent(event);

        verify(virtualView, times(5)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        List<Event> allEvents = eventCapture.getAllValues();

        String messageError = allEvents.get(4).getMessageInJsonFormat();

        assertEquals(EventType.ERROR, allEvents.get(4).getCommand());
        assertEquals(GeneralInfo.MSG_ERROR, messageError);
    }


    @Test
    public void initialRespawnCorrectTest() {
        List<PowerUp> powerUps = new ArrayList<>();

        when(player0.getPosition()).thenReturn(null);
        when(player0.isPlayerDown()).thenReturn(false);
        when(player0.getPowerUps()).thenReturn(powerUps);

        PowerUp powerUpD1 = (PowerUp) gameTable.getPowerUpDeck().getCards().get(0);
        PowerUp powerUpD2 = (PowerUp) gameTable.getPowerUpDeck().getCards().get(1);
        powerUps.add(powerUpD1);
        powerUps.add(powerUpD2);

        InfoRequest infoRequest = new InfoRequest(player0.getHostname(), player0.getIdPlayer());
        EventType eventTypeIS = EventType.REQUEST_INITIAL_SPAWN_CARDS;
        String infoRequestJson = new Gson().toJson(infoRequest);
        Event infoRequestEvent = new Event(eventTypeIS, infoRequestJson);

        virtualView.forwardEvent(infoRequestEvent);

        SpawnChoiceRequest spawnChoiceRequest = new SpawnChoiceRequest(player0.getHostname(), player0.getIdPlayer(), powerUpD1.getIdCard());
        EventType eventType = EventType.REQUEST_INITIAL_SPAWN;
        Event event = new Event(eventType, new Gson().toJson(spawnChoiceRequest));

        virtualView.forwardEvent(event);

        verify(virtualView, times(8)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        List<Event> allEvents = eventCapture.getAllValues();

        String mess = allEvents.get(6).getMessageInJsonFormat();
        MessageActionLeft messageActionLeft = new Gson().fromJson(allEvents.get(7).getMessageInJsonFormat(), MessageActionLeft.class);

        assertEquals(EventType.MSG_BEFORE_ENEMY_ACTION_OR_RELOAD, allEvents.get(6).getCommand());
        assertEquals(EventType.MSG_MY_N_ACTION_LEFT, allEvents.get(7).getCommand());

        assertEquals("", mess);
        assertEquals(2, messageActionLeft.getnActionsLeft());
    }


    @Test
    public void respawnInactivePlayerTest() {
        when(player0.isActive()).thenReturn(false);

        spawnController.spawnInactivePlayer(player0);

        verify(virtualView, times(4)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        List<Event> allEvents = eventCapture.getAllValues();

        MessageEnemyDrawPowerUp messageEnemyDrawPowerUp = new Gson().fromJson(allEvents.get(0).getMessageInJsonFormat(), MessageEnemyDrawPowerUp.class);

        assertEquals(EventType.MSG_ENEMY_DRAW_POWERUP, allEvents.get(0).getCommand());
        assertEquals(EventType.MSG_POWERUP_DISCARDED_TO_SPAWN, allEvents.get(1).getCommand());
        assertEquals(EventType.UPDATE_MAP, allEvents.get(2).getCommand());
        assertEquals(EventType.UPDATE_MY_POWERUPS, allEvents.get(3).getCommand());

        assertEquals(1, messageEnemyDrawPowerUp.getnCards());
        assertEquals(player0.getIdPlayer(), messageEnemyDrawPowerUp.getIdPlayer());
    }
}
