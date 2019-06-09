package it.polimi.ProgettoIngSW2019.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.common.enums.*;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import it.polimi.ProgettoIngSW2019.model.weapon_effects.WeaponEffect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe WeaponCard
 *
 * @author Luca Iovine
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
     * @author Luca Iovine
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
     * @author Luca Iovine
     */
    public String getName(){
        return this.name;
    }

    /**
     * @return description of the weapon
     * @author Luca Iovine
     */
    public String getDescription(){
        return this.description;
    }

    /**
     * @return ammo need to reload the weapon
     * @author Luca Iovine
     */
    public List<AmmoType> getreloadCost(){
        return this.reloadCost;
    }

    /**
     * @return ammo need to buy the weapon
     * @author Luca Iovine
     */
    public List<AmmoType> getBuyCost(){
        List<AmmoType> buyCost = new ArrayList<>();
        for(int i = 0; i < reloadCost.size() - 1; i++)
            buyCost.add(reloadCost.get(i));

        return buyCost;
    }

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
        WeaponEffectType type = WeaponEffectType.fromString(jsonObj.get("typeOfEffect").toString());
        //TODO: testare l'assegnamento a type (che va a null)
        /*switch(type){
            case GENERAL:
                baseEffect = new WeaponEffect(jsonObj,distance);
                break;
        }*/
    }
    /**
     * Every list of the list represent all the player in a certain square.
     *
     * @param fromSquare position from where you want to know the target visible for that specific weapon
     * @return all the target visible to be hitted by the weapon
     * @author: Luca Iovine
     */

    public List<Player> getEnemyList(Square fromSquare) {
        return baseEffect.getEnemyList(fromSquare);
    }

    public List<Square> getMovementList(Player weaponUser){
        return baseEffect.getMovementList(weaponUser);
    }

    public void activateBaseEff(Player weaponUser, List<Player> enemyList){
        baseEffect.activateEffect(weaponUser, enemyList);
    }

    public boolean checkBaseEffectParameterValidity(Player weaponUser, List<Player> enemyChosenList){
        return baseEffect.checkValidityEnemy(weaponUser, enemyChosenList);
    }

    public boolean checkBaseEffectMovementPositionValidity(Square position){
        return baseEffect.checkValidityMoveUserPlayer(position);
    }

    public AreaOfEffect getBaseEffectAoe(){
        return baseEffect.getAoe();
    }

    public boolean isEnemyMoveInBaseEffect(){
        return baseEffect.isEnemyMove();
    }

    public boolean hasToMoveInBaseEffect(){
        return baseEffect.hasMoveOptions();
    }

    public WeaponEffectType getBaseEffectType(){
        return baseEffect.getWeaponEffectType();
    }
}
