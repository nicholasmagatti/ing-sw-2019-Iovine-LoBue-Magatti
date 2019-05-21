package it.polimi.ProgettoIngSW2019.modelTest;

import it.polimi.ProgettoIngSW2019.model.GameTable;
import it.polimi.ProgettoIngSW2019.model.Maps;
import it.polimi.ProgettoIngSW2019.model.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


public class TestGameTable {

    private GameTable gameTable;
    private Maps maps = new Maps();
    final int NR_SKULLS = 8;
    Player carlo, giulia, alessia, ermes;


    @Before
    public void setUp(){

        gameTable = new GameTable(maps.getMap3(), NR_SKULLS);

        List<String> names = new ArrayList<>();
        names.add("Carlo");
        names.add("Giulia");
        names.add("Alessia");
        names.add("Ermes");
        gameTable.setPlayersBeforeStart(names);
        carlo = gameTable.getPlayers()[0];
        giulia = gameTable.getPlayers()[1];
        alessia = gameTable.getPlayers()[2];
        ermes = gameTable.getPlayers()[3];

    }

    @Test
    public void setGameTableCorrectly(){
        gameTable = new GameTable(maps.getMap1(), 7);
        gameTable = new GameTable(maps.getMap2(), 8);
        gameTable = new GameTable(maps.getMap3(), 5);
        gameTable = new GameTable(maps.getMap4(), 6);
    }

    @Test
    public void setGameTableIncorrectly(){

        try {
            gameTable = new GameTable(maps.getMap1(), 2);
            fail();
        }
        catch (IllegalArgumentException e){
            System.out.println(e);
        }

        try {
            gameTable = new GameTable(maps.getMap3(), 9);
            fail();
        }
        catch (IllegalArgumentException e){
            System.out.println(e);
        }

    }

    @Test
    public void numberSkullsCorrect(){
        assertTrue(gameTable.initialNumberOfSkulls() == NR_SKULLS);
    }

    @Test
    public void addTokenOnKillshotTrackCorrectly(){
        final int K1 = 5;
        final int K2 = NR_SKULLS - K1;
        final int DAMAGE_TO_KILL = 11;
        assertFalse(gameTable.isKillshotTrackFull());
        ermes.dealDamage(DAMAGE_TO_KILL, carlo);
        for(int i=0; i < K1; i++) {
            gameTable.addTokenOnKillshotTrack(carlo, ermes);
        }
        assertEquals(K1, gameTable.getKillshotTrack().size());
        carlo.dealDamage(DAMAGE_TO_KILL, giulia);
        for(int i=0; i < K2; i++){
            gameTable.addTokenOnKillshotTrack(giulia, carlo);
        }
        assertTrue(gameTable.isKillshotTrackFull());
    }

    @Test
    public void addTokenOnKillshotTrackWithNonDeadPlayer(){
        try{
            gameTable.addTokenOnKillshotTrack(carlo,ermes);
            fail();
        }
        catch (RuntimeException e){
            System.out.println(e);
        }
    }

}
