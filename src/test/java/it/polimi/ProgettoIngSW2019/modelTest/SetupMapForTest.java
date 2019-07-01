package it.polimi.ProgettoIngSW2019.modelTest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.common.enums.WeaponEffectType;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import it.polimi.ProgettoIngSW2019.model.weapon_effects.*;
import org.junit.Before;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class SetupMapForTest {
    private DistanceDictionary distance;
    private Square[][] map;
    private GameTable gt;
    private WeaponEffect weaponEffect;
    private final String hostname = "hostTest";

    Player p1;
    Player p2;
    Player p3;
    Player p4;
    Player p5;
    Player p6;
    Player p7;
    Player p8;
    Player p9;
    Player p10;
    Player p11;
    Player p12;

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

    public void setupGeneralMap(){
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
        map[1][3] = new AmmoPoint( 3, true,true,true,false);
        map[2][0] = new SpawningPoint( 5, true,true,true,true);
        map[2][1] = new AmmoPoint(5, false,true,true,true);
        map[2][2] = new SpawningPoint( 4, true,true,true,true);
        map[2][3] = new AmmoPoint( 3, true,true,true,true);

        for(Square[] s: map){
            for(Square square: s)
                square.setDependency(map);
        }

        p1 = new Player(1, "P1", gt, hostname);
        p1.moveTo(map[0][0]);
        p2 = new Player(2, "P2", gt, hostname);
        p2.moveTo(map[0][1]);
        p3 = new Player(3, "P3", gt, hostname);
        p3.moveTo(map[0][2]);
        p4 = new Player(4, "P4", gt, hostname);
        p4.moveTo(map[0][3]);
        p5 = new Player(5, "P5", gt, hostname);
        p5.moveTo(map[1][0]);
        p6 = new Player(6, "P6", gt, hostname);
        p6.moveTo(map[1][1]);
        p7 = new Player(7, "P7", gt, hostname);
        p7.moveTo(map[1][2]);
        p8 = new Player(8, "P8", gt, hostname);
        p8.moveTo(map[1][3]);
        p9 = new Player(9, "P9", gt, hostname);
        p9.moveTo(map[2][0]);
        p10 = new Player(10, "P10", gt, hostname);
        p10.moveTo(map[2][1]);
        p11 = new Player(11, "P11", gt, hostname);
        p11.moveTo(map[2][2]);
        p12 = new Player(12, "P12", gt, hostname);
        p12.moveTo(map[2][3]);
    }

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

    public void setupVariantMap(){
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

        p1 = new Player(1, "P1", gt, hostname);
        p1.moveTo(map[0][0]);
        p2 = new Player(2, "P2", gt, hostname);
        p2.moveTo(map[0][0]);
        p3 = new Player(3, "P3", gt, hostname);
        p3.moveTo(map[0][2]);
        p4 = new Player(4, "P4", gt, hostname);
        p4.moveTo(map[0][3]);
        p5 = new Player(5, "P5", gt, hostname);
        p5.moveTo(map[1][0]);
        p6 = new Player(6, "P6", gt, hostname);
        p6.moveTo(map[1][1]);
        p7 = new Player(7, "P7", gt, hostname);
        p7.moveTo(map[1][2]);
        p8 = new Player(8, "P8", gt, hostname);
        p8.moveTo(map[1][3]);
        p9 = new Player(9, "P9", gt, hostname);
        p9.moveTo(map[2][0]);
        p10 = new Player(10, "P10", gt, hostname);
        p10.moveTo(map[2][1]);
        p11 = new Player(11, "P11", gt, hostname);
        p11.moveTo(map[2][2]);
        p12 = new Player(12, "P12", gt, hostname);
        p12.moveTo(map[2][3]);
    }

    public void assignWeaponEffect(String filename, WeaponEffectType effectType){
        String pathOfEffectFile = new File("").getAbsolutePath()+"\\src\\test\\java\\it\\polimi\\ProgettoIngSW2019\\resourceTest\\"+filename;
        FileReader file;
        BufferedReader br = null;

        try {
            file = new FileReader(pathOfEffectFile);
            br = new BufferedReader(file);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
        JsonObject jsonObj = new Gson().fromJson(br, JsonObject.class);

        switch(effectType) {
            case GENERAL:
                weaponEffect = new WeaponEffect(jsonObj, distance);
                break;
            case FLAMETHROWER:
                weaponEffect = new FlamethrowerEffect(jsonObj, distance);
                break;
            case ONE_PER_SQUARE:
                weaponEffect = new OnePerSquareEffect(jsonObj, distance);
                break;
            case POWER_GLOVE:
                weaponEffect = new PowerGloveEffect(jsonObj, distance);
                break;
            case SAME_ROOM:
                weaponEffect = new SameRoomEffect(jsonObj, distance);
                break;
            case SHIFT_ONE_MOVEMENT:
                weaponEffect = new ShiftOneMovementEffect(jsonObj, distance);
                break;
            case TRACTOR_BEAM:
                weaponEffect = new TractorBeamEffect(jsonObj, distance);
                break;
            case VORTEX:
                weaponEffect = new VortexEffect(jsonObj, distance);
                break;
            case RAILGUN:
                weaponEffect = new RailgunEffect(jsonObj, distance);
                break;
            case HELLION:
                weaponEffect = new HellionEffect(jsonObj, distance);
                break;
        }
    }

    public Square[][] getMap() {
        return map;
    }

    public Player getP1() {
        return p1;
    }

    public Player getP2() {
        return p2;
    }

    public Player getP3() {
        return p3;
    }

    public Player getP4() {
        return p4;
    }

    public Player getP5() {
        return p5;
    }

    public Player getP6() {
        return p6;
    }

    public Player getP7() {
        return p7;
    }

    public Player getP8() {
        return p8;
    }

    public Player getP9() {
        return p9;
    }

    public Player getP10() {
        return p10;
    }

    public Player getP11() {
        return p11;
    }

    public Player getP12() {
        return p12;
    }

    public WeaponEffect getWeaponEffect() {
        return weaponEffect;
    }

    public DistanceDictionary getDistance() {
        return distance;
    }
}
