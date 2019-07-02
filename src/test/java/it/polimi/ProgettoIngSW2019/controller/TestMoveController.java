package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.LightModel.PlayerDataLM;
import it.polimi.ProgettoIngSW2019.common.Message.toController.InfoRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toController.MoveChoiceRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.MessageActionLeft;
import it.polimi.ProgettoIngSW2019.common.Message.toView.MoveInfoResponse;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.ClientMessageReceiver;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class TestMoveController {
    private final String hostname0 = "PincoHost0";
    private final String hostname1 = "PincoHost1";

    private MoveController moveController;
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


    @Before
    public void setUp() {
        maps = new Maps();

        clientMsgRcv = mock(ClientMessageReceiver.class);

        gameTable = spy(new GameTable(maps.getMaps()[0], 5));
        when(gameTable.getMap()).thenReturn(maps.getMaps()[0]);

        player0 = spy(new Player(0, "Priscilla", gameTable, hostname0));
        player1 = new Player(1, "Luca", gameTable, hostname1);

        //when(player0.getPowerUps()).thenReturn(new ArrayList<>(Arrays.asList(powerUp0, powerUp1)));

        Player[] players = new Player[2];
        players[0] = player0;
        players[1] = player1;

        when(gameTable.getPlayers()).thenReturn(players);

        turnManager = new TurnManager(gameTable);
        virtualView = spy(VirtualView.class);
        idConverter = new IdConverter(gameTable);
        createJson = new CreateJson(turnManager);
        hostNameCreateList = new HostNameCreateList(turnManager);

        moveController = new MoveController(turnManager, virtualView, idConverter, createJson, hostNameCreateList);

        virtualView.addObserver(moveController);
        virtualView.registerMessageReceiver(hostname0, clientMsgRcv);
        virtualView.registerMessageReceiver(hostname1, clientMsgRcv);
    }


    @Test
    public void moveRequestInfoCorrect() {
        gameTable.getMap()[0][2].addPlayerOnSquare(player0);
        Square square = gameTable.getMap()[0][2];
        when(player0.getPosition()).thenReturn(square);

        List<int[]> coordinates = new ArrayList<>();
        for(int i = 0 ; i < GeneralInfo.ROWS_MAP; i++) {
            for(int j = 0; j < GeneralInfo.COLUMNS_MAP; j++) {
                if((i==0) && (j==0 || j==1 || j==2)) {
                    int[] coordinates1 = new int[2];
                    coordinates1[0] = i;
                    coordinates1[1] = j;
                    coordinates.add(coordinates1);
                }

                if(i==1) {
                    int[] coordinates2 = new int[2];
                    coordinates2[0] = i;
                    coordinates2[1] = j;
                    coordinates.add(coordinates2);
                }

                if((i==2) && (j==1 || j==3)) {
                    int[] coordinates3 = new int[2];
                    coordinates3[0] = i;
                    coordinates3[1] = j;
                    coordinates.add(coordinates3);
                }
            }
        }

        EventType eventType = EventType.REQUEST_MOVE_INFO;
        InfoRequest infoRequest = new InfoRequest(player0.getHostname(), player0.getIdPlayer());
        Event eventRequestInfo = new Event(eventType, new Gson().toJson(infoRequest));

        virtualView.forwardEvent(eventRequestInfo);

        verify(virtualView, times(1)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());

        List<Event> allEvents = eventCapture.getAllValues();

        MoveInfoResponse moveInfoResponse = new Gson().fromJson(allEvents.get(0).getMessageInJsonFormat(), MoveInfoResponse.class);

        for(int[] c:coordinates) {
            System.out.print(c[0]);
            System.out.println(c[1]);
        }

        System.out.println("----");

        for(int[] c:moveInfoResponse.getCoordinates()) {
            System.out.print(c[0]);
            System.out.println(c[1]);
        }

        int result = 0;

        assertEquals(coordinates.size(), moveInfoResponse.getCoordinates().size());

        for (int[] c:coordinates) {
            for(int[] c1:moveInfoResponse.getCoordinates()) {
                if(c[0] == c1[0] && c[1] == c1[1]) {
                    result++;
                }
            }
        }

        assertEquals(coordinates.size(), result);
        assertEquals(EventType.RESPONSE_REQUEST_MOVE_INFO, allEvents.get(0).getCommand());
    }


    @Test
    public void moveRequestCorrect() {
        gameTable.getMap()[0][2].addPlayerOnSquare(player0);
        Square square = gameTable.getMap()[0][2];
        when(player0.getPosition()).thenReturn(square);

        EventType eventType = EventType.REQUEST_MOVE_INFO;
        InfoRequest infoRequest = new InfoRequest(player0.getHostname(), player0.getIdPlayer());
        Event eventRequestInfo = new Event(eventType, new Gson().toJson(infoRequest));

        virtualView.forwardEvent(eventRequestInfo);

        int[] coordinates = new int[2];
        coordinates[0] = 1;
        coordinates[1] = 1;
        EventType eventType1 = EventType.REQUEST_MOVE;
        MoveChoiceRequest moveChoiceRequest = new MoveChoiceRequest(player0.getHostname(), player0.getIdPlayer(), coordinates);
        Event event = new Event(eventType1, new Gson().toJson(moveChoiceRequest));

        virtualView.forwardEvent(event);

        verify(virtualView, times(5)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());
        List<Event> allEvents = eventCapture.getAllValues();

        MessageActionLeft messageActionLeft = new Gson().fromJson(allEvents.get(4).getMessageInJsonFormat(), MessageActionLeft.class);

        reset(player0);
        assertEquals(player0.getPosition().getSquareType(), gameTable.getMap()[1][1].getSquareType());
        assertEquals(player0.getPosition().getIdRoom(), gameTable.getMap()[1][1].getIdRoom());

        assertEquals(1, messageActionLeft.getnActionsLeft());
    }



    @Test
    public void moveRequestWrong() {
        gameTable.getMap()[0][2].addPlayerOnSquare(player0);
        Square square = gameTable.getMap()[0][2];
        when(player0.getPosition()).thenReturn(square);

        EventType eventType = EventType.REQUEST_MOVE_INFO;
        InfoRequest infoRequest = new InfoRequest(player0.getHostname(), player0.getIdPlayer());
        Event eventRequestInfo = new Event(eventType, new Gson().toJson(infoRequest));

        virtualView.forwardEvent(eventRequestInfo);

        int[] coordinates = new int[2];
        coordinates[0] = 2;
        coordinates[1] = 0;
        EventType eventType1 = EventType.REQUEST_MOVE;
        MoveChoiceRequest moveChoiceRequest = new MoveChoiceRequest(player0.getHostname(), player0.getIdPlayer(), coordinates);
        Event event = new Event(eventType1, new Gson().toJson(moveChoiceRequest));

        virtualView.forwardEvent(event);

        verify(virtualView, times(2)).sendMessage(eventCapture.capture(), hostnameListCapture.capture());

        List<Event> allEvents = eventCapture.getAllValues();

        assertEquals(EventType.ERROR, allEvents.get(1).getCommand());
        assertEquals(GeneralInfo.MSG_ERROR, allEvents.get(1).getMessageInJsonFormat());
    }
}
