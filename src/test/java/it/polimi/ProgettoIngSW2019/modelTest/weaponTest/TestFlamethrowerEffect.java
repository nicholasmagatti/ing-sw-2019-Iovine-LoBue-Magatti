package it.polimi.ProgettoIngSW2019.modelTest.weaponTest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.common.enums.WeaponEffectType;
import it.polimi.ProgettoIngSW2019.custom_exception.EnemySizeLimitExceededException;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import it.polimi.ProgettoIngSW2019.model.weapon_effects.FlamethrowerEffect;
import it.polimi.ProgettoIngSW2019.model.weapon_effects.WeaponEffect;
import it.polimi.ProgettoIngSW2019.modelTest.SetupMapForTest;
import javafx.scene.effect.Effect;
import org.junit.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class TestFlamethrowerEffect {
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
        SetupMapForTest setup = new SetupMapForTest();
        setup.setupGeneralMap();
        setup.assignWeaponEffect("FlamethrowerEffTest.json", WeaponEffectType.FLAMETHROWER);
        map = setup.getMap();
        weaponEffect = setup.getWeaponEffect();
    }

    @Test
    public void flamethrowerGetEnemyList(){
        Player p6 = map[1][1].getPlayerOnSquare().get(0);
        List<Player> enemyShouldList = new ArrayList<>();

        enemyShouldList.addAll(map[0][1].getPlayerOnSquare());
        enemyShouldList.addAll(map[2][1].getPlayerOnSquare());
        enemyShouldList.addAll(map[1][2].getPlayerOnSquare());
        enemyShouldList.addAll(map[1][3].getPlayerOnSquare());

        List<Player> enemyList = weaponEffect.getEnemyList(p6);

        assertTrue(enemyList.size() == 4);
        assertTrue(enemyList.containsAll(enemyShouldList));
    }

    @Test
    public void flamethrowerCheckEnemyValidityEASTTest(){
        Player p6 = map[1][1].getPlayerOnSquare().get(0);
        List<Player> enemyChosen = new ArrayList<>();

        weaponEffect.getEnemyList(p6);

        enemyChosen.addAll(map[1][2].getPlayerOnSquare());
        enemyChosen.addAll(map[1][3].getPlayerOnSquare());

        try {
            if(!weaponEffect.checkValidityEnemy(p6, enemyChosen))
                fail();
        } catch (EnemySizeLimitExceededException e) {
            fail();
        }
    }

    @Test
    public void flamethrowerCheckEnemyValidityNORTHTest(){
        Player p6 = map[1][1].getPlayerOnSquare().get(0);
        List<Player> enemyChosen = new ArrayList<>();

        weaponEffect.getEnemyList(p6);

        enemyChosen.addAll(map[0][1].getPlayerOnSquare());

        try {
            if(!weaponEffect.checkValidityEnemy(p6, enemyChosen))
                fail();
        } catch (EnemySizeLimitExceededException e) {
            fail();
        }
    }

    @Test
    public void flamethrowerCheckEnemyValiditySOUTHTest(){
        Player p2 = map[0][1].getPlayerOnSquare().get(0);
        List<Player> enemyChosen = new ArrayList<>();

        weaponEffect.getEnemyList(p2);

        enemyChosen.addAll(map[2][1].getPlayerOnSquare());
        enemyChosen.addAll(map[1][1].getPlayerOnSquare());

        try {
            if(!weaponEffect.checkValidityEnemy(p2, enemyChosen))
                fail();
        } catch (EnemySizeLimitExceededException e) {
            fail();
        }
    }

    @Test
    public void flamethrowerCheckEnemyValidityWESTTest(){
        Player p10 = map[2][1].getPlayerOnSquare().get(0);
        List<Player> enemyChosen = new ArrayList<>();

        weaponEffect.getEnemyList(p10);

        enemyChosen.addAll(map[2][0].getPlayerOnSquare());

        try {
            if(!weaponEffect.checkValidityEnemy(p10, enemyChosen))
                fail();
        } catch (EnemySizeLimitExceededException e) {
            fail();
        }
    }

}
