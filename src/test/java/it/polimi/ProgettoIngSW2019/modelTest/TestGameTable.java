package it.polimi.ProgettoIngSW2019.modelTest;

import it.polimi.ProgettoIngSW2019.model.GameTable;
import it.polimi.ProgettoIngSW2019.model.Maps;
import org.junit.Test;
import static org.junit.Assert.*;


public class TestGameTable {

    private GameTable gameTable;
    private Maps maps = new Maps();


    @Test
    public void setGameTableCorrectly(){
        gameTable = new GameTable(maps.getMap1(), 7);
        /*
        gameTable = new GameTable(maps.getMap2(), 8);
        gameTable = new GameTable(maps.getMap3(), 5);
        gameTable = new GameTable(maps.getMap4(), 6);
        */
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
}
