package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.LightModel.PlayerDataLM;
import it.polimi.ProgettoIngSW2019.common.Message.toController.*;
import it.polimi.ProgettoIngSW2019.common.Message.toView.MessagePowerUpsDiscarded;
import it.polimi.ProgettoIngSW2019.common.Message.toView.MessageTagBackGrenade;
import it.polimi.ProgettoIngSW2019.common.Message.toView.NewtonInfoResponse;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.enums.DeckType;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.ClientMessageReceiver;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.model.powerup_effects.NewtonEff;
import it.polimi.ProgettoIngSW2019.model.powerup_effects.TagbackGrenadeEff;
import it.polimi.ProgettoIngSW2019.model.powerup_effects.TargetingScopeEff;
import it.polimi.ProgettoIngSW2019.model.powerup_effects.TeleporterEff;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.spy;

public class TestPowerUpController {
    private final String hostname0 = "PincoHost0";
    private final String hostname1 = "PincoHost1";

    private PowerUpController powerUpController;
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
    private Maps maps;
    private PowerUp pTeleporter, pTagBack, pNewton, pTargeting;


    @Before
    public void setUp() {
        maps = new Maps();

        clientMsgRcv = mock(ClientMessageReceiver.class);

        gameTable = spy(new GameTable(maps.getMaps()[0], 5));
        when(gameTable.getMap()).thenReturn(maps.getMaps()[0]);

        player0 = spy(new Player(0, "Priscilla", gameTable, hostname0));
        player1 = spy(new Player(1, "Luca", gameTable, hostname1));

        //when(player0.getPowerUps()).thenReturn(new ArrayList<>(Arrays.asList(powerUp0, powerUp1)));

        pTeleporter = new PowerUp(22, DeckType.POWERUP_CARD, AmmoType.YELLOW, GeneralInfo.TELEPORTER, "", new TeleporterEff());
        pTagBack = new PowerUp(23, DeckType.POWERUP_CARD, AmmoType.YELLOW, GeneralInfo.TAGBACK_GRENADE, "", new TagbackGrenadeEff());
        pNewton = new PowerUp(24, DeckType.POWERUP_CARD, AmmoType.YELLOW, GeneralInfo.NEWTON, "", new NewtonEff());
        pTargeting = new PowerUp(25, DeckType.POWERUP_CARD, AmmoType.YELLOW, GeneralInfo.TARGETING_SCOPE, "", new TargetingScopeEff());

        Player[] players = new Player[2];
        players[0] = player0;
        players[1] = player1;

        when(gameTable.getPlayers()).thenReturn(players);

        turnManager = new TurnManager(gameTable);
        virtualView = spy(VirtualView.class);
        idConverter = new IdConverter(gameTable);
        createJson = new CreateJson(turnManager);
        hostNameCreateList = new HostNameCreateList(turnManager);

        powerUpController = new PowerUpController(turnManager, virtualView, idConverter, createJson, hostNameCreateList);

        virtualView.addObserver(powerUpController);
        virtualView.registerMessageReceiver(hostname0, clientMsgRcv);
        virtualView.registerMessageReceiver(hostname1, clientMsgRcv);
    }


    @Test
    public void teleporterCorrectTest() {
        gameTable.getMap()[0][2].addPlayerOnSquare(player0);
        Square square = gameTable.getMap()[0][2];
        when(player0.getPosition()).thenReturn(square);
        when(player0.getPowerUps()).thenReturn(new ArrayList<>(Arrays.asList(pTeleporter)));

        int[] coordinates = new int[2];
        coordinates[0] = 1;
        coordinates[1] = 1;
        EventType eventType = EventType.TELEPORTER;
        TeleporterRequest teleporterRequest = new TeleporterRequest(player0.getHostname(), player0.getIdPlayer(), pTeleporter.getIdCard(), coordinates);
        Event event = new Event(eventType, new Gson().toJson(teleporterRequest));

        virtualView.forwardEvent(event);

        verify(virtualView, times(5)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());

        List<Event> allEvents = eventCapture.getAllValues();

        assertEquals(EventType.MSG_USE_POWERUP, allEvents.get(0).getCommand());
        assertEquals(EventType.UPDATE_MAP, allEvents.get(1).getCommand());
        assertEquals(EventType.UPDATE_MY_POWERUPS, allEvents.get(2).getCommand());
        assertEquals(EventType.MSG_BEFORE_ENEMY_ACTION_OR_RELOAD, allEvents.get(3).getCommand());
        assertEquals(EventType.MSG_MY_N_ACTION_LEFT, allEvents.get(4).getCommand());

        MessagePowerUpsDiscarded messagePowerUpsDiscarded = new Gson().fromJson(allEvents.get(0).getMessageInJsonFormat(), MessagePowerUpsDiscarded.class);

        assertEquals(pTeleporter.getName(), messagePowerUpsDiscarded.getPowerUpsToDiscard().get(0).getName());
    }


    @Test
    public void teleporterWrongPPNameTest() {
        gameTable.getMap()[0][2].addPlayerOnSquare(player0);
        Square square = gameTable.getMap()[0][2];
        when(player0.getPosition()).thenReturn(square);
        when(player0.getPowerUps()).thenReturn(new ArrayList<>(Arrays.asList(pTagBack)));

        int[] coordinates = new int[2];
        coordinates[0] = 1;
        coordinates[1] = 1;
        EventType eventType = EventType.TELEPORTER;
        TeleporterRequest teleporterRequest = new TeleporterRequest(player0.getHostname(), player0.getIdPlayer(), pTagBack.getIdCard(), coordinates);
        Event event = new Event(eventType, new Gson().toJson(teleporterRequest));

        virtualView.forwardEvent(event);

        verify(virtualView, times(1)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());

        List<Event> allEvents = eventCapture.getAllValues();
        String messageError = allEvents.get(0).getMessageInJsonFormat();
        assertEquals(GeneralInfo.MSG_ERROR, messageError);
        assertEquals(EventType.ERROR, allEvents.get(0).getCommand());
    }


    @Test
    public void tagBackGrenadeCorrectTest() {
        gameTable.getMap()[0][2].addPlayerOnSquare(player0);
        Square square0 = gameTable.getMap()[0][2];
        when(player1.getPosition()).thenReturn(square0);
        when(player1.getPowerUps()).thenReturn(new ArrayList<>(Arrays.asList(pTagBack)));
        when(player1.getDamageLine()).thenReturn(Arrays.asList("Luca", "Nick", "Priscilla"));

        Square square1 = gameTable.getMap()[0][1];
        when(player0.getPosition()).thenReturn(square1);
        when(player0.getMarkLine()).thenReturn(Arrays.asList("Nick"));

        EventType eventType = EventType.TAGBACK_GRENADE;
        TagBackGrenadeRequest tagBackGrenadeRequest = new TagBackGrenadeRequest(player1.getHostname(), player1.getIdPlayer(), pTagBack.getIdCard(), player0.getIdPlayer());
        Event event = new Event(eventType, new Gson().toJson(tagBackGrenadeRequest));

        virtualView.forwardEvent(event);

        verify(virtualView, times(5)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        List<Event> allEvents = eventCapture.getAllValues();

        assertEquals(EventType.MSG_USE_POWERUP, allEvents.get(0).getCommand());
        assertEquals(EventType.MSG_TAGBACK_GRENADE, allEvents.get(1).getCommand());
        assertEquals(EventType.UPDATE_PLAYER_INFO, allEvents.get(2).getCommand());
        assertEquals(EventType.UPDATE_MY_POWERUPS, allEvents.get(3).getCommand());
        assertEquals(EventType.MSG_BEFORE_ENEMY_ACTION_OR_RELOAD, allEvents.get(4).getCommand());

        MessagePowerUpsDiscarded messagePowerUpsDiscarded = new Gson().fromJson(allEvents.get(0).getMessageInJsonFormat(), MessagePowerUpsDiscarded.class);
        assertEquals(pTagBack.getName(), messagePowerUpsDiscarded.getPowerUpsToDiscard().get(0).getName());

        MessageTagBackGrenade messageTagBackGrenade = new Gson().fromJson(allEvents.get(1).getMessageInJsonFormat(), MessageTagBackGrenade.class);
        assertEquals(player0.getCharaName(), messageTagBackGrenade.getNameTarget());
        assertEquals(player1.getIdPlayer(), messageTagBackGrenade.getIdPlayer());
        assertEquals(player1.getCharaName(), messageTagBackGrenade.getNamePlayer());

        PlayerDataLM updateEnemy = new Gson().fromJson(allEvents.get(2).getMessageInJsonFormat(), PlayerDataLM.class);
        assertEquals(player0.getIdPlayer(), updateEnemy.getIdPlayer());
    }


    @Test
    public void tagBackGrenadeWrongTargetIdTest() {
        gameTable.getMap()[0][2].addPlayerOnSquare(player0);
        Square square0 = gameTable.getMap()[0][2];
        when(player1.getPosition()).thenReturn(square0);
        when(player1.getPowerUps()).thenReturn(new ArrayList<>(Arrays.asList(pTagBack)));
        when(player1.getDamageLine()).thenReturn(Arrays.asList("Luca", "Nick", "Priscilla"));

        EventType eventType = EventType.TAGBACK_GRENADE;
        TagBackGrenadeRequest tagBackGrenadeRequest = new TagBackGrenadeRequest(player1.getHostname(), player1.getIdPlayer(), pTagBack.getIdCard(), player1.getIdPlayer());
        Event event = new Event(eventType, new Gson().toJson(tagBackGrenadeRequest));

        virtualView.forwardEvent(event);

        verify(virtualView, times(1)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        List<Event> allEvents = eventCapture.getAllValues();

        String messageError = allEvents.get(0).getMessageInJsonFormat();
        assertEquals(EventType.ERROR, allEvents.get(0).getCommand());
        assertEquals(GeneralInfo.MSG_ERROR, messageError);
    }


    @Test
    public void tagBackGrenadeWrongTargetNameTest() {
        gameTable.getMap()[0][2].addPlayerOnSquare(player0);
        Square square0 = gameTable.getMap()[0][2];
        when(player1.getPosition()).thenReturn(square0);
        when(player1.getPowerUps()).thenReturn(new ArrayList<>(Arrays.asList(pTagBack)));
        when(player1.getDamageLine()).thenReturn(Arrays.asList("Luca", "Nick", "Luca"));

        EventType eventType = EventType.TAGBACK_GRENADE;
        TagBackGrenadeRequest tagBackGrenadeRequest = new TagBackGrenadeRequest(player1.getHostname(), player1.getIdPlayer(), pTagBack.getIdCard(), player1.getIdPlayer());
        Event event = new Event(eventType, new Gson().toJson(tagBackGrenadeRequest));

        virtualView.forwardEvent(event);

        verify(virtualView, times(1)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        List<Event> allEvents = eventCapture.getAllValues();

        String messageError = allEvents.get(0).getMessageInJsonFormat();
        assertEquals(EventType.ERROR, allEvents.get(0).getCommand());
        assertEquals(GeneralInfo.MSG_ERROR, messageError);
    }


    @Test
    public void tagBackGrenadeWrongNameTest() {
        gameTable.getMap()[0][2].addPlayerOnSquare(player0);
        Square square0 = gameTable.getMap()[0][2];
        when(player1.getPosition()).thenReturn(square0);
        when(player1.getPowerUps()).thenReturn(new ArrayList<>(Arrays.asList(pTagBack)));

        EventType eventType = EventType.TAGBACK_GRENADE;
        TagBackGrenadeRequest tagBackGrenadeRequest = new TagBackGrenadeRequest(player1.getHostname(), player1.getIdPlayer(), pNewton.getIdCard(), player1.getIdPlayer());
        Event event = new Event(eventType, new Gson().toJson(tagBackGrenadeRequest));

        virtualView.forwardEvent(event);

        verify(virtualView, times(1)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        List<Event> allEvents = eventCapture.getAllValues();

        String messageError = allEvents.get(0).getMessageInJsonFormat();
        assertEquals(EventType.ERROR, allEvents.get(0).getCommand());
        assertEquals(GeneralInfo.MSG_ERROR, messageError);
    }


    @Test
    public void newtonInfoCorrectTest() {
        gameTable.getMap()[0][2].addPlayerOnSquare(player0);
        Square square0 = gameTable.getMap()[0][2];
        when(player0.getPowerUps()).thenReturn(new ArrayList<>(Arrays.asList(pNewton)));
        when(player1.getPosition()).thenReturn(square0);

        List<int[]> coordinatesToMove = new ArrayList<>();

        for(int i = 0; i < GeneralInfo.ROWS_MAP; i++) {
            for(int j = 0; j < GeneralInfo.COLUMNS_MAP; j++) {
                if(i==0 && (j==0 || j==1)) {
                    int[] coordinates = new int[2];
                    coordinates[0] = i;
                    coordinates[1] = j;
                    coordinatesToMove.add(coordinates);
                }

                if(i==1 && j==2) {
                    int[] coordinates = new int[2];
                    coordinates[0] = i;
                    coordinates[1] = j;
                    coordinatesToMove.add(coordinates);
                }
            }
        }

        EventType eventType = EventType.REQUEST_NEWTON_INFO;
        PowerUpChoiceRequest powerUpChoiceRequest = new PowerUpChoiceRequest(player0.getHostname(), player0.getIdPlayer(), pNewton.getIdCard());
        Event event = new Event(eventType, new Gson().toJson(powerUpChoiceRequest));

        virtualView.forwardEvent(event);

        verify(virtualView, times(1)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        List<Event> allEvents = eventCapture.getAllValues();

        NewtonInfoResponse newtonInfoResponse = new Gson().fromJson(allEvents.get(0).getMessageInJsonFormat(), NewtonInfoResponse.class);
        assertEquals(EventType.RESPONSE_NEWTON_INFO, allEvents.get(0).getCommand());

        assertEquals(coordinatesToMove.size(), newtonInfoResponse.getEnemyInfoMovement().get(0).getMovement().size());

        int result = 0;

        for (int[] c:coordinatesToMove) {
            for(int[] c1:newtonInfoResponse.getEnemyInfoMovement().get(0).getMovement()) {
                if(c[0] == c1[0] && c[1] == c1[1]) {
                    result++;
                }
            }
        }

        assertEquals(coordinatesToMove.size(), result);

        for(int[] c:coordinatesToMove) {
            System.out.print(c[0]);
            System.out.println(c[1]);
        }

        System.out.println("----");

        for(int[] c:newtonInfoResponse.getEnemyInfoMovement().get(0).getMovement()) {
            System.out.print(c[0]);
            System.out.println(c[1]);
        }
    }


    @Test
    public void newtonInfoWrongNameTest() {
        gameTable.getMap()[0][2].addPlayerOnSquare(player0);
        Square square0 = gameTable.getMap()[0][2];
        when(player0.getPowerUps()).thenReturn(new ArrayList<>(Arrays.asList(pTagBack)));
        when(player1.getPosition()).thenReturn(square0);

        EventType eventType = EventType.REQUEST_NEWTON_INFO;
        PowerUpChoiceRequest powerUpChoiceRequest = new PowerUpChoiceRequest(player0.getHostname(), player0.getIdPlayer(), pTagBack.getIdCard());
        Event event = new Event(eventType, new Gson().toJson(powerUpChoiceRequest));

        virtualView.forwardEvent(event);

        verify(virtualView, times(1)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        List<Event> allEvents = eventCapture.getAllValues();

        String messageError = allEvents.get(0).getMessageInJsonFormat();
        assertEquals(EventType.ERROR, allEvents.get(0).getCommand());
        assertEquals(GeneralInfo.MSG_ERROR, messageError);
    }


    @Test
    public void newtonCorrectTest() {
        gameTable.getMap()[0][2].addPlayerOnSquare(player0);
        Square square0 = gameTable.getMap()[0][2];
        when(player0.getPowerUps()).thenReturn(new ArrayList<>(Arrays.asList(pNewton)));
        when(player1.getPosition()).thenReturn(square0);

        int[] movement = new int[2];
        movement[0] = 1;
        movement[1] = 2;

        EventType eventType = EventType.REQUEST_NEWTON_INFO;
        PowerUpChoiceRequest powerUpChoiceRequest = new PowerUpChoiceRequest(player0.getHostname(), player0.getIdPlayer(), pNewton.getIdCard());
        Event event = new Event(eventType, new Gson().toJson(powerUpChoiceRequest));

        virtualView.forwardEvent(event);

        EventType eventType2 = EventType.NEWTON;
        NewtonRequest newtonRequest = new NewtonRequest(player0.getHostname(), player0.getIdPlayer(), pNewton.getIdCard(), player1.getIdPlayer(), movement);
        Event event2 = new Event(eventType2, new Gson().toJson(newtonRequest));

        virtualView.forwardEvent(event2);

        verify(virtualView, times(6)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        List<Event> allEvents = eventCapture.getAllValues();

        assertEquals(EventType.MSG_USE_POWERUP, allEvents.get(1).getCommand());
        assertEquals(EventType.UPDATE_MAP, allEvents.get(2).getCommand());
        assertEquals(EventType.UPDATE_MY_POWERUPS, allEvents.get(3).getCommand());
        assertEquals(EventType.MSG_BEFORE_ENEMY_ACTION_OR_RELOAD, allEvents.get(4).getCommand());
        assertEquals(EventType.MSG_MY_N_ACTION_LEFT, allEvents.get(5).getCommand());
    }


    @Test
    public void newtonWrongSquareTest() {
        gameTable.getMap()[0][2].addPlayerOnSquare(player0);
        Square square0 = gameTable.getMap()[0][2];
        when(player0.getPowerUps()).thenReturn(new ArrayList<>(Arrays.asList(pNewton)));
        when(player1.getPosition()).thenReturn(square0);

        int[] movement = new int[2];
        movement[0] = 1;
        movement[1] = 1;

        EventType eventType = EventType.REQUEST_NEWTON_INFO;
        PowerUpChoiceRequest powerUpChoiceRequest = new PowerUpChoiceRequest(player0.getHostname(), player0.getIdPlayer(), pNewton.getIdCard());
        Event event = new Event(eventType, new Gson().toJson(powerUpChoiceRequest));

        virtualView.forwardEvent(event);

        EventType eventType2 = EventType.NEWTON;
        NewtonRequest newtonRequest = new NewtonRequest(player0.getHostname(), player0.getIdPlayer(), pNewton.getIdCard(), player1.getIdPlayer(), movement);
        Event event2 = new Event(eventType2, new Gson().toJson(newtonRequest));

        virtualView.forwardEvent(event2);

        verify(virtualView, times(2)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        List<Event> allEvents = eventCapture.getAllValues();

        assertEquals(EventType.ERROR, allEvents.get(1).getCommand());
        assertEquals(GeneralInfo.MSG_ERROR, allEvents.get(1).getMessageInJsonFormat());
    }


    @Test
    public void targetingScopeCorrectAmmoTest() {
        gameTable.getMap()[0][2].addPlayerOnSquare(player0);
        Square square0 = gameTable.getMap()[0][2];
        when(player0.getPowerUps()).thenReturn(new ArrayList<>(Arrays.asList(pTargeting)));
        when(player1.getPosition()).thenReturn(square0);
        when(player1.getDamageLine()).thenReturn(new ArrayList<>(Arrays.asList("Luca", "Priscilla")));
        when(player0.getYellowAmmo()).thenReturn(2);

        EventType eventType = EventType.TARGETING_SCOPE;
        TargetingScopeRequest targetingScopeRequest = new TargetingScopeRequest(player0.getHostname(), player0.getIdPlayer(), pTargeting.getIdCard(), player1.getIdPlayer(), AmmoType.YELLOW, -1);
        Event event = new Event(eventType, new Gson().toJson(targetingScopeRequest));

        virtualView.forwardEvent(event);

        verify(virtualView, times(5)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        List<Event> allEvents = eventCapture.getAllValues();

        assertEquals(EventType.MSG_USE_POWERUP, allEvents.get(0).getCommand());
        assertEquals(EventType.MSG_TARGETING_SCOPE, allEvents.get(1).getCommand());
        assertEquals(EventType.UPDATE_PLAYER_INFO, allEvents.get(2).getCommand());
        assertEquals(EventType.UPDATE_MY_POWERUPS, allEvents.get(3).getCommand());
        assertEquals(EventType.MSG_BEFORE_ENEMY_ACTION_OR_RELOAD, allEvents.get(4).getCommand());
    }


    @Test
    public void targetingScopeCorrectPowerUpTest() {
        gameTable.getMap()[0][2].addPlayerOnSquare(player0);
        Square square0 = gameTable.getMap()[0][2];
        when(player0.getPowerUps()).thenReturn(new ArrayList<>(Arrays.asList(pTargeting, pNewton)));
        when(player1.getPosition()).thenReturn(square0);
        when(player1.getDamageLine()).thenReturn(new ArrayList<>(Arrays.asList("Luca", "Priscilla")));
        when(player0.getYellowAmmo()).thenReturn(2);

        EventType eventType = EventType.TARGETING_SCOPE;
        TargetingScopeRequest targetingScopeRequest = new TargetingScopeRequest(player0.getHostname(), player0.getIdPlayer(), pTargeting.getIdCard(), player1.getIdPlayer(), null, pNewton.getIdCard());
        Event event = new Event(eventType, new Gson().toJson(targetingScopeRequest));

        virtualView.forwardEvent(event);

        verify(virtualView, times(5)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        List<Event> allEvents = eventCapture.getAllValues();

        assertEquals(EventType.MSG_POWERUPS_DISCARDED_AS_AMMO, allEvents.get(0).getCommand());
        assertEquals(EventType.MSG_USE_POWERUP, allEvents.get(1).getCommand());
        assertEquals(EventType.MSG_TARGETING_SCOPE, allEvents.get(2).getCommand());
        assertEquals(EventType.UPDATE_MY_POWERUPS, allEvents.get(3).getCommand());
        assertEquals(EventType.MSG_BEFORE_ENEMY_ACTION_OR_RELOAD, allEvents.get(4).getCommand());
    }


    @Test
    public void targetingScopeWrongNameTest() {
        gameTable.getMap()[0][2].addPlayerOnSquare(player0);
        Square square0 = gameTable.getMap()[0][2];
        when(player0.getPowerUps()).thenReturn(new ArrayList<>(Arrays.asList(pTargeting)));
        when(player1.getPosition()).thenReturn(square0);
        when(player1.getDamageLine()).thenReturn(new ArrayList<>(Arrays.asList("Luca", "Priscilla")));
        when(player0.getYellowAmmo()).thenReturn(2);

        EventType eventType = EventType.TARGETING_SCOPE;
        TargetingScopeRequest targetingScopeRequest = new TargetingScopeRequest(player0.getHostname(), player0.getIdPlayer(), pNewton.getIdCard(), player1.getIdPlayer(), AmmoType.YELLOW, -1);
        Event event = new Event(eventType, new Gson().toJson(targetingScopeRequest));

        virtualView.forwardEvent(event);

        verify(virtualView, times(1)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        List<Event> allEvents = eventCapture.getAllValues();

        assertEquals(EventType.ERROR, allEvents.get(0).getCommand());
        assertEquals(GeneralInfo.MSG_ERROR, allEvents.get(0).getMessageInJsonFormat());
    }


    @Test
    public void targetingScopeWrongTargetTest() {
        gameTable.getMap()[0][2].addPlayerOnSquare(player0);
        Square square0 = gameTable.getMap()[0][2];
        when(player0.getPowerUps()).thenReturn(new ArrayList<>(Arrays.asList(pTargeting)));
        when(player1.getPosition()).thenReturn(square0);
        when(player1.getDamageLine()).thenReturn(new ArrayList<>(Arrays.asList("Luca", "Priscilla")));
        when(player0.getYellowAmmo()).thenReturn(2);

        EventType eventType = EventType.TARGETING_SCOPE;
        TargetingScopeRequest targetingScopeRequest = new TargetingScopeRequest(player0.getHostname(), player0.getIdPlayer(), pNewton.getIdCard(), 2, AmmoType.YELLOW, -1);
        Event event = new Event(eventType, new Gson().toJson(targetingScopeRequest));

        virtualView.forwardEvent(event);

        verify(virtualView, times(1)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        List<Event> allEvents = eventCapture.getAllValues();

        assertEquals(EventType.ERROR, allEvents.get(0).getCommand());
        assertEquals(GeneralInfo.MSG_ERROR, allEvents.get(0).getMessageInJsonFormat());
    }
}
