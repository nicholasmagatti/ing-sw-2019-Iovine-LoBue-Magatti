package it.polimi.ProgettoIngSW2019.modelTest;

import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import it.polimi.ProgettoIngSW2019.common.enums.AreaOfEffect;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class TestDictionaryDistance {
    private DistanceDictionary d;
    private Square[][] map;
    private GameTable gt;
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
        gt = new GameTable(map, 5);
        map[0][0] = new SpawningPoint( 1, true,true,true,true);
        map[0][1] = new AmmoPoint(1, true,false,false,true);
        map[0][2] = new AmmoPoint(2, true,true,true,false);
        map[0][3] = new SpawningPoint( 3, true,true,true,true);
        map[1][0] = new AmmoPoint( 1, true,true,true,true);
        map[1][1] = new AmmoPoint(4, false,true,false,true);
        map[1][2] = new AmmoPoint( 4, true,false,true,true);
        map[1][3] = new AmmoPoint( 3, false,true,true,true);
        map[2][0] = new SpawningPoint( 5, true,true,true,true);
        map[2][1] = new AmmoPoint(5, false,true,true,true);
        map[2][2] = new SpawningPoint( 4, true,true,true,true);
        map[2][3] = new AmmoPoint( 3, true,true,true,true);

        for(Square[] s: map){
            for(Square square: s)
                square.setDependency(map);
        }

        d = new DistanceDictionary(map);

        map[0][0].addPlayerOnSquare(new Player(0, "P1", gt, ""));
        map[0][1].addPlayerOnSquare(new Player(1, "P2", gt, ""));
        map[0][2].addPlayerOnSquare(new Player(2, "P3", gt, ""));
        map[0][3].addPlayerOnSquare(new Player(3, "P4", gt, ""));
        map[1][0].addPlayerOnSquare(new Player(4, "P5", gt, ""));
        map[1][1].addPlayerOnSquare(new Player(5, "P6", gt, ""));
        map[1][2].addPlayerOnSquare(new Player(6, "P7", gt, ""));
        map[1][3].addPlayerOnSquare(new Player(7, "P8", gt, ""));
        map[2][0].addPlayerOnSquare(new Player(8, "P9", gt, ""));
        map[2][1].addPlayerOnSquare(new Player(9, "P10", gt, ""));
        map[2][2].addPlayerOnSquare(new Player(10, "P11", gt, ""));
        map[2][3].addPlayerOnSquare(new Player(11, "P12", gt, ""));
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
    public void sameSquareP2(){
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

    @Test
    public void nearRoomFromP2(){
        List<Square> s = d.getTargetPosition(AreaOfEffect.NEAR_ROOM_VISIBLE, map[0][1]);

        assertFalse(s.contains(map[0][0]));
        assertFalse(s.contains(map[0][1]));
        assertTrue(s.contains(map[0][2]));
        assertFalse(s.contains(map[0][3]));
        assertFalse(s.contains(map[1][0]));
        assertTrue(s.contains(map[1][1]));
        assertTrue(s.contains(map[1][2]));
        assertFalse(s.contains(map[1][3]));
        assertFalse(s.contains(map[2][0]));
        assertFalse(s.contains(map[2][1]));
        assertTrue(s.contains(map[2][2]));
        assertFalse(s.contains(map[2][3]));
    }

    @Test
    public void sameRoomFromP2(){
        List<Square> s = d.getTargetPosition(AreaOfEffect.SAME_ROOM, map[0][1]);

        assertTrue(s.contains(map[0][0]));
        assertTrue(s.contains(map[0][1]));
        assertFalse(s.contains(map[0][2]));
        assertFalse(s.contains(map[0][3]));
        assertTrue(s.contains(map[1][0]));
        assertFalse(s.contains(map[1][1]));
        assertFalse(s.contains(map[1][2]));
        assertFalse(s.contains(map[1][3]));
        assertFalse(s.contains(map[2][0]));
        assertFalse(s.contains(map[2][1]));
        assertFalse(s.contains(map[2][2]));
        assertFalse(s.contains(map[2][3]));
    }

    @Test
    public void allFromP2(){
        List<Square> s = d.getTargetPosition(AreaOfEffect.ALL, map[0][1]);

        assertTrue(s.contains(map[0][0]));
        assertTrue(s.contains(map[0][1]));
        assertTrue(s.contains(map[0][2]));
        assertTrue(s.contains(map[0][3]));
        assertTrue(s.contains(map[1][0]));
        assertTrue(s.contains(map[1][1]));
        assertTrue(s.contains(map[1][2]));
        assertTrue(s.contains(map[1][3]));
        assertTrue(s.contains(map[2][0]));
        assertTrue(s.contains(map[2][1]));
        assertTrue(s.contains(map[2][2]));
        assertTrue(s.contains(map[2][3]));
    }

    @Test
    public void UpToOneFromP2(){
        List<Square> s = d.getTargetPosition(AreaOfEffect.UP_TO_ONE, map[0][1]);

        assertTrue(s.contains(map[0][0]));
        assertTrue(s.contains(map[0][1]));
        assertTrue(s.contains(map[0][2]));
        assertFalse(s.contains(map[0][3]));
        assertFalse(s.contains(map[1][0]));
        assertTrue(s.contains(map[1][1]));
        assertFalse(s.contains(map[1][2]));
        assertFalse(s.contains(map[1][3]));
        assertFalse(s.contains(map[2][0]));
        assertFalse(s.contains(map[2][1]));
        assertFalse(s.contains(map[2][2]));
        assertFalse(s.contains(map[2][3]));
    }

    @Test
    public void UpToTwoFromP6() {
        List<Square> s = d.getTargetPosition(AreaOfEffect.UP_TO_TWO, map[1][1]);

        assertTrue(s.contains(map[0][0]));
        assertTrue(s.contains(map[0][1]));
        assertTrue(s.contains(map[0][2]));
        assertFalse(s.contains(map[0][3]));
        assertFalse(s.contains(map[1][0]));
        assertTrue(s.contains(map[1][1]));
        assertTrue(s.contains(map[1][2]));
        assertTrue(s.contains(map[1][3]));
        assertTrue(s.contains(map[2][0]));
        assertTrue(s.contains(map[2][1]));
        assertTrue(s.contains(map[2][2]));
        assertFalse(s.contains(map[2][3]));
    }
}
