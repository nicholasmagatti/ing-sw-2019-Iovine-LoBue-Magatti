package it.polimi.ProgettoIngSW2019.modelTest.weaponTest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.common.enums.WeaponEffectType;
import it.polimi.ProgettoIngSW2019.custom_exception.EnemySizeLimitExceededException;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import it.polimi.ProgettoIngSW2019.model.weapon_effects.OnePerSquareEffect;
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

import static org.junit.Assert.fail;

public class TestOnePerSquare {
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
    public void setUpMap(){
        SetupMapForTest setup = new SetupMapForTest();
        setup.setupVariantMap();
        setup.assignWeaponEffect("OnePerSquareEffTest.json", WeaponEffectType.ONE_PER_SQUARE);
        map = setup.getMap();
        weaponEffect = setup.getWeaponEffect();
    }

    @Test
    public void generalCheckEnemyValidityTest(){
        Player player = map[1][0].getPlayerOnSquare().get(0);
        List<Player> enemyChosen = new ArrayList<>();

        enemyChosen.add(map[0][0].getPlayerOnSquare().get(0));

        try{
            boolean result = weaponEffect.checkValidityEnemy(player, enemyChosen);
            if(!result)
                fail();
        }catch(EnemySizeLimitExceededException e){
            fail();
        }
    }

    @Test
    public void generalCheckEnemyValidityWrongEnemyTest(){
        Player player = map[1][0].getPlayerOnSquare().get(0);
        List<Player> enemyChosen = new ArrayList<>();

        enemyChosen.addAll(map[0][0].getPlayerOnSquare());

        try{
            boolean result = weaponEffect.checkValidityEnemy(player, enemyChosen);
            if(result)
                fail();
        }catch(EnemySizeLimitExceededException e){
            fail();
        }
    }
}
