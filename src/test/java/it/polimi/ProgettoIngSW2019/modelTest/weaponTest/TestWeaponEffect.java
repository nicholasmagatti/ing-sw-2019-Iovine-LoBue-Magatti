package it.polimi.ProgettoIngSW2019.modelTest.weaponTest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.common.enums.AreaOfEffect;
import it.polimi.ProgettoIngSW2019.common.enums.WeaponEffectType;
import it.polimi.ProgettoIngSW2019.custom_exception.EnemySizeLimitExceededException;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import it.polimi.ProgettoIngSW2019.model.weapon_effects.WeaponEffect;
import it.polimi.ProgettoIngSW2019.modelTest.SetupMapForTest;
import org.junit.*;

import javax.naming.SizeLimitExceededException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestWeaponEffect {
    private DistanceDictionary distance;
    private Square[][] map;
    private GameTable gt;
    private WeaponEffect weaponEffect;
    private final String hostname = "hostTest";

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
        setup.assignWeaponEffect("WeaponEffTest.json", WeaponEffectType.GENERAL);
        map = setup.getMap();
        weaponEffect = setup.getWeaponEffect();
    }

    /*
        GENERAL EFFECT
     */
    @Test
    public void generalEffectGetEnemyListTest(){
        int nrOfPlayerCanSee = 2;
        Player player = map[0][0].getPlayerOnSquare().get(0);

        List<Player> enemyActuallyPlayerCanSee = new ArrayList<>();
        enemyActuallyPlayerCanSee.addAll(map[1][0].getPlayerOnSquare());
        enemyActuallyPlayerCanSee.addAll(map[0][1].getPlayerOnSquare());

        List<Player> enemyList = weaponEffect.getEnemyList(player);

        assertTrue(enemyList.size() == nrOfPlayerCanSee);
        assertTrue(enemyList.containsAll(enemyActuallyPlayerCanSee));

    }

    @Test
    public void generalEffectGetPlayerInTheAreaTest(){
        int nrOfPlayerCanSee = 3;
        Player player = map[0][0].getPlayerOnSquare().get(0);

        List<Player> actualPlayerCanSee = new ArrayList<>();
        actualPlayerCanSee.add(player);
        actualPlayerCanSee.addAll(map[1][0].getPlayerOnSquare());
        actualPlayerCanSee.addAll(map[0][1].getPlayerOnSquare());

        List<Player> enemyList = weaponEffect.getPlayerInTheArea(player, AreaOfEffect.CAN_SEE);

        assertTrue(enemyList.size() == nrOfPlayerCanSee);
        assertTrue(enemyList.containsAll(actualPlayerCanSee));

    }

    @Test
    public void generalActivateEffectFailsTooManyPlayersTest(){
        Player player = map[0][0].getPlayerOnSquare().get(0);
        List<Player> enemyList = new ArrayList<>();

        enemyList.addAll(map[1][0].getPlayerOnSquare());
        enemyList.addAll(map[1][1].getPlayerOnSquare());
        enemyList.addAll(map[1][2].getPlayerOnSquare());
        enemyList.addAll(map[1][3].getPlayerOnSquare());
        enemyList.addAll(map[2][3].getPlayerOnSquare());

        try {
            weaponEffect.activateEffect(player, enemyList);
            fail();
        } catch (EnemySizeLimitExceededException e) {
        }
    }

    @Test
    public void generalCheckEnemyValidityTest(){
        Player player = map[0][0].getPlayerOnSquare().get(0);
        List<Player> enemyChosen = new ArrayList<>();

        enemyChosen.addAll(map[1][0].getPlayerOnSquare());

        try{
            boolean result = weaponEffect.checkValidityEnemy(player, enemyChosen);
            if(!result)
                fail();
        }catch(EnemySizeLimitExceededException e){
            fail();
        }
    }

    @Test
    public void generalCheckEnemyValidityCatchExceptionTest(){
        Player player = map[0][0].getPlayerOnSquare().get(0);
        List<Player> enemyChosen = new ArrayList<>();

        enemyChosen.addAll(map[1][0].getPlayerOnSquare());
        enemyChosen.addAll(map[0][1].getPlayerOnSquare());

        try{
            boolean result = weaponEffect.checkValidityEnemy(player, enemyChosen);
            fail();
        }catch(EnemySizeLimitExceededException e){

        }
    }

    @Test
    public void generalCheckEnemyValidityWrongEnemyTest(){
        Player player = map[0][0].getPlayerOnSquare().get(0);
        List<Player> enemyChosen = new ArrayList<>();

        enemyChosen.addAll(map[2][1].getPlayerOnSquare());

        try{
            boolean result = weaponEffect.checkValidityEnemy(player, enemyChosen);
            if(result)
                fail();
        }catch(EnemySizeLimitExceededException e){
            fail();
        }
    }
}
