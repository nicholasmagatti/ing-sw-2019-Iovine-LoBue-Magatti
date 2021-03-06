package it.polimi.ProgettoIngSW2019.modelTest;

import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.model.*;

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
    private Player player1, player2, player3, player4;
    private int[] pointsExpected;
    private int[] pointsActuallyAssigned;

    @Before
    public void setUp(){
        Square[][][] maps = new Maps().getMaps();
        gameTable = new GameTable(maps[3],5);
        List<Session> sessions = new ArrayList<>();
        sessions.add(new Session("Nome1", "abc", "host"));
        sessions.add(new Session("Nome2", "123", "host"));
        sessions.add(new Session("Nome3", "3344", "host"));
        sessions.add(new Session("Nome4", "aaa", "host"));
        gameTable.setPlayersBeforeStart(sessions);
        turnManager = new TurnManager(gameTable);

        player1 = gameTable.getPlayers()[0];
        player2 = gameTable.getPlayers()[1];
        player3 = gameTable.getPlayers()[2];
        player4 = gameTable.getPlayers()[3];

        pointsExpected = new int[gameTable.getPlayers().length];
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
        player3.dealDamage(GeneralInfo.DAMAGE_TO_KILL - 1 - 3, player4);

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

        player2.dealDamage(GeneralInfo.DAMAGE_TO_KILL, player1);

        scoreBefore = player2.getScore();
        assertFalse(turnManager.assignDoubleKillPoint());
        assertEquals(1, turnManager.checkDeadPlayers().size());
        scoreAfter = player2.getScore();

        assertEquals(scoreBefore, scoreAfter);

        ////////////////////////////////////////////
        player2.dealDamage(GeneralInfo.DAMAGE_TO_KILL, player3);

        assertTrue(turnManager.assignDoubleKillPoint());
        scoreAfter = player2.getScore();

        assertEquals(scoreBefore + 1, scoreAfter);

        ////////////////////////////////////////////
        player2.dealDamage(GeneralInfo.DAMAGE_TO_KILL, player4);

        scoreBefore = player2.getScore();
        assertTrue(turnManager.assignDoubleKillPoint());
        scoreAfter = player2.getScore();

        assertEquals(scoreBefore + 1, scoreAfter);
    }

    /*
     *
     * Test cases for scoreDamageLineOf:
     * 1) 3,3,5
     * 2) 4,6,1
     * 3) 4,6,1
     */


    @Test
    public void testScoreDamageLineZeroSkulls(){
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
        pointsExpected[0] = 6;
        pointsExpected[1] = 8;
        pointsExpected[2] = 4;
        pointsExpected[3] = 0;
        //assign the "non-extra" points
        pointsActuallyAssigned = turnManager.scoreDamageLineOf(player4);
        for(int i=0; i < pointsExpected.length; i++){
            assertEquals(pointsExpected[i], pointsActuallyAssigned[i]);
        }
        //now condiering also extra points (+1 for player1 for first blood)
        pointsExpected[0] += 1;
        for(int i=0; i < gameTable.getPlayers().length; i++){
            assertEquals(pointsExpected[i], gameTable.getPlayers()[i].getScore());
        }
    }

    @Test
    public void scoreDmLineWithSomeEnemiesMissing(){

        player1.dealDamage(11, player2);
        pointsActuallyAssigned = turnManager.scoreDamageLineOf(player2);
        pointsExpected[0] = 8;
        pointsExpected[1] = 0;
        pointsExpected[2] = 0;
        pointsExpected[3] = 0;
        for(int i=0; i < pointsExpected.length; i++){
            assertEquals(pointsExpected[i], pointsActuallyAssigned[i]);
        }
        player2.getDamageLine().clear();
        player2.increaseNumberOfSkulls();

        player1.dealDamage(11, player2);
        pointsActuallyAssigned = turnManager.scoreDamageLineOf(player2);
        pointsExpected[0] = 6;
        pointsExpected[1] = 0;
        pointsExpected[2] = 0;
        pointsExpected[3] = 0;
        for(int i=0; i < pointsExpected.length; i++){
            assertEquals(pointsExpected[i], pointsActuallyAssigned[i]);
        }
        player2.getDamageLine().clear();
        player2.increaseNumberOfSkulls();

        player3.dealDamage(11, player2);
        pointsActuallyAssigned = turnManager.scoreDamageLineOf(player2);
        pointsExpected[0] = 0;
        pointsExpected[1] = 0;
        pointsExpected[2] = 4;
        pointsExpected[3] = 0;
        for(int i=0; i < pointsExpected.length; i++){
            assertEquals(pointsExpected[i], pointsActuallyAssigned[i]);
        }
        player2.getDamageLine().clear();
        player2.increaseNumberOfSkulls();

        player1.dealDamage(11, player2);
        pointsActuallyAssigned = turnManager.scoreDamageLineOf(player2);
        pointsExpected[0] = 2;
        pointsExpected[1] = 0;
        pointsExpected[2] = 0;
        pointsExpected[3] = 0;
        for(int i=0; i < pointsExpected.length; i++){
            assertEquals(pointsExpected[i], pointsActuallyAssigned[i]);
        }
        player2.getDamageLine().clear();
        player2.increaseNumberOfSkulls();

        for(int j=0; j < 10; j++) {
            player1.dealDamage(11, player2);
            pointsActuallyAssigned = turnManager.scoreDamageLineOf(player2);
            pointsExpected[0] = 1;
            pointsExpected[1] = 0;
            pointsExpected[2] = 0;
            pointsExpected[3] = 0;
            for (int i = 0; i < pointsExpected.length; i++) {
                assertEquals(pointsExpected[i], pointsActuallyAssigned[i]);
            }
            player2.getDamageLine().clear();
            player2.increaseNumberOfSkulls();
        }



        player2.dealDamage(5, player1);
        player3.dealDamage(6, player1);
        pointsActuallyAssigned = turnManager.scoreDamageLineOf(player1);
        pointsExpected[0] = 0;
        pointsExpected[1] = 6;
        pointsExpected[2] = 8;
        pointsExpected[3] = 0;

        for(int i=0; i < pointsExpected.length; i++){
            assertEquals(pointsExpected[i], pointsActuallyAssigned[i]);
        }
        player1.getDamageLine().clear();
        player1.increaseNumberOfSkulls();

        player2.dealDamage(5, player1);
        player3.dealDamage(6, player1);
        pointsActuallyAssigned = turnManager.scoreDamageLineOf(player1);
        pointsExpected[0] = 0;
        pointsExpected[1] = 4;
        pointsExpected[2] = 6;
        pointsExpected[3] = 0;

        for(int i=0; i < pointsExpected.length; i++){
            assertEquals(pointsExpected[i], pointsActuallyAssigned[i]);
        }
        player1.getDamageLine().clear();
        player1.increaseNumberOfSkulls();

        player2.dealDamage(5, player1);
        player3.dealDamage(6, player1);
        pointsActuallyAssigned = turnManager.scoreDamageLineOf(player1);
        pointsExpected[0] = 0;
        pointsExpected[1] = 2;
        pointsExpected[2] = 4;
        pointsExpected[3] = 0;

        for(int i=0; i < pointsExpected.length; i++){
            assertEquals(pointsExpected[i], pointsActuallyAssigned[i]);
        }
        player1.getDamageLine().clear();
        player1.increaseNumberOfSkulls();

        player2.dealDamage(5, player1);
        player3.dealDamage(6, player1);
        pointsActuallyAssigned = turnManager.scoreDamageLineOf(player1);
        pointsExpected[0] = 0;
        pointsExpected[1] = 1;
        pointsExpected[2] = 2;
        pointsExpected[3] = 0;

        for(int i=0; i < pointsExpected.length; i++){
            assertEquals(pointsExpected[i], pointsActuallyAssigned[i]);
        }
        player1.getDamageLine().clear();
        player1.increaseNumberOfSkulls();

        for(int j=0; j < 10; j++) {
            player2.dealDamage(5, player1);
            player3.dealDamage(6, player1);
            pointsActuallyAssigned = turnManager.scoreDamageLineOf(player1);
            pointsExpected[0] = 0;
            pointsExpected[1] = 1;
            pointsExpected[2] = 1;
            pointsExpected[3] = 0;

            for (int i = 0; i < pointsExpected.length; i++) {
                assertEquals(pointsExpected[i], pointsActuallyAssigned[i]);
            }
            player1.getDamageLine().clear();
            player1.increaseNumberOfSkulls();
        }

    }

    //damages: p2: 6, p3: 1, p4: 4
    @Test
    public void testScoreDmLineWithSkulls(){
        player4.dealDamage(4, player1);
        player2.dealDamage(3, player1);
        player2.dealDamage(1, player1);
        player3.dealDamage(1, player1);
        player2.dealDamage(2, player1);

        //0 skulls
        //assign points( without extra points)
        //we expect 0 to p1, 8 to p2, , 6 to p4, 4 to p3
        pointsActuallyAssigned = turnManager.scoreDamageLineOf(player1);
        pointsExpected[0] = 0;
        pointsExpected[1] = 8;
        pointsExpected[3] = 6;
        pointsExpected[2] = 4;


        for(int i=0; i < pointsExpected.length; i++){
            assertEquals(pointsExpected[i], pointsActuallyAssigned[i]);
        }

        //1 skull
        player1.increaseNumberOfSkulls();
        //assign points( without extra points)
        //we expect 0 to p1, 6 to p2, , 4 to p4, 2 to p3
        pointsActuallyAssigned = turnManager.scoreDamageLineOf(player1);
        pointsExpected[1] = 6;
        pointsExpected[3] = 4;
        pointsExpected[2] = 2;


        for(int i=0; i < pointsExpected.length; i++){
            assertEquals(pointsExpected[i], pointsActuallyAssigned[i]);
        }

        //2 skulls
        player1.increaseNumberOfSkulls();

        pointsActuallyAssigned = turnManager.scoreDamageLineOf(player1);
        pointsExpected[1] = 4;
        pointsExpected[3] = 2;
        pointsExpected[2] = 1;


        for(int i=0; i < pointsExpected.length; i++){
            assertEquals(pointsExpected[i], pointsActuallyAssigned[i]);
        }

        //3 skulls
        player1.increaseNumberOfSkulls();

        pointsActuallyAssigned = turnManager.scoreDamageLineOf(player1);
        pointsExpected[1] = 2;
        pointsExpected[3] = 1;

        for(int i=0; i < pointsExpected.length; i++){
            assertEquals(pointsExpected[i], pointsActuallyAssigned[i]);
        }

        //4 skulls
        player1.increaseNumberOfSkulls();

        pointsActuallyAssigned = turnManager.scoreDamageLineOf(player1);
        pointsExpected[1] = 1;


        for(int i=0; i < pointsExpected.length; i++){
            assertEquals(pointsExpected[i], pointsActuallyAssigned[i]);
        }

        //a lot of skulls: everyone get 1 point
        for(int i=0; i < 10; i++){
            player1.increaseNumberOfSkulls();
        }


        for(int i=0; i < pointsExpected.length; i++){
            assertEquals(pointsExpected[i], pointsActuallyAssigned[i]);
        }

    }


    @Test
    public void testScoreKillshotTrack(){

        //TOKENS
        //p1: 2, p2: 6, p3: 5, p4: 2

        player3.dealDamage(GeneralInfo.DAMAGE_TO_KILL, player1);
        gameTable.addTokenOnKillshotTrack(player1, player3);
        player1.getDamageLine().clear();

        assertEquals(1, gameTable.getKillshotTrack().size());
        assertFalse(gameTable.getKillshotTrack().get(0).isOverkill());

        player1.dealDamage(GeneralInfo.DAMAGE_TO_OVERKILL, player4);
        gameTable.addTokenOnKillshotTrack(player4, player1);
        player4.getDamageLine().clear();

        assertEquals(2, gameTable.getKillshotTrack().size());
        assertTrue(gameTable.getKillshotTrack().get(1).isOverkill());

        player2.dealDamage(GeneralInfo.DAMAGE_TO_OVERKILL, player4);
        gameTable.addTokenOnKillshotTrack(player4, player2);
        player4.getDamageLine().clear();

        assertEquals(3, gameTable.getKillshotTrack().size());
        assertTrue(gameTable.getKillshotTrack().get(2).isOverkill());

        player4.dealDamage(GeneralInfo.DAMAGE_TO_OVERKILL, player1);
        gameTable.addTokenOnKillshotTrack(player1, player4);
        player1.getDamageLine().clear();

        assertEquals(4, gameTable.getKillshotTrack().size());
        assertTrue(gameTable.getKillshotTrack().get(3).isOverkill());

        for(int i=0; i < 4; i++){
            player3.dealDamage(GeneralInfo.DAMAGE_TO_KILL, player1);
            gameTable.addTokenOnKillshotTrack(player1, player3);
            player1.getDamageLine().clear();

            assertEquals(5+i, gameTable.getKillshotTrack().size());
            assertFalse(gameTable.getKillshotTrack().get(4+i).isOverkill());
        }

        assertEquals(8, gameTable.getKillshotTrack().size());
        assertFalse(gameTable.getKillshotTrack().get(7).isOverkill());

        for(int i=0; i < 2; i++){
            player2.dealDamage(GeneralInfo.DAMAGE_TO_KILL, player4);
            gameTable.addTokenOnKillshotTrack(player4, player2);
            player4.getDamageLine().clear();
        }

        assertEquals(10, gameTable.getKillshotTrack().size());
        assertFalse(gameTable.getKillshotTrack().get(8).isOverkill());
        assertFalse(gameTable.getKillshotTrack().get(9).isOverkill());

        player2.dealDamage(GeneralInfo.DAMAGE_TO_OVERKILL, player4);
        gameTable.addTokenOnKillshotTrack(player4, player2);
        player4.getDamageLine().clear();

        assertEquals(11, gameTable.getKillshotTrack().size());
        assertTrue(gameTable.getKillshotTrack().get(10).isOverkill());

        //POINTS assigned: 8 to p2, 6 to p3, 4 to p1, 2 to p4

        pointsActuallyAssigned = turnManager.scoreKillshotTrack();

        pointsExpected[1] = 8;
        pointsExpected[2] = 6;
        pointsExpected[0] = 4;
        pointsExpected[3] = 2;

        for(int i=0; i < pointsExpected.length; i++){
            assertEquals(pointsExpected[i], pointsActuallyAssigned[i]);
        }

    }

    @Test
    public void checkDeadPlayersCorrectly(){
            //at first, no dead players
            assertEquals(0, turnManager.checkDeadPlayers().size());

            player2.dealDamage(GeneralInfo.DAMAGE_TO_KILL, player1);
            player2.dealDamage(GeneralInfo.DAMAGE_TO_KILL, player3);

            List<Player> deadPlayersFound = turnManager.checkDeadPlayers();

            assertEquals(2, deadPlayersFound.size());
            assertTrue(deadPlayersFound.contains(player1));
            assertTrue(deadPlayersFound.contains(player3));

            assertFalse(deadPlayersFound.contains(player2));
            assertFalse(deadPlayersFound.contains(player4));
    }

    @Test
    public void scoreAlivePlayer(){
        player2.dealDamage(4, player1);
        pointsActuallyAssigned = turnManager.scoreDamageLineOf(player1);
        pointsExpected[0] = 0;
        pointsExpected[1] = 8;
        pointsExpected[2] = 0;
        pointsExpected[3] = 0;
        for(int i=0; i < pointsExpected.length; i++){
            assertEquals(pointsExpected[i], pointsActuallyAssigned[i]);
        }
        player3.dealDamage(2, player1);
        pointsActuallyAssigned = turnManager.scoreDamageLineOf(player1);
        pointsExpected[0] = 0;
        pointsExpected[1] = 8;
        pointsExpected[2] = 6;
        pointsExpected[3] = 0;
        for(int i=0; i < pointsExpected.length; i++){
            assertEquals(pointsExpected[i], pointsActuallyAssigned[i]);
        }
        for(int i=0; i < 2; i++)
            player1.increaseNumberOfSkulls();
        pointsActuallyAssigned = turnManager.scoreDamageLineOf(player1);
        pointsExpected[0] = 0;
        pointsExpected[1] = 4;
        pointsExpected[2] = 2;
        pointsExpected[3] = 0;
        for(int i=0; i < pointsExpected.length; i++){
            assertEquals(pointsExpected[i], pointsActuallyAssigned[i]);
        }

        for(int i=0; i < 12; i++)
            player1.increaseNumberOfSkulls();
        pointsActuallyAssigned = turnManager.scoreDamageLineOf(player1);
        pointsExpected[0] = 0;
        pointsExpected[1] = 1;
        pointsExpected[2] = 1;
        pointsExpected[3] = 0;
        for(int i=0; i < pointsExpected.length; i++){
            assertEquals(pointsExpected[i], pointsActuallyAssigned[i]);
        }
    }

    @Test
    public void testResetDecksIfNecessary(){
        Deck ammoDeck, pwUpDeck;
        ammoDeck = gameTable.getAmmoDeck();
        pwUpDeck = gameTable.getPowerUpDeck();
        List<AmmoCard> ammoCardsDiscarded = gameTable.getAmmoDiscarded();
        List<PowerUp> pwUpsDiscarded = gameTable.getPowerUpDiscarded();

        int nrAmmoCardsBefore = ammoDeck.getCards().size();
        int nrPwUpsBefore = pwUpDeck.getCards().size();

        //discard all the cards from the ammo deck
        while(!ammoDeck.getCards().isEmpty()){
            AmmoCard card = (AmmoCard) ammoDeck.getCards().get(0);
            ammoCardsDiscarded.add(card);
            ammoDeck.getCards().remove(card);
        }
        //discard some cards from the powerup deck but not all
        for(int i=0; i < 3; i++){
            PowerUp card = (PowerUp) pwUpDeck.getCards().get(0);
            pwUpsDiscarded.add(card);
            pwUpDeck.getCards().remove(card);
        }
        //the ammo deck should be reset but not the powerup deck
        turnManager.resetDecksIfNecessary();
        assertEquals(nrAmmoCardsBefore, ammoDeck.getCards().size());
        assertEquals(nrPwUpsBefore - 3, pwUpDeck.getCards().size());


        //then reset powerup deck and verify
        while(!pwUpDeck.getCards().isEmpty()){
            PowerUp card = (PowerUp) pwUpDeck.getCards().get(0);
            pwUpsDiscarded.add(card);
            pwUpDeck.getCards().remove(card);
        }

        AmmoCard card = (AmmoCard) ammoDeck.getCards().get(0);
        ammoCardsDiscarded.add(card);
        ammoDeck.getCards().remove(card);

        turnManager.resetDecksIfNecessary();

        assertEquals(nrAmmoCardsBefore - 1, ammoDeck.getCards().size());
        assertEquals(nrPwUpsBefore , pwUpDeck.getCards().size());

    }

}
