package it.polimi.ProgettoIngSW2019.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.common.enums.*;
import it.polimi.ProgettoIngSW2019.custom_exception.EnemySizeLimitExceededException;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import it.polimi.ProgettoIngSW2019.model.weapon_effects.*;

import javax.naming.SizeLimitExceededException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe WeaponCard
 *
 * @author: Luca Iovine
 */
public class WeaponCard extends Card {
    private String name;
    private String description;
    private List<AmmoType> reloadCost;
    private WeaponEffect baseEffect;
    private String pathOfEffectFile;
    private Gson gson = new Gson();
    private DistanceDictionary distance;

    /**
     * Constructor
     *
     * @param name name of the weapon
     * @param description textual description of the card
     * @param reloadCost ammo needed to reload the weapon
     * @param effectFileName name of the file of the effect to associate
     * @author: Luca Iovine
     */
    public WeaponCard(int idCard, DeckType cardType, String name, String description, List<AmmoType> reloadCost, String effectFileName){
        super(idCard, cardType);
        this.name  = name;
        this.description = description;
        this.reloadCost = reloadCost;
        pathOfEffectFile = new File("").getAbsolutePath()+"\\resources\\json\\weaponeff\\"+effectFileName;
        setWeaponEffect(pathOfEffectFile);
    }

    /**
     * @return name of the weapon
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    public String getName(){
        return this.name;
    }

    /**
     * @return description of the weapon
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    public String getDescription(){
        return this.description;
    }

    /**
     * @return ammo need to reload the weapon
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    public List<AmmoType> getreloadCost(){
        return this.reloadCost;
    }

    /**
     * @return ammo need to buy the weapon
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    public List<AmmoType> getBuyCost(){
        List<AmmoType> buyCost = new ArrayList<>();

        buyCost.addAll(reloadCost);
        buyCost.remove(0);

        return buyCost;
    }

    /**
     * Set the weapon effect based on the "typeOfEffect" parameter in the configuration file.
     *
     * @param pathOfEffectFile path of the weapon effect json file configuration
     * @author: Luca Iovine
     */
    /*
        TESTED --> assignWeaponEffectTest
                   assignWeaponEffectWrongTest
     */
    private void setWeaponEffect(String pathOfEffectFile){
        FileReader file;
        BufferedReader br = null;
        try {
            file = new FileReader(pathOfEffectFile);
            br = new BufferedReader(file);
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        JsonObject jsonObj = gson.fromJson(br, JsonObject.class);
        WeaponEffectType type = gson.fromJson(jsonObj.get("typeOfEffect"), WeaponEffectType.class);

        if(type != null) {
            switch (type) {
                case GENERAL:
                    baseEffect = new WeaponEffect(jsonObj, distance);
                    baseEffect.setWeaponEffectType(WeaponEffectType.GENERAL);
                    break;
                case FLAMETHROWER:
                    baseEffect = new FlamethrowerEffect(jsonObj, distance);
                    baseEffect.setWeaponEffectType(WeaponEffectType.FLAMETHROWER);
                    break;
                case ONE_PER_SQUARE:
                    baseEffect = new OnePerSquareEffect(jsonObj, distance);
                    baseEffect.setWeaponEffectType(WeaponEffectType.ONE_PER_SQUARE);
                    break;
                case POWER_GLOVE:
                    baseEffect = new PowerGloveEffect(jsonObj, distance);
                    baseEffect.setWeaponEffectType(WeaponEffectType.POWER_GLOVE);
                    break;
                case SAME_ROOM:
                    baseEffect = new SameRoomEffect(jsonObj, distance);
                    baseEffect.setWeaponEffectType(WeaponEffectType.SAME_ROOM);
                    break;
                case SHIFT_ONE_MOVEMENT:
                    baseEffect = new ShiftOneMovementEffect(jsonObj, distance);
                    baseEffect.setWeaponEffectType(WeaponEffectType.SHIFT_ONE_MOVEMENT);
                    break;
                case TRACTOR_BEAM:
                    baseEffect = new TractorBeamEffect(jsonObj, distance);
                    baseEffect.setWeaponEffectType(WeaponEffectType.TRACTOR_BEAM);
                    break;
                case VORTEX:
                    baseEffect = new VortexEffect(jsonObj, distance);
                    baseEffect.setWeaponEffectType(WeaponEffectType.VORTEX);
                    break;
                case HELLION:
                    baseEffect = new HellionEffect(jsonObj, distance);
                    baseEffect.setWeaponEffectType(WeaponEffectType.HELLION);
                    break;
            }
        }else
            throw new NullPointerException();

    }

    /**
     * @param fromPlayer the weapon user who request the enemy list
     * @return the enemy list of the base effect
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    public List<Player> getEnemyList(Player fromPlayer) {
        return baseEffect.getEnemyList(fromPlayer);
    }

    /**
     * @param weaponUser the weapon user who request the enemy list
     * @param enemy the enemy that could be moved due to the effect
     * @return the movement list of user/enemey of the base effect
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    public List<Square> getMovementList(Player weaponUser, Player enemy){
        return baseEffect.getMovementList(weaponUser, enemy);
    }

    /**
     * Activate the base effect of the weapon.
     *
     * @param weaponUser the player who used the weapon
     * @param enemyList the enemy list chosen by the user
     * @throws SizeLimitExceededException thrown if the enemy chosen exceed the player it can hit
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    public void activateBaseEff(Player weaponUser, List<Player> enemyList) throws EnemySizeLimitExceededException {
        baseEffect.activateEffect(weaponUser, enemyList);
    }

    /**
     * Check that the enemy list chosen, for the base effect, is valid
     * @param weaponUser the player who used the weapon
     * @param enemyChosenList he enemy list chosen by the user
     * @return true if the chosen list are valid, false otherwise
     * @throws EnemySizeLimitExceededException thrown if the enemy chosen exceed the player it can hit
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    public boolean checkBaseEffectParameterValidity(Player weaponUser, List<Player> enemyChosenList) throws EnemySizeLimitExceededException {
        boolean result = baseEffect.checkValidityEnemy(weaponUser, enemyChosenList);

        return result;
    }

    /**
     * Check that the position chosen to move the user or the enemy, for the base effect, is valid.
     *
     * @param position square where to move the player
     * @return true if the position chosen is valid, false otherwise
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    public boolean checkBaseEffectMovementPositionValidity(Square position, List<Player> enemyChosenList) throws EnemySizeLimitExceededException{
        return baseEffect.checkValidityMoveEnemy(position, enemyChosenList);

    }

    /**
     * @return the are of effect of the base effect
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    public AreaOfEffect getBaseEffectAoe(){
        return baseEffect.getAoe();
    }

    /**
     * @return true if it base effect has a movement side effect which involve an user choice,
     * false otherwise
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    public boolean hasToMoveInBaseEffect(){
        return baseEffect.hasMoveOptions();
    }

    /**
     * @return the the base effect weapon type to know at which category it belongs.
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    public WeaponEffectType getBaseEffectType(){
        return baseEffect.getWeaponEffectType();
    }
}
