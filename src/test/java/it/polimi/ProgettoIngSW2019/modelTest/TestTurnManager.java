package it.polimi.ProgettoIngSW2019.modelTest;

import it.polimi.ProgettoIngSW2019.model.GameTable;
import it.polimi.ProgettoIngSW2019.model.Maps;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.TurnManager;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Nichoals Magatti
 */

public class TestTurnManager {

    private TurnManager turnManager;
    private GameTable gameTable;
    private Player player1, player2, player3, player4;
    private final int DAMAGE_TO_KILL = 11;
    int[] pointsAssigned;

    @Before
    public void setUp(){
        Maps maps = new Maps();
        gameTable = new GameTable(maps.getMap4(),5);
        List<String> names = new ArrayList<>();
        names.add("Nome1");
        names.add("Nome2");
        names.add("Nome3");
        names.add("Nome4");
        gameTable.setPlayersBeforeStart(names);
        turnManager = new TurnManager(gameTable);

        player1 = gameTable.getPlayers()[0];
        player2 = gameTable.getPlayers()[1];
        player3 = gameTable.getPlayers()[2];
        player4 = gameTable.getPlayers()[3];

        pointsAssigned = new int[gameTable.getPlayers().length];
    }

    @Test
    public void setNextPlayer(){
        //the first player is the current player
        player1.suspendPlayer();
        turnManager.changeCurrentPlayer();
        assertSame(player2, turnManager.getCurrentPlayer());
        turnManager.changeCurrentPlayer();
        assertSame(player3, turnManager.getCurrentPlayer());
        turnManager.changeCurrentPlayer();
        assertSame(player4, turnManager.getCurrentPlayer());
        turnManager.changeCurrentPlayer();
        assertSame(player2, turnManager.getCurrentPlayer());
        player1.reactivatePlayer();
        player3.suspendPlayer();
        player4.suspendPlayer();
        /*
        this is not supposed to happen(less than 3 active players) but
        we are just testing this method so, it is ok to do that in this test
         */
        turnManager.changeCurrentPlayer();
        assertSame(player1, turnManager.getCurrentPlayer());
        turnManager.changeCurrentPlayer();
        assertSame(player2, turnManager.getCurrentPlayer());
        player2.suspendPlayer();
        for(int i=0; i < 7; i++) {
            turnManager.changeCurrentPlayer();
            assertSame(player1, turnManager.getCurrentPlayer());
        }

        player4.reactivatePlayer();
        turnManager.changeCurrentPlayer();
        assertSame(player4, turnManager.getCurrentPlayer());
        turnManager.changeCurrentPlayer();
        assertSame(player1, turnManager.getCurrentPlayer());
        player2.reactivatePlayer();
        player3.reactivatePlayer();
        turnManager.changeCurrentPlayer();
        assertSame(player2, turnManager.getCurrentPlayer());
        turnManager.changeCurrentPlayer();
        assertSame(player3, turnManager.getCurrentPlayer());
        turnManager.changeCurrentPlayer();
        assertSame(player4, turnManager.getCurrentPlayer());
        turnManager.changeCurrentPlayer();
        assertSame(player1, turnManager.getCurrentPlayer());
    }

    @Test
    public void fromCharaNameToPlayerCorrectly(){
        assertSame(player3, turnManager.getPlayerFromCharaName("Nome3"));
        assertSame(player1, turnManager.getPlayerFromCharaName("Nome1"));
        assertSame(player2, turnManager.getPlayerFromCharaName("Nome2"));
        assertSame(player4, turnManager.getPlayerFromCharaName("Nome4"));
    }

    @Test
    public void fromCharaNameToPlayerIncorrectly(){
        try {
            turnManager.getPlayerFromCharaName("Giada");
            fail();
        }
        catch (RuntimeException e){
            System.out.println(e);
        }

        //there is "Nome1" but no "nome1" is present
        try {
            turnManager.getPlayerFromCharaName("nome1");
            fail();
        }
        catch (RuntimeException e){
            System.out.println(e);
        }

        try {
            turnManager.getPlayerFromCharaName("Nome22");
            fail();
        }
        catch (RuntimeException e){
            System.out.println(e);
        }

        try {
            turnManager.getPlayerFromCharaName("Nome2 ");
            fail();
        }
        catch (RuntimeException e){
            System.out.println(e);
        }

        try {
            turnManager.getPlayerFromCharaName("");
            fail();
        }
        catch (RuntimeException e){
            System.out.println(e);
        }
        try {
            turnManager.getPlayerFromCharaName(" ");
            fail();
        }
        catch (RuntimeException e){
            System.out.println(e);
        }
    }

    @Test
    public void testFirstBlood(){
        int scoreBefore;

        player2.dealDamage(1, player4);
        player1.dealDamage(3, player4);
        player3.dealDamage(DAMAGE_TO_KILL - 1 - 3, player4);

        scoreBefore = player2.getScore();
        assertSame(player2, turnManager.assignFirstBlood(player4));
        assertEquals(scoreBefore + 1 , player2.getScore());
    }

    @Test
    public void testDoubleKillAssignment(){
        int scoreBefore, scoreAfter;

        //set palyer2 as currentPlayer
        turnManager.changeCurrentPlayer();

        assertSame(player2, turnManager.getCurrentPlayer());

        player2.dealDamage(DAMAGE_TO_KILL, player1);

        scoreBefore = player2.getScore();
        assertFalse(turnManager.assignDoubleKillPoint());
        assertEquals(1, turnManager.checkDeadPlayers().size());
        scoreAfter = player2.getScore();

        assertEquals(scoreBefore, scoreAfter);

        ////////////////////////////////////////////
        player2.dealDamage(DAMAGE_TO_KILL, player3);

        assertTrue(turnManager.assignDoubleKillPoint());
        scoreAfter = player2.getScore();

        assertEquals(scoreBefore + 1, scoreAfter);

        ////////////////////////////////////////////
        player2.dealDamage(DAMAGE_TO_KILL, player4);

        scoreBefore = player2.getScore();
        assertTrue(turnManager.assignDoubleKillPoint());
        scoreAfter = player2.getScore();

        assertEquals(scoreBefore + 1, scoreAfter);
    }

    /*
     *
     * Test cases for scoreDamageLineOf:
     * 1) 3,3,5
     * 2) 4,4,4
     * 3) 4,6,1
     */


    @Test
    public void testScoreDamageLine(){
        assertEquals(0, player1.getScore());
        assertEquals(0, player2.getScore());
        assertEquals(0, player3.getScore());
        assertEquals(0, player4.getScore());

        player1.dealDamage(3, player4);
        player2.dealDamage(1, player4);
        player3.dealDamage(2, player4);
        player2.dealDamage(4, player4);
        player3.dealDamage(1, player4);
        //set player2 as current player
        turnManager.changeCurrentPlayer();
        //assign extra points (double kill and first blood)
        assertFalse(turnManager.assignDoubleKillPoint());
        assertSame(player1, turnManager.assignFirstBlood(player4));
        //check only player1 got points (1 for first blood)
        for(Player player : gameTable.getPlayers()){
            if(player == player1){
                assertEquals(1, player.getScore());
            }
            else {
                assertEquals(0, player.getScore());
            }
        }

        //at the end (without extra points) they should have:
        //p2: 8, p1: 6, p3: 4
        pointsAssigned[0] = 6;
        pointsAssigned[1] = 8;
        pointsAssigned[2] = 4;
        pointsAssigned[3] = 0;
        //assign the "non-extra" points
        int [] pointsActuallyAssigned = turnManager.scoreDamageLineOf(player4);
        for(int i=0; i < pointsAssigned.length; i++){
            //TODO: delete this line
            System.out.println("Points of the player1: " + player1.getScore());
            assertEquals(pointsAssigned[i], pointsActuallyAssigned[i]);
        }
        //now condiering also extra points (+1 for player1 for first blood)
        pointsAssigned[0] += 1;
        for(int i=0; i < gameTable.getPlayers().length; i++){
            //TODO: uncomment it
            /*
            assertEquals(pointsAssigned, gameTable.getPlayers()[i].getScore());
            */
        }

    }

    //TODO: test score player with skulls on the player (but not too many)

    //TODO: test score player with a lot of skulls (it should give 1 point to everyone)

    //TODO: finish testScoreDamageLine
    //TODO: scoreKillshotTrack

    @Test
    public void checkDeadPlayersCorrectly(){
            //at first, no dead players
            assertEquals(0, turnManager.checkDeadPlayers().size());

            player2.dealDamage(DAMAGE_TO_KILL, player1);
            player2.dealDamage(DAMAGE_TO_KILL, player3);

            List<Player> deadPlayersFound = turnManager.checkDeadPlayers();

            assertEquals(2, deadPlayersFound.size());
            assertTrue(deadPlayersFound.contains(player1));
            assertTrue(deadPlayersFound.contains(player3));

            assertFalse(deadPlayersFound.contains(player2));
            assertFalse(deadPlayersFound.contains(player4));
    }

}
