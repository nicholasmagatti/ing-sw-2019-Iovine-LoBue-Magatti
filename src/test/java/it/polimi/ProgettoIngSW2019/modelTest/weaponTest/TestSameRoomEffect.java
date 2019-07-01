package it.polimi.ProgettoIngSW2019.modelTest.weaponTest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.common.enums.WeaponEffectType;
import it.polimi.ProgettoIngSW2019.custom_exception.EnemySizeLimitExceededException;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import it.polimi.ProgettoIngSW2019.model.weapon_effects.SameRoomEffect;
import it.polimi.ProgettoIngSW2019.model.weapon_effects.WeaponEffect;
import it.polimi.ProgettoIngSW2019.modelTest.SetupMapForTest;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestSameRoomEffect {
    private Square[][] map;
    private WeaponEffect weaponEffect;

/*
    ---------------------
    |1S  |1   |2   |3S  |
    |P1P2|-   D P3 |P4  |
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
        SetupMapForTest setup = new SetupMapForTest();
        setup.setupVariantMap();
        setup.assignWeaponEffect("SameRoomEff.json", WeaponEffectType.SAME_ROOM);
        map = setup.getMap();
        weaponEffect = setup.getWeaponEffect();
    }

    @Test
    public void checkValidityEnemySameRoomTest(){
        Player p6 = map[1][1].getPlayerOnSquare().get(0);

        List<Player> enemyChosen = new ArrayList<>();
        List<Player> enemyChosenTwo = new ArrayList<>();
        List<Player> enemyChosenFail = new ArrayList<>();
        List<Player> enemyChosenFailTwo = new ArrayList<>();

        enemyChosen.addAll(map[0][0].getPlayerOnSquare());
        enemyChosen.addAll(map[0][1].getPlayerOnSquare());
        enemyChosen.addAll(map[1][0].getPlayerOnSquare());

        enemyChosenTwo.addAll(map[2][0].getPlayerOnSquare());
        enemyChosenTwo.addAll(map[2][1].getPlayerOnSquare());

        enemyChosenFail.addAll(map[0][0].getPlayerOnSquare());

        enemyChosenFailTwo.addAll(map[0][3].getPlayerOnSquare());
        enemyChosenFailTwo.addAll(map[1][3].getPlayerOnSquare());
        enemyChosenFailTwo.addAll(map[2][3].getPlayerOnSquare());

        try {
            assertTrue(weaponEffect.checkValidityEnemy(p6, enemyChosen));
            assertTrue(weaponEffect.checkValidityEnemy(p6, enemyChosenTwo));
            assertFalse(weaponEffect.checkValidityEnemy(p6, enemyChosenFail));
            assertFalse(weaponEffect.checkValidityEnemy(p6, enemyChosenFailTwo));
        }catch(EnemySizeLimitExceededException e) {
            fail();
        }
    }
}
