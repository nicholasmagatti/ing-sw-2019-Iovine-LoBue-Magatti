package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.model.GameTable;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.TurnManager;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;


public class TestHostNameCreateList {
    private TurnManager turnManager;
    private HostNameCreateList hostNameCreateList;
    private Player player1, player2, player3;


    @Before
    public void setUp() {
        turnManager = mock(TurnManager.class);
        hostNameCreateList = spy(new HostNameCreateList(turnManager));
        GameTable gameTable = mock(GameTable.class);
        when(turnManager.getGameTable()).thenReturn(gameTable);

        player1 = mock(Player.class);
        player2 = mock(Player.class);
        player3 = mock(Player.class);

        Player[] players = new Player[3];
        players[0] = player1;
        players[1] = player2;
        players[2] = player3;

        when(player1.getIdPlayer()).thenReturn(1);
        when(player2.getIdPlayer()).thenReturn(2);
        when(player3.getIdPlayer()).thenReturn(3);

        when(player1.getHostname()).thenReturn("HostName1");
        when(player2.getHostname()).thenReturn("HostName2");
        when(player3.getHostname()).thenReturn("HostName3");

        when(gameTable.getPlayers()).thenReturn(players);
    }


    @Test
    public void addAllHostNameTest() {
        List<String> hostNamePlayers = hostNameCreateList.addAllHostName();

        for(int i = 0; i < 3; i++) {
            assertEquals("HostName" + (i+1) +"", hostNamePlayers.get(i));
        }
    }


    @Test
    public void addOneHostNameTest() {
        List<String> hostNameCreated = hostNameCreateList.addOneHostName(player1);
        List<String> hostName = Arrays.asList(player1.getHostname());

        assertEquals(hostName.get(0), hostNameCreated.get(0));
    }


    @Test
    public void addAllExceptOneHostNameTest() {
        List<String> hostNameCreated = hostNameCreateList.addAllExceptOneHostName(player1);

        for(int i = 0; i < 2; i++) {
            assertNotEquals("HostName1", hostNameCreated.get(i));
            assertEquals("HostName" + (i+2) +"", hostNameCreated.get(i));
        }
    }
}
