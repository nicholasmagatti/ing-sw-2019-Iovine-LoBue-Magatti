package it.polimi.ProgettoIngSW2019.model.weapon_effects;

import com.google.gson.JsonObject;
import it.polimi.ProgettoIngSW2019.common.enums.AreaOfEffect;
import it.polimi.ProgettoIngSW2019.custom_exception.EnemySizeLimitExceededException;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;

import java.util.ArrayList;
import java.util.List;

public class FlamethrowerEffect extends WeaponEffect {
    private static final int maxNrTarget = 2;
    private List<Player> northEnemyList;
    private List<Player> southEnemyList;
    private List<Player> eastEnemyList;
    private List<Player> westEnemyList;
    /**
     * Constructor class
     * Read the weapon effect file and associate to paramater
     *
     * @param jsonObj
     * @suthor: Luca Iovine
     */
    public FlamethrowerEffect(JsonObject jsonObj, DistanceDictionary distance) {
        super(jsonObj, distance);
    }

    /*
        TESTED --> flamethrowerGetEnemyList
     */
    @Override
    public List<Player> getEnemyList(Player fromPlayer){
        List<Player> enemyList = new ArrayList<>();

        northEnemyList = getNorthEnemy(fromPlayer);
        southEnemyList = getSouthEnemy(fromPlayer);
        eastEnemyList = getEastEnemy(fromPlayer);
        westEnemyList = getWestEnemy(fromPlayer);

        enemyList.addAll(northEnemyList);
        enemyList.addAll(southEnemyList);
        enemyList.addAll(eastEnemyList);
        enemyList.addAll(westEnemyList);

        return enemyList;
    }

    /**
     * It assert that the enemy chosen by the user are actually a correct selection.
     *
     * @param weaponUser player who used the weapon
     * @param enemyChosenList list of player chosen by the user to be hitted
     * @return true if the enemy list is good to go, false otherwise
     * @throws EnemySizeLimitExceededException whenever the enemy size list is greater than expected
     * @suthor: Luca Iovine
     */
    /*
        TESTED --> flamethrowerCheckEnemyValidityEASTTest
                   flamethrowerCheckEnemyValidityNORTHTest
                   flamethrowerCheckEnemyValiditySOUTHTest
                   flamethrowerCheckEnemyValidityWESTTest
     */
    @Override
    public boolean checkValidityEnemy(Player weaponUser, List<Player> enemyChosenList) throws EnemySizeLimitExceededException {
        boolean result = false;
        if(enemyChosenList.size() <= maxNrTarget) {
            if (northEnemyList.containsAll(enemyChosenList) ||
                southEnemyList.containsAll(enemyChosenList) ||
                eastEnemyList.containsAll(enemyChosenList) ||
                westEnemyList.containsAll(enemyChosenList))
                result = true;
        }else
            throw new EnemySizeLimitExceededException();
        return result;
    }

    /**
     * It calculates the player of the first to movement at north of the player.
     *
     * @param player from where it is calculated
     * @return the list of player on the frist and secondo square at north
     * @suthor: Luca Iovine
     */
    private List<Player> getNorthEnemy(Player player){
        List<Player> enemyNorthList = new ArrayList<>();
        Square northOfNorthSquare = null;
        Square northSquare = player.getPosition().getNorthSquare();

        if(northSquare != null) {
            enemyNorthList.addAll(northSquare.getPlayerOnSquare());
            northOfNorthSquare = northSquare.getNorthSquare();
        }
        if(northOfNorthSquare != null)
            enemyNorthList.addAll(northOfNorthSquare.getPlayerOnSquare());

        return enemyNorthList;
    }

    /**
     * It calculates the player of the first to movement at south of the player.
     *
     * @param player from where it is calculated
     * @return the list of player on the frist and secondo square at south
     * @suthor: Luca Iovine
     */
    private List<Player> getSouthEnemy(Player player){
        List<Player> enemySouthList = new ArrayList<>();
        Square southOfSouthSquare = null;
        Square southSquare = player.getPosition().getSouthSquare();

        if(southSquare != null) {
            enemySouthList.addAll(southSquare.getPlayerOnSquare());
            southOfSouthSquare = southSquare.getSouthSquare();
        }
        if(southOfSouthSquare != null)
            enemySouthList.addAll(southOfSouthSquare.getPlayerOnSquare());

        return enemySouthList;
    }

    /**
     * It calculates the player of the first to movement at east of the player.
     *
     * @param player from where it is calculated
     * @return the list of player on the frist and secondo square at east
     * @suthor: Luca Iovine
     */
    private List<Player> getEastEnemy(Player player){
        List<Player> enemyEastList = new ArrayList<>();
        Square eastOfEastSquare = null;
        Square eastSquare = player.getPosition().getEastSquare();

        if(eastSquare != null) {
            enemyEastList.addAll(eastSquare.getPlayerOnSquare());
            eastOfEastSquare = eastSquare.getEastSquare();
        }
        if(eastOfEastSquare != null)
            enemyEastList.addAll(eastOfEastSquare.getPlayerOnSquare());

        return enemyEastList;
    }

    /**
     * It calculates the player of the first to movement at west of the player.
     *
     * @param player from where it is calculated
     * @return the list of player on the frist and secondo square at west
     * @suthor: Luca Iovine
     */
    private List<Player> getWestEnemy(Player player){
        List<Player> enemyWestList = new ArrayList<>();
        Square westOfWestSquare = null;
        Square westSquare = player.getPosition().getWestSquare();

        if(westSquare != null) {
            enemyWestList.addAll(westSquare.getPlayerOnSquare());
            westOfWestSquare = westSquare.getWestSquare();
        }
        if(westOfWestSquare != null)
            enemyWestList.addAll(westOfWestSquare.getPlayerOnSquare());

        return enemyWestList;
    }
}
