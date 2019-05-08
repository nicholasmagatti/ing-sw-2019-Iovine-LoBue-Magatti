package it.polimi.ProgettoIngSW2019.modelTest;

import it.polimi.ProgettoIngSW2019.model.GameTable;
import it.polimi.ProgettoIngSW2019.model.Maps;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.TurnManager;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author Nichoals Magatti
 */
//TODO: test this
public class TestTurnManager {

    private TurnManager turnManager;
    private GameTable gameTable;
    private Maps maps;
    private Player player1, player2, player3, player4;

    @Before
    public void setUp(){
        maps = new Maps();
        gameTable = new GameTable(maps.getMap4(),5);
        turnManager = new TurnManager(gameTable);
        player1 = new Player(0, "Nome1", gameTable);
        player2 = new Player(1, "Nome2", gameTable);
        player3 = new Player(2, "Nome3", gameTable);
        player4 = new Player(3, "Nome4", gameTable);
    }

    @Test
    public void checkDeadPlayersCorrectly(){

            player2.dealDamage(11, player1);
            player2.dealDamage(11, player3);

            List<Player> deadPlayersFound = turnManager.checkDeadPlayers();

            assertTrue(deadPlayersFound.contains(player1));
            assertTrue(deadPlayersFound.contains(player3));

            assertFalse(deadPlayersFound.contains(player2));
            assertFalse(deadPlayersFound.contains(player4));
    }

    //TODO: test distribution of points after scoring a player
    //TODO: test distribution of points after scoring the killshot track
}
