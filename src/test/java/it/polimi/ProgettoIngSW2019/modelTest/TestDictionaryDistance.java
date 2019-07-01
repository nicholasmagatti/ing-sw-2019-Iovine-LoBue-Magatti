package it.polimi.ProgettoIngSW2019.modelTest;

import it.polimi.ProgettoIngSW2019.common.enums.WeaponEffectType;
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
        SetupMapForTest setup = new SetupMapForTest();
        setup.setupGeneralMap();
        map = setup.getMap();
        d = setup.getDistance();
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

    @Test
    public void NorthDirectionFromP11(){
        List<Square> s = d.getTargetPosition(AreaOfEffect.NORTH_DIRECTION, map[2][2]);

        assertFalse(s.contains(map[0][0]));
        assertFalse(s.contains(map[0][1]));
        assertTrue(s.contains(map[0][2]));
        assertFalse(s.contains(map[0][3]));
        assertFalse(s.contains(map[1][0]));
        assertFalse(s.contains(map[1][1]));
        assertTrue(s.contains(map[1][2]));
        assertFalse(s.contains(map[1][3]));
        assertFalse(s.contains(map[2][0]));
        assertFalse(s.contains(map[2][1]));
        assertTrue(s.contains(map[2][2]));
        assertFalse(s.contains(map[2][3]));
    }

    @Test
    public void SouthDirectionFromP1(){
        List<Square> s = d.getTargetPosition(AreaOfEffect.SOUTH_DIRECTION, map[0][0]);

        assertTrue(s.contains(map[0][0]));
        assertFalse(s.contains(map[0][1]));
        assertFalse(s.contains(map[0][2]));
        assertFalse(s.contains(map[0][3]));
        assertTrue(s.contains(map[1][0]));
        assertFalse(s.contains(map[1][1]));
        assertFalse(s.contains(map[1][2]));
        assertFalse(s.contains(map[1][3]));
        assertTrue(s.contains(map[2][0]));
        assertFalse(s.contains(map[2][1]));
        assertFalse(s.contains(map[2][2]));
        assertFalse(s.contains(map[2][3]));
    }

    @Test
    public void EastDirectionFromP11(){
        List<Square> s = d.getTargetPosition(AreaOfEffect.EAST_DIRECTION, map[2][2]);

        assertFalse(s.contains(map[0][0]));
        assertFalse(s.contains(map[0][1]));
        assertFalse(s.contains(map[0][2]));
        assertFalse(s.contains(map[0][3]));
        assertFalse(s.contains(map[1][0]));
        assertFalse(s.contains(map[1][1]));
        assertFalse(s.contains(map[1][2]));
        assertFalse(s.contains(map[1][3]));
        assertFalse(s.contains(map[2][0]));
        assertFalse(s.contains(map[2][1]));
        assertTrue(s.contains(map[2][2]));
        assertTrue(s.contains(map[2][3]));
    }

    @Test
    public void WestDirectionFromP11(){
        List<Square> s = d.getTargetPosition(AreaOfEffect.WEST_DIRECTION, map[2][2]);

        assertFalse(s.contains(map[0][0]));
        assertFalse(s.contains(map[0][1]));
        assertFalse(s.contains(map[0][2]));
        assertFalse(s.contains(map[0][3]));
        assertFalse(s.contains(map[1][0]));
        assertFalse(s.contains(map[1][1]));
        assertFalse(s.contains(map[1][2]));
        assertFalse(s.contains(map[1][3]));
        assertTrue(s.contains(map[2][0]));
        assertTrue(s.contains(map[2][1]));
        assertTrue(s.contains(map[2][2]));
        assertFalse(s.contains(map[2][3]));
    }
}
