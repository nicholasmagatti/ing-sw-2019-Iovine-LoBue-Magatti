package it.polimi.ProgettoIngSW2019.modelTest;

import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import it.polimi.ProgettoIngSW2019.model.enums.AreaOfEffect;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class DictionaryDistanceTest {
    DistanceDictionary d;
    Square[][] map;
    GameTable gt;
    /*
        ---------------------
        |1S  |1   |2   |3S  |
        |P1  |P2  D P3 |P4  |
        --------D------------
        |1   |4   |4   |3   |
        |P5  |P6  |P7  D P8 |
        --------D------------
        |5S  |5   |4S  |3   |
        |P9  |P10 |P11 |P12 |
        ---------------------
     */
    @Before
    public void setUpMap(){
        map = new Square[3][4];
        gt = new GameTable(1, 5);
        map[0][0] = new SpawningPoint(1, true,true,true,true);
        map[0][1] = new AmmoPoint(1, true,false,true,false);
        map[0][2] = new AmmoPoint(2, false,true,true,true);
        map[0][3] = new SpawningPoint( 3, true,true,true,true);
        map[1][0] = new AmmoPoint( 1, true,true,true,true);
        map[1][1] = new AmmoPoint( 4, true,true,false,false);
        map[1][2] = new AmmoPoint( 4, true,false,true,true);
        map[1][3] = new AmmoPoint(3, false,true,true,true);
        map[2][0] = new SpawningPoint(5, true,true,true,true);
        map[2][1] = new AmmoPoint( 5, true,true,false,true);
        map[2][2] = new SpawningPoint( 4, true,true,true,true);
        map[2][3] = new AmmoPoint( 3, true,true,true,true);

        for(Square[] s: map){
            for(Square square: s)
                square.setDependency(map);
        }

        d = new DistanceDictionary(map);

        map[0][0].setPlayerOnSquare(new Player(0, "P1", gt, true));
        map[0][1].setPlayerOnSquare(new Player(1, "P2", gt, true));
        map[0][2].setPlayerOnSquare(new Player(2, "P3", gt, true));
        map[0][3].setPlayerOnSquare(new Player(3, "P4", gt, true));
        map[1][0].setPlayerOnSquare(new Player(4, "P5", gt, true));
        map[1][1].setPlayerOnSquare(new Player(5, "P6", gt, true));
        map[1][2].setPlayerOnSquare(new Player(6, "P7", gt, true));
        map[1][3].setPlayerOnSquare(new Player(7, "P8", gt, true));
        map[2][0].setPlayerOnSquare(new Player(8, "P9", gt, true));
        map[2][1].setPlayerOnSquare(new Player(9, "P10", gt, true));
        map[2][2].setPlayerOnSquare(new Player(10, "P11", gt, true));
        map[2][3].setPlayerOnSquare(new Player(11, "P12", gt, true));
    }

    @Test
    public void canSeeFromP2(){
        List<Square> s = d.getTargetPosition(AreaOfEffect.CAN_SEE, map[0][1]);

        assertTrue(s.contains(map[0][0]));
        assertTrue(s.contains(map[0][1]));
        assertTrue(s.contains(map[0][2]));
        assertFalse(s.contains(map[0][3]));
        assertTrue(s.contains(map[1][0]));
        assertTrue(s.contains(map[1][1]));
        assertTrue(s.contains(map[1][2]));
        assertFalse(s.contains(map[1][3]));
        assertFalse(s.contains(map[2][0]));
        assertFalse(s.contains(map[2][1]));
        assertTrue(s.contains(map[2][2]));
        assertFalse(s.contains(map[2][3]));
    }

    @Test
    public void SameSquareP2(){
        List<Square> s = d.getTargetPosition(AreaOfEffect.SAME_SQUARE, map[0][1]);

        assertFalse(s.contains(map[0][0]));
        assertTrue(s.contains(map[0][1]));
        assertFalse(s.contains(map[0][2]));
        assertFalse(s.contains(map[0][3]));
        assertFalse(s.contains(map[1][0]));
        assertFalse(s.contains(map[1][1]));
        assertFalse(s.contains(map[1][2]));
        assertFalse(s.contains(map[1][3]));
        assertFalse(s.contains(map[2][0]));
        assertFalse(s.contains(map[2][1]));
        assertFalse(s.contains(map[2][2]));
        assertFalse(s.contains(map[2][3]));
    }

    @Test
    public void canSeeAtLeastOneFromP2(){
        List<Square> s = d.getTargetPosition(AreaOfEffect.CAN_SEE_ATLEAST_ONE, map[0][1]);

        assertTrue(s.contains(map[0][0]));
        assertFalse(s.contains(map[0][1]));
        assertTrue(s.contains(map[0][2]));
        assertFalse(s.contains(map[0][3]));
        assertTrue(s.contains(map[1][0]));
        assertTrue(s.contains(map[1][1]));
        assertTrue(s.contains(map[1][2]));
        assertFalse(s.contains(map[1][3]));
        assertFalse(s.contains(map[2][0]));
        assertFalse(s.contains(map[2][1]));
        assertTrue(s.contains(map[2][2]));
        assertFalse(s.contains(map[2][3]));
    }

    @Test
    public void canSeeAtLeastTwoFromP2(){
        List<Square> s = d.getTargetPosition(AreaOfEffect.CAN_SEE_ATLEAST_TWO, map[0][1]);

        assertFalse(s.contains(map[0][0]));
        assertFalse(s.contains(map[0][1]));
        assertFalse(s.contains(map[0][2]));
        assertFalse(s.contains(map[0][3]));
        assertTrue(s.contains(map[1][0]));
        assertFalse(s.contains(map[1][1]));
        assertTrue(s.contains(map[1][2]));
        assertFalse(s.contains(map[1][3]));
        assertFalse(s.contains(map[2][0]));
        assertFalse(s.contains(map[2][1]));
        assertTrue(s.contains(map[2][2]));
        assertFalse(s.contains(map[2][3]));
    }

    @Test
    public void cannotFromP2(){
        List<Square> s = d.getTargetPosition(AreaOfEffect.CANNOT_SEE, map[0][1]);

        assertFalse(s.contains(map[0][0]));
        assertFalse(s.contains(map[0][1]));
        assertFalse(s.contains(map[0][2]));
        assertTrue(s.contains(map[0][3]));
        assertFalse(s.contains(map[1][0]));
        assertFalse(s.contains(map[1][1]));
        assertFalse(s.contains(map[1][2]));
        assertTrue(s.contains(map[1][3]));
        assertTrue(s.contains(map[2][0]));
        assertTrue(s.contains(map[2][1]));
        assertFalse(s.contains(map[2][2]));
        assertTrue(s.contains(map[2][3]));
    }
}
