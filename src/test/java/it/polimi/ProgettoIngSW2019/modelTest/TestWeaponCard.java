package it.polimi.ProgettoIngSW2019.modelTest;

import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.enums.DeckType;
import org.junit.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestWeaponCard {

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

        map[0][0].setPlayerOnSquare(new Player(0, "P1", gt));
        map[0][0].setPlayerOnSquare(new Player(12, "P13", gt));
        map[0][1].setPlayerOnSquare(new Player(1, "P2", gt));
        map[0][2].setPlayerOnSquare(new Player(2, "P3", gt));
        map[0][3].setPlayerOnSquare(new Player(3, "P4", gt));
        map[1][0].setPlayerOnSquare(new Player(4, "P5", gt));
        map[1][1].setPlayerOnSquare(new Player(5, "P6", gt));
        map[1][2].setPlayerOnSquare(new Player(6, "P7", gt));
        map[1][3].setPlayerOnSquare(new Player(7, "P8", gt));
        map[2][0].setPlayerOnSquare(new Player(8, "P9", gt));
        map[2][1].setPlayerOnSquare(new Player(9, "P10", gt));
        map[2][2].setPlayerOnSquare(new Player(10, "P11", gt));
        map[2][3].setPlayerOnSquare(new Player(11, "P12", gt));
    }

    @Test
    public void shouldShowPlayerSeenFromP2(){
        //WeaponEffect file must have "can_see" as aoe
        DistanceDictionary d = new DistanceDictionary(map);
        WeaponCard c = new WeaponCard(0, DeckType.WEAPON_CARD, "Test", "Test", Arrays.asList(AmmoType.BLUE, AmmoType.RED, AmmoType.RED), "PlasmaGunEff.json");
        List<List<Player>> target = c.getTarget(map[0][1], d);

        assertTrue(target.contains(map[0][0].getPlayerOnSquare()));
        assertTrue(target.contains(map[0][1].getPlayerOnSquare()));
        assertTrue(target.contains(map[0][2].getPlayerOnSquare()));
        assertFalse(target.contains(map[0][3].getPlayerOnSquare()));
        assertTrue(target.contains(map[1][0].getPlayerOnSquare()));
        assertTrue(target.contains(map[1][1].getPlayerOnSquare()));
        assertTrue(target.contains(map[1][2].getPlayerOnSquare()));
        assertFalse(target.contains(map[1][3].getPlayerOnSquare()));
        assertFalse(target.contains(map[2][0].getPlayerOnSquare()));
        assertFalse(target.contains(map[2][1].getPlayerOnSquare()));
        assertTrue(target.contains(map[2][2].getPlayerOnSquare()));
        assertFalse(target.contains(map[2][3].getPlayerOnSquare()));
    }
}
