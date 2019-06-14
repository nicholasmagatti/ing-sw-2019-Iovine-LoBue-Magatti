package it.polimi.ProgettoIngSW2019.model.weapon_effects;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.common.enums.AreaOfEffect;
import it.polimi.ProgettoIngSW2019.common.enums.WeaponEffectType;
import it.polimi.ProgettoIngSW2019.custom_exception.EnemySizeLimitExceededException;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;

import javax.naming.SizeLimitExceededException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WeaponEffect {
    private Gson gson = new Gson();
    protected DistanceDictionary distance;
    private FileReader file;
    private boolean hasMoveOptions = false;
    private WeaponDmg effect;
    private WeaponMove moveDueToEffect;
    private WeaponEffectType weaponEffectType;

    /**
     * Constructor class
     * Read the weapon effect configuration json file and setup the effect
     *
     * @suthor: Luca Iovine
     */
    public WeaponEffect(JsonObject jsonObj, DistanceDictionary distance){
        effect = gson.fromJson(jsonObj.getAsJsonObject("dmg"), WeaponDmg.class);
        moveDueToEffect = gson.fromJson(jsonObj.getAsJsonObject("move"), WeaponMove.class);
        this.distance = distance;
    }

    /**
     * Set the effect type used. It can be used to know which kind of effect you are handling in order
     * to generate the correct interaction with the user.
     *
     * @param weaponEffectType enum that contains the effect type to associate
     * @suthor: Luca Iovine
     */
    //NOT TO BE TESTED
    public void setWeaponEffectType(WeaponEffectType weaponEffectType) {
        this.weaponEffectType = weaponEffectType;
    }

    /**
     * @return the effect type of the weapon
     * @suthor: Luca Iovine
     */
    //NOT TO BE TESTED
    public WeaponEffectType getWeaponEffectType() {
        return weaponEffectType;
    }

    /**
     * Based on the weapon and it's area of effect of the weapon it retrive all the enemy you can
     * hit with the weapon.
     *
     * @param fromPlayer the player who uses the weapon
     * @return a list of Player who are the enemy hittable
     * @suthor: Luca Iovine
     */
    //TESTED --> generalEffectGetEnemyListTest
    public List<Player> getEnemyList(Player fromPlayer) {
        List<Square> squareVisible = distance.getTargetPosition(effect.getAoe(), fromPlayer.getPosition());
        List<Player> enemyList = new ArrayList<>();

        for(Square s: squareVisible)
            enemyList.addAll(s.getPlayerOnSquare());

        if(enemyList.contains(fromPlayer))
            enemyList.remove(fromPlayer);

        return enemyList;
    }

    /**
     * It calculates all the Player in the area of effect passed as parameter of the player from where
     * it is calculated, its included.
     *
     * @param fromPlayer player from where the enemy is visible in the area of effect. It could be differente from the weapon user.
     * @param aoe enum that indicates the area of effect you are going to see
     * @return a list of Player in the area
     * @suthor: Luca Iovine
     */
    //TESTED --> generalEffectGetEnemyListKnowingAOETest
    //TODO: da spostare in square (?)
    public List<Player> getPlayerInTheArea(Player fromPlayer, AreaOfEffect aoe) {
        List<Square> squareVisible = distance.getTargetPosition(aoe, fromPlayer.getPosition());
        List<Player> enemyList = new ArrayList<>();

        for(Square s: squareVisible)
            enemyList.addAll(s.getPlayerOnSquare());

        return enemyList;
    }

    /**
     * @param player who used the weapon
     * @param enemy who could be moved due to the effect
     * @return the list of position the player can move
     * @suthor: Luca Iovine
     */
    //NOT TO BE TESTED
    public List<Square> getMovementList(Player player, Player enemy){
        return new ArrayList<>();
    }

    /**
     * It activate the effect of the weapon doing damage and marking the players in the enemyChosenList.
     *
     * @param player user player
     * @param enemyList list of the enemy chosen from the user
     * @throws SizeLimitExceededException whenever the enemy size list is greater than expected
     * @suthor: Luca Iovine
     */
    //TESTED --> generalActivateEffectFailsTooManyPlayersTest
    public void activateEffect(Player player, List<Player> enemyList) throws SizeLimitExceededException{
        if(enemyList.size() <= effect.getNrTarget()) {
            for (Player enemy: enemyList) {
                player.dealDamage(effect.getDmg(), enemy);
                player.markPlayer(effect.getMark(), enemy);
            }
        }
        else{
            throw new SizeLimitExceededException();
        }
    }

    /*
        TESTED --> generalCheckEnemyValidityTest
                   generalCheckEnemyValidityCatchExceptionTest
                   generalCheckEnemyValidityWrongEnemyTest
     */

    /**
     * It assert that the enemy chosen by the user are actually a correct selection.
     *
     * @param weaponUser player who used the weapon
     * @param enemyChosenList list of player chosen by the user to be hitted
     * @return true if the enemy list is good to go, false otherwise
     * @throws EnemySizeLimitExceededException whenever the enemy size list is greater than expected
     * @suthor: Luca Iovine
     */
    public boolean checkValidityEnemy(Player weaponUser, List<Player> enemyChosenList) throws EnemySizeLimitExceededException{
        boolean result = true;
        List<Player> enemyList;

        if(enemyChosenList.size() <= effect.getNrTarget()) {
            enemyList = getEnemyList(weaponUser);

            for (Player enemyChosen : enemyChosenList) {
                if (!enemyList.contains(enemyChosen)) {
                    result = false;
                    break;
                }
            }
        }else{
            throw new EnemySizeLimitExceededException();
        }

        return result;
    }

    /**
     * It assert that the movement that the user or the enemy has to do is actually a correct selection.
     *
     * @param chosenPosition position where the user/enemy should go
     * @param enemyList is the enemy hitted by the weapon. It must be one target only
     * @return true if the position is good to go, false otherwise
     * @suthor: Luca Iovine
     */
    //NOT TO BE TESTED
    public boolean checkValidityMoveEnemy(Square chosenPosition, List<Player> enemyList) throws EnemySizeLimitExceededException{
        return true;
    }

    /**
     * To check if the user/enemy should move or not
     * @return true if it has to move, false otherwise
     * @suthor: Luca Iovine
     */
    //NOT TO BE TESTED
    public boolean hasMoveOptions(){
        return moveDueToEffect.hasToMove();
    }

    /**
     * @return the area of  effect of the weapon
     * @suthor: Luca Iovine
     */
    //NOT TO BE TESTED
    public AreaOfEffect getAoe(){
        return effect.getAoe();
    }
}

/**
 * Class that contains the primary information of the effect.
 *
 * @suthor: Luca Iovine
 */
class WeaponDmg{
    private int dmg;
    private int mark;
    private AreaOfEffect aoe;
    private int nrTarget;
    private WeaponEffectType typeOfEffect;

    public int getDmg() {
        return dmg;
    }

    public int getMark() {
        return mark;
    }

    public AreaOfEffect getAoe() {
        return aoe;
    }

    public int getNrTarget() {
        return nrTarget;
    }

    public WeaponEffectType getTypeOfEffect() {
        return typeOfEffect;
    }
}

/**
 * Class that contains the information for effect with movement.
 *
 * @suthor: Luca Iovine
 */
class WeaponMove{
    private boolean hasToMove;
    private int nrOfMovement;

    public boolean hasToMove() {
        return hasToMove;
    }

    public int getNrOfMovement() {
        return nrOfMovement;
    }
}
