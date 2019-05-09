package it.polimi.ProgettoIngSW2019.modelTest;

import it.polimi.ProgettoIngSW2019.model.GameTable;
import it.polimi.ProgettoIngSW2019.model.Maps;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.TurnManager;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nichoals Magatti
 */

public class TestTurnManager {

    private TurnManager turnManager;
    private GameTable gameTable;
    private Maps maps;
    private Player player1, player2, player3, player4;

    @Before
    public void setUp(){
        maps = new Maps();
        gameTable = new GameTable(maps.getMap4(),5);
        List<String> names = new ArrayList<>();
        names.add("Nome1");
        names.add("Nome2");
        names.add("Nome3");
        names.add("Nome4");
        gameTable.setPlayersBeforeStart(names);
        turnManager = new TurnManager(gameTable);
    }

    @Test
    public void checkDeadPlayersCorrectly(){

            Player player1 = gameTable.getPlayers()[0];
            Player player2 = gameTable.getPlayers()[1];
            Player player3 = gameTable.getPlayers()[2];
            Player player4 = gameTable.getPlayers()[3];

            player2.dealDamage(11, player1);
            player2.dealDamage(11, player3);

            List<Player> deadPlayersFound = turnManager.checkDeadPlayers();

            assertEquals(2, deadPlayersFound.size());
            assertTrue(deadPlayersFound.contains(player1));
            assertTrue(deadPlayersFound.contains(player3));

            assertFalse(deadPlayersFound.contains(player2));
            assertFalse(deadPlayersFound.contains(player4));
    }

    //TODO: test distribution of points after scoring a player
    //TODO: test distribution of points after scoring the killshot track
}
