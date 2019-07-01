package it.polimi.ProgettoIngSW2019.modelTest.weaponTest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.common.enums.WeaponEffectType;
import it.polimi.ProgettoIngSW2019.custom_exception.EnemySizeLimitExceededException;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import it.polimi.ProgettoIngSW2019.model.weapon_effects.ShiftOneMovementEffect;
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

public class TestShiftOneMovementEff {
    private DistanceDictionary distance;
    private Square[][] map;
    private GameTable gt;
    private WeaponEffect weaponEffect;
    private JsonObject jsonObj;
    private final String hostname = "hostTest";

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
        setup.assignWeaponEffect("ShiftOneMovementEff.json", WeaponEffectType.SHIFT_ONE_MOVEMENT);
        map = setup.getMap();
        weaponEffect = setup.getWeaponEffect();
    }

    @Test
    public void checkValidityMoveEnemyTest(){
        Player p6 = map[1][1].getPlayerOnSquare().get(0);

        List<Player> enemyChosen = new ArrayList<>();
        enemyChosen.addAll(map[1][2].getPlayerOnSquare());
        Square positionWhereP8Move1 = map[1][2];
        Square positionWhereP8Move2 = map[1][3];
        Square positionWhereP8Move3 = map[2][2];
        Square positionWhereP8Move4 = map[1][1];

        try {
            assertTrue(weaponEffect.checkValidityMoveEnemy(positionWhereP8Move1, p6, enemyChosen));
            assertTrue(weaponEffect.checkValidityMoveEnemy(positionWhereP8Move2, p6, enemyChosen));
            assertTrue(weaponEffect.checkValidityMoveEnemy(positionWhereP8Move3, p6, enemyChosen));
            assertTrue(weaponEffect.checkValidityMoveEnemy(positionWhereP8Move4, p6, enemyChosen));
        }catch(EnemySizeLimitExceededException e) {
            fail();
        }
    }

    @Test
    public void checkValidityMoveEnemyWrongTest(){
        Player p6 = map[1][1].getPlayerOnSquare().get(0);

        List<Player> enemyChosen = new ArrayList<>();
        enemyChosen.addAll(map[1][2].getPlayerOnSquare());

        Square positionWhereP8CannotMove1 = map[0][0];
        Square positionWhereP8CannotMove2 = map[0][1];
        Square positionWhereP8CannotMove3 = map[0][2];
        Square positionWhereP8CannotMove4 = map[0][3];
        Square positionWhereP8CannotMove5 = map[1][0];
        Square positionWhereP8CannotMove6 = map[2][0];
        Square positionWhereP8CannotMove7 = map[2][1];
        Square positionWhereP8CannotMove8 = map[2][3];

        try {
            assertFalse(weaponEffect.checkValidityMoveEnemy(positionWhereP8CannotMove1, p6, enemyChosen));
            assertFalse(weaponEffect.checkValidityMoveEnemy(positionWhereP8CannotMove2, p6, enemyChosen));
            assertFalse(weaponEffect.checkValidityMoveEnemy(positionWhereP8CannotMove3, p6, enemyChosen));
            assertFalse(weaponEffect.checkValidityMoveEnemy(positionWhereP8CannotMove4, p6, enemyChosen));
            assertFalse(weaponEffect.checkValidityMoveEnemy(positionWhereP8CannotMove5, p6, enemyChosen));
            assertFalse(weaponEffect.checkValidityMoveEnemy(positionWhereP8CannotMove6, p6, enemyChosen));
            assertFalse(weaponEffect.checkValidityMoveEnemy(positionWhereP8CannotMove7, p6, enemyChosen));
            assertFalse(weaponEffect.checkValidityMoveEnemy(positionWhereP8CannotMove8, p6, enemyChosen));
        }catch(EnemySizeLimitExceededException e) {
            fail();
        }
    }
}
