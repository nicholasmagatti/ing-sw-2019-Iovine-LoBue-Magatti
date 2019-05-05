package it.polimi.ProgettoIngSW2019.modelTest;

import it.polimi.ProgettoIngSW2019.model.GameTable;
import it.polimi.ProgettoIngSW2019.model.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Nicholas Magatti
 */
public class TestPlayer {

    GameTable gameTable;
    Player player1, player2;

    @Before
    public void setUp(){
        gameTable = new GameTable(0,5);
        player1 = new Player(0, "Nome1", gameTable, true);
        player2 = new Player(1, "Nome2", gameTable, false);
    }

    @Test
    public void testDealDamageCorrectly(){
        player1.dealDamage(5, player2);
        assertEquals(5, player2.getDamageLine().size());
        for(int i=0; i < 5; i++) {
            assertEquals(player1.getCharaName(), player2.getDamageLine().get(i));
        }
    }

    @Test
    public void nullPointerExcDealDamage(){
        try {
            player2.dealDamage(3, null);
            fail();
        }
        catch(NullPointerException e){
            System.out.println(e);
        }
    }

    @Test
    public void exceptionOfTargetingHimself(){
        try {
            player1.dealDamage(2, player1);
        }
        catch (IllegalArgumentException e){
            System.out.println(e);
        }
    }
}
