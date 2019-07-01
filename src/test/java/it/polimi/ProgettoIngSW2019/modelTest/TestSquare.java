package it.polimi.ProgettoIngSW2019.modelTest;

import it.polimi.ProgettoIngSW2019.common.enums.WeaponEffectType;
import it.polimi.ProgettoIngSW2019.custom_exception.NotPartOfBoardException;
import it.polimi.ProgettoIngSW2019.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestSquare {
    Square[][] map;
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
    }

    @Test
    public void shouldGetAxis0and2(){
        try {
            int axis[] = map[0][2].getCoordinates(map);
            assertEquals(0, axis[0]);
            assertEquals(2, axis[1]);
        }catch(NotPartOfBoardException e){
            fail();
        }
    }

    @Test
    public void shouldThrowNotPartOfBoardException(){
        try {
            Square s = new SpawningPoint(1, true,true,true,true);
            int axis[] = s.getCoordinates(map);
            fail();
        }catch(NotPartOfBoardException e){

        }
    }

    @Test
    public void setSurrondProperly(){
        map[0][1].setDependency(map);
        assertEquals(map[0][0], map[0][1].getWestSquare());
        assertEquals(map[0][2], map[0][1].getEastSquare());
        assertNull(map[0][1].getNorthSquare());
        assertEquals(map[1][1], map[0][1].getSouthSquare());
    }
}
