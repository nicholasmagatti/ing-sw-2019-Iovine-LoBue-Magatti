package it.polimi.ProgettoIngSW2019.modelTest.weaponTest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.custom_exception.EnemySizeLimitExceededException;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import it.polimi.ProgettoIngSW2019.model.weapon_effects.ShiftOneMovementEffect;
import it.polimi.ProgettoIngSW2019.model.weapon_effects.WeaponEffect;
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
    DistanceDictionary distance;
    Square[][] map;
    GameTable gt;
    WeaponEffect weaponEffect;
    JsonObject jsonObj;

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
        map = new Square[3][4];
        gt = new GameTable(map, 5);
        distance = new DistanceDictionary(map);

        map[0][0] = new SpawningPoint(1, true, true, true, true);
        map[0][1] = new AmmoPoint(1, true, false, false, true);
        map[0][2] = new AmmoPoint(2, true, true, true, false);
        map[0][3] = new SpawningPoint(3, true, true, true, true);
        map[1][0] = new AmmoPoint(1, true, true, true, true);
        map[1][1] = new AmmoPoint(4, false, true, false, true);
        map[1][2] = new AmmoPoint(4, true, false, true, true);
        map[1][3] = new AmmoPoint(3, false, true, true, true);
        map[2][0] = new SpawningPoint(5, true, true, true, true);
        map[2][1] = new AmmoPoint(5, false, true, true, true);
        map[2][2] = new SpawningPoint(4, true, true, true, true);
        map[2][3] = new AmmoPoint(3, true, true, true, true);

        for (Square[] s : map) {
            for (Square square : s)
                square.setDependency(map);
        }

        Player p1 = new Player(1, "P1", gt);
        p1.moveTo(map[0][0]);
        Player p2 = new Player(2, "P2", gt);
        p2.moveTo(map[0][0]);
        Player p3 = new Player(3, "P3", gt);
        p3.moveTo(map[0][2]);
        Player p4 = new Player(4, "P4", gt);
        p4.moveTo(map[0][3]);
        Player p5 = new Player(5, "P5", gt);
        p5.moveTo(map[1][0]);
        Player p6 = new Player(6, "P6", gt);
        p6.moveTo(map[1][1]);
        Player p7 = new Player(7, "P7", gt);
        p7.moveTo(map[1][2]);
        Player p8 = new Player(8, "P8", gt);
        p8.moveTo(map[1][3]);
        Player p9 = new Player(9, "P9", gt);
        p9.moveTo(map[2][0]);
        Player p10 = new Player(10, "P10", gt);
        p10.moveTo(map[2][1]);
        Player p11 = new Player(11, "P11", gt);
        p11.moveTo(map[2][2]);
        Player p12 = new Player(12, "P12", gt);
        p12.moveTo(map[2][3]);

        assignWeaponEffect();
    }

    public void assignWeaponEffect() {
        String pathOfEffectFile;

        pathOfEffectFile = new File("").getAbsolutePath() + "\\src\\test\\java\\it\\polimi\\ProgettoIngSW2019\\resourceTest\\ShiftOneMovementEff.json";

        FileReader file;
        BufferedReader br = null;

        try {
            file = new FileReader(pathOfEffectFile);
            br = new BufferedReader(file);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        jsonObj = new Gson().fromJson(br, JsonObject.class);
    }

    @Test
    public void checkValidityMoveEnemyTest(){
        weaponEffect = new ShiftOneMovementEffect(jsonObj, distance);

        Player p6 = map[1][1].getPlayerOnSquare().get(0);

        List<Player> enemyChosen = new ArrayList<>();
        enemyChosen.addAll(map[1][2].getPlayerOnSquare());
        Square positionWhereP8Move1 = map[1][2];
        Square positionWhereP8Move2 = map[1][3];
        Square positionWhereP8Move3 = map[2][2];
        Square positionWhereP8Move4 = map[1][1];

        try {
            assertTrue(weaponEffect.checkValidityMoveEnemy(positionWhereP8Move1, enemyChosen));
            assertTrue(weaponEffect.checkValidityMoveEnemy(positionWhereP8Move2, enemyChosen));
            assertTrue(weaponEffect.checkValidityMoveEnemy(positionWhereP8Move3, enemyChosen));
            assertTrue(weaponEffect.checkValidityMoveEnemy(positionWhereP8Move4, enemyChosen));
        }catch(EnemySizeLimitExceededException e) {
            fail();
        }
    }

    @Test
    public void checkValidityMoveEnemyWrongTest(){
        weaponEffect = new ShiftOneMovementEffect(jsonObj, distance);

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
            assertFalse(weaponEffect.checkValidityMoveEnemy(positionWhereP8CannotMove1, enemyChosen));
            assertFalse(weaponEffect.checkValidityMoveEnemy(positionWhereP8CannotMove2, enemyChosen));
            assertFalse(weaponEffect.checkValidityMoveEnemy(positionWhereP8CannotMove3, enemyChosen));
            assertFalse(weaponEffect.checkValidityMoveEnemy(positionWhereP8CannotMove4, enemyChosen));
            assertFalse(weaponEffect.checkValidityMoveEnemy(positionWhereP8CannotMove5, enemyChosen));
            assertFalse(weaponEffect.checkValidityMoveEnemy(positionWhereP8CannotMove6, enemyChosen));
            assertFalse(weaponEffect.checkValidityMoveEnemy(positionWhereP8CannotMove7, enemyChosen));
            assertFalse(weaponEffect.checkValidityMoveEnemy(positionWhereP8CannotMove8, enemyChosen));
        }catch(EnemySizeLimitExceededException e) {
            fail();
        }
    }
}
