package it.polimi.ProgettoIngSW2019.modelTest.weaponTest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.custom_exception.EnemySizeLimitExceededException;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import it.polimi.ProgettoIngSW2019.model.weapon_effects.FlamethrowerEffect;
import it.polimi.ProgettoIngSW2019.model.weapon_effects.WeaponEffect;
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
        map = new Square[3][4];
        gt = new GameTable(map, 5);
        distance = new DistanceDictionary(map);

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

        Player p1 = new Player(1, "P1", gt, hostname);
        p1.moveTo(map[0][0]);
        Player p2 = new Player(2, "P2", gt, hostname);
        p2.moveTo(map[0][1]);
        Player p3 = new Player(3, "P3", gt, hostname);
        p3.moveTo(map[0][2]);
        Player p4 = new Player(4, "P4", gt, hostname);
        p4.moveTo(map[0][3]);
        Player p5 = new Player(5, "P5", gt, hostname);
        p5.moveTo(map[1][0]);
        Player p6 = new Player(6, "P6", gt, hostname);
        p6.moveTo(map[1][1]);
        Player p7 = new Player(7, "P7", gt, hostname);
        p7.moveTo(map[1][2]);
        Player p8 = new Player(8, "P8", gt, hostname);
        p8.moveTo(map[1][3]);
        Player p9 = new Player(9, "P9", gt, hostname);
        p9.moveTo(map[2][0]);
        Player p10 = new Player(10, "P10", gt, hostname);
        p10.moveTo(map[2][1]);
        Player p11 = new Player(11, "P11", gt, hostname);
        p11.moveTo(map[2][2]);
        Player p12 = new Player(12, "P12", gt, hostname);
        p12.moveTo(map[2][3]);

        assignWeaponEffect();
    }

    private void assignWeaponEffect(){
        String pathOfEffectFile = new File("").getAbsolutePath()+"\\src\\test\\java\\it\\polimi\\ProgettoIngSW2019\\resourceTest\\FlamethrowerEffTest.json";
        FileReader file;
        BufferedReader br = null;

        try {
            file = new FileReader(pathOfEffectFile);
            br = new BufferedReader(file);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        JsonObject jsonObj = new Gson().fromJson(br, JsonObject.class);
        weaponEffect = new FlamethrowerEffect(jsonObj, distance);
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
        Player p11 = map[2][1].getPlayerOnSquare().get(0);
        List<Player> enemyChosen = new ArrayList<>();

        enemyChosen.addAll(map[0][1].getPlayerOnSquare());
        enemyChosen.addAll(map[1][1].getPlayerOnSquare());

        try {
            if(!weaponEffect.checkValidityEnemy(p11, enemyChosen))
                fail();
        } catch (EnemySizeLimitExceededException e) {
            fail();
        }
    }

    @Test
    public void flamethrowerCheckEnemyValidityWESTTest(){
        Player p11 = map[2][1].getPlayerOnSquare().get(0);
        List<Player> enemyChosen = new ArrayList<>();

        enemyChosen.addAll(map[2][0].getPlayerOnSquare());

        try {
            if(!weaponEffect.checkValidityEnemy(p11, enemyChosen))
                fail();
        } catch (EnemySizeLimitExceededException e) {
            fail();
        }
    }

}
