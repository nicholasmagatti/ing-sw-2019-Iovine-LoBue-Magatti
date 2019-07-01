package it.polimi.ProgettoIngSW2019.modelTest.weaponTest;

import it.polimi.ProgettoIngSW2019.common.enums.WeaponEffectType;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.model.weapon_effects.WeaponEffect;
import it.polimi.ProgettoIngSW2019.modelTest.SetupMapForTest;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestRailgunEffect {
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
    public void setUpMap() {
        setup = new SetupMapForTest();
        setup.setupGeneralMap();
        setup.assignWeaponEffect("FlamethrowerEffTest.json", WeaponEffectType.RAILGUN);
        map = setup.getMap();
        weaponEffect = setup.getWeaponEffect();
    }

    @Test
    public void getEnemyListTestFromP2(){
        List<Player> enemyList = weaponEffect.getEnemyList(setup.getP3());
        List<Player> expecteEnemyList = new ArrayList<>();

        expecteEnemyList.add(setup.getP2());
        expecteEnemyList.add(setup.getP1());
        expecteEnemyList.add(setup.getP4());
        expecteEnemyList.add(setup.getP7());
        expecteEnemyList.add(setup.getP11());

        assertEquals(expecteEnemyList.size(), enemyList.size());
        assertTrue(expecteEnemyList.containsAll(enemyList));
    }
}
