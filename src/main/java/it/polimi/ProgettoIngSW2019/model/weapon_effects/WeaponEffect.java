package it.polimi.ProgettoIngSW2019.model.weapon_effects;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.common.enums.AreaOfEffect;
import it.polimi.ProgettoIngSW2019.common.enums.WeaponEffectType;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;

import java.awt.geom.Area;
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
     * Read the weapon effect file and associate to paramater
     *
     * @suthor: Luca Iovine
     */
    public WeaponEffect(JsonObject jsonObj, DistanceDictionary distance){
        effect = gson.fromJson(jsonObj.getAsJsonObject("dmg"), WeaponDmg.class);
        moveDueToEffect = gson.fromJson(jsonObj.getAsJsonObject("move"), WeaponMove.class);
        this.distance = distance;
    }

    public void setWeaponEffectType(WeaponEffectType weaponEffectType) {
        this.weaponEffectType = weaponEffectType;
    }

    public WeaponEffectType getWeaponEffectType() {
        return weaponEffectType;
    }

    public List<Player> getEnemyList(Square fromSquare) {
        List<Square> squareVisible = distance.getTargetPosition(effect.getAoe(), fromSquare);
        List<Player> enemyList = new ArrayList<>();

        for(Square s: squareVisible)
            enemyList.addAll(s.getPlayerOnSquare());

        return enemyList;
    }

    public List<Player> getEnemyList(Square fromSquare, AreaOfEffect aoe) {
        List<Square> squareVisible = distance.getTargetPosition(aoe, fromSquare);
        List<Player> enemyList = new ArrayList<>();

        for(Square s: squareVisible)
            enemyList.addAll(s.getPlayerOnSquare());

        return enemyList;
    }

    public List<Square> getMovementList(Player player){
        return new ArrayList<>();
    }

    public void activateEffect(Player player, List<Player> enemyList){
        //TODO: da cambiare con il nr di giocatori effettivamente in partita
        if(enemyList.size() > 4) {
            for (int i = 0; i < enemyList.size(); i++) {
                player.dealDamage(effect.getDmg(), enemyList.get(i));
                player.markPlayer(effect.getMark(), enemyList.get(i));
            }
        }
        else{
            //TODO: deve lanciare messaggio d'errore generico (?)
        }
    }

    public boolean checkValidityEnemy(Player weaponUser, List<Player> enemyChosenList){
        boolean result = true;

        List<Player> enemyList = getEnemyList(weaponUser.getPosition());

        for(Player enemyChosen: enemyChosenList) {
            if (!enemyList.contains(enemyChosen)) {
                result = false;
                break;
            }
        }

        return result;
    }

    public boolean checkValidityMoveUserPlayer(Square chosenPosition){
        return true;
    }

    /**
     *
     * @return true nel caso l'effetto in questione abbia opzioni di movimento con interazione utente
     */
    public boolean hasMoveOptions(){
        return moveDueToEffect.hasToMove();
    }

    public AreaOfEffect getAoe(){
        return effect.getAoe();
    }

    public boolean isEnemyMove(){
        return moveDueToEffect.isEnemyMove();
    }
}

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

class WeaponMove{
    private boolean hasToMove;
    private boolean isMandatory;
    private boolean isEnemyMove; // true if is the eneymy who have to move, false otherwise
    private int nrOfMovement;

    public boolean hasToMove() {
        return hasToMove;
    }

    public boolean isEnemyMove() {
        return isEnemyMove;
    }

    public boolean isMandatory() {
        return isMandatory;
    }

    public int getNrOfMovement() {
        return nrOfMovement;
    }
}
