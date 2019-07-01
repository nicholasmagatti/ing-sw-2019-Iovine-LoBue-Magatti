package it.polimi.ProgettoIngSW2019.modelTest.weaponTest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.common.enums.WeaponEffectType;
import it.polimi.ProgettoIngSW2019.custom_exception.EnemySizeLimitExceededException;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import it.polimi.ProgettoIngSW2019.model.weapon_effects.TractorBeamEffect;
import it.polimi.ProgettoIngSW2019.model.weapon_effects.WeaponEffect;
import it.polimi.ProgettoIngSW2019.modelTest.SetupMapForTest;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class TestTractorbeamEffect {
    private DistanceDictionary distance;
    private Square[][] map;
    private GameTable gt;
    private WeaponEffect weaponEffect;
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
    public void setUpMap(){
        SetupMapForTest setup = new SetupMapForTest();
        setup.setupVariantMap();
        setup.assignWeaponEffect("TractorbeamEff.json", WeaponEffectType.TRACTOR_BEAM);
        map = setup.getMap();
        weaponEffect = setup.getWeaponEffect();
    }

    @Test
    public void checkMovementTractorBeam(){
        List<Player> enemyp11 = new ArrayList<>();
        Player p6 = map[1][1].getPlayerOnSquare().get(0);
        enemyp11.add(map[2][2].getPlayerOnSquare().get(0));

        //Square visible:
        Square s00 = map[0][0];
        Square s01 = map[0][1];
        Square s02 = map[0][2];
        Square s11 = map[1][1];
        Square s12 = map[1][2];
        Square s13 = map[1][3];
        Square s20 = map[2][0];
        Square s21 = map[2][1];
        Square s22 = map[2][2];

        try {
            assertTrue(weaponEffect.checkValidityMoveEnemy(s00, p6, enemyp11));
            assertTrue(weaponEffect.checkValidityMoveEnemy(s01, p6, enemyp11));
            assertTrue(weaponEffect.checkValidityMoveEnemy(s02, p6, enemyp11));
            assertTrue(weaponEffect.checkValidityMoveEnemy(s11, p6, enemyp11));
            assertTrue(weaponEffect.checkValidityMoveEnemy(s12, p6, enemyp11));
            assertTrue(weaponEffect.checkValidityMoveEnemy(s13, p6, enemyp11));
            assertTrue(weaponEffect.checkValidityMoveEnemy(s20, p6, enemyp11));
            assertTrue(weaponEffect.checkValidityMoveEnemy(s21, p6, enemyp11));
            assertTrue(weaponEffect.checkValidityMoveEnemy(s22, p6, enemyp11));
        }catch(EnemySizeLimitExceededException e) {
            fail();
        }
    }

    @Test
    public void checkWRONGMovementTractorBeam(){
        List<Player> enemyp11 = new ArrayList<>();

        Player p6 = map[1][1].getPlayerOnSquare().get(0);
        enemyp11.add(map[2][2].getPlayerOnSquare().get(0));

        //Square visible:
        Square s03 = map[0][3];
        Square s10 = map[1][0];
        Square s23 = map[2][3];

        try{
            assertFalse(weaponEffect.checkValidityMoveEnemy(s03, p6, enemyp11));
            assertFalse(weaponEffect.checkValidityMoveEnemy(s10, p6, enemyp11));
            assertFalse(weaponEffect.checkValidityMoveEnemy(s23, p6, enemyp11));
        }catch(EnemySizeLimitExceededException e) {
            fail();
        }
    }
}
