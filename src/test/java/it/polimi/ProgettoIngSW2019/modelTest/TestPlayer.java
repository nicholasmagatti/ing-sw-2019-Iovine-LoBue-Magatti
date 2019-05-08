package it.polimi.ProgettoIngSW2019.modelTest;

import it.polimi.ProgettoIngSW2019.model.GameTable;
import it.polimi.ProgettoIngSW2019.model.Maps;
import it.polimi.ProgettoIngSW2019.model.Player;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Nicholas Magatti
 */
public class TestPlayer {

    private GameTable gameTable;
    private Maps maps = new Maps();
    private Player player1, player2, player3, player4;

    @Before
    public void setUp(){
        gameTable = new GameTable(maps.getMap4(),5);
        player1 = new Player(0, "Nome1", gameTable);
        player2 = new Player(1, "Nome2", gameTable);
        player3 = new Player(2, "Nome3", gameTable);
        player4 = new Player(3, "Nome4", gameTable);
    }

    @Test
    public void testDealDamageCorrectly(){
        int nDamages = 5;
        int nMarks = 3;
        player1.markPlayer(nMarks, player2);
        player1.dealDamage(nDamages, player2);
        assertEquals(nDamages + nMarks, player2.getDamageLine().size());
        for(int i=0; i < 5; i++) {
            assertEquals(player1.getCharaName(), player2.getDamageLine().get(i));
        }
    }

    @Test
    public void nullPointerTarget(){
        try {
            player2.dealDamage(3, null);
            fail();
        }
        catch(NullPointerException e){
            System.out.println(e);
        }
        try {
            player2.markPlayer(3, null);
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
        try{
            player1.markPlayer(3, player1);
            fail();
        }
        catch(IllegalArgumentException e){
            System.out.println(e);
        }
    }

    /**
     * Verify the player is put down in the two cases of overkill(12 damages) and kill(standard kill: 11 damages)
     */
    @Test
    public void verifyPlayerPutDownAfterDamage(){
        //overkill
        player1.dealDamage(12, player2);
        assertTrue(player2.isPlayerDown());
        //kill (but no overkill)
        player1.dealDamage(11, player3);
        assertTrue(player3.isPlayerDown());
        //this time the damaged player should be still alive
        player1.dealDamage(6, player4);
        assertFalse(player4.isPlayerDown());
    }
}
