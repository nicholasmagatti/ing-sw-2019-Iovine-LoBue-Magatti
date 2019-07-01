package it.polimi.ProgettoIngSW2019.modelTest.weaponTest;

import it.polimi.ProgettoIngSW2019.common.enums.WeaponEffectType;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.model.weapon_effects.WeaponEffect;
import it.polimi.ProgettoIngSW2019.modelTest.SetupMapForTest;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestVortexEffect {
    SetupMapForTest setup;
    private Square[][] map;
    private WeaponEffect weaponEffect;

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
        setup = new SetupMapForTest();
        setup.setupGeneralMap();
        setup.assignWeaponEffect("VortexEffTest.json", WeaponEffectType.VORTEX);
        map = setup.getMap();
        weaponEffect = setup.getWeaponEffect();
    }

    @Test
    public void testGetEnemyListFromP6Test(){
        List<Player> enemyList = weaponEffect.getEnemyList(setup.getP6());
        List<Player> expectedEnemyList = new ArrayList<>();

        //What I can see at least one
        expectedEnemyList.add(setup.getP1());
        expectedEnemyList.add(setup.getP2());
        expectedEnemyList.add(setup.getP5());
        expectedEnemyList.add(setup.getP7());
        expectedEnemyList.add(setup.getP11());
        expectedEnemyList.add(setup.getP9());
        expectedEnemyList.add(setup.getP10());

        //due to vortex
        expectedEnemyList.add(setup.getP3());
        expectedEnemyList.add(setup.getP8());

        assertEquals(expectedEnemyList.size(), enemyList.size());
        assertTrue(expectedEnemyList.containsAll(enemyList));
    }

    @Test
    public void testGetMovementListFromP8Test(){
       List<Square> movementList = weaponEffect.getMovementList(setup.getP6(), setup.getP8());
       List<Square> expectedMovementList = new ArrayList<>();

       expectedMovementList.add(setup.getP7().getPosition());

       assertEquals(expectedMovementList.size(), movementList.size());
       assertTrue(expectedMovementList.containsAll(movementList));
    }
}
