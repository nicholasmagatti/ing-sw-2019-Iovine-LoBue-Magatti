package it.polimi.ProgettoIngSW2019.model;

import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import it.polimi.ProgettoIngSW2019.model.enums.*;

import java.io.File;
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
    private List<List<Player>> targetPlayerBySquare;
    private List<WeaponEffect> weaponEffects;
    private String pathOfEffectFile;

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
        weaponEffects = new ArrayList<>();
        pathOfEffectFile = new File("").getAbsolutePath()+"\\resources\\json\\weaponeff\\"+effectFileName;
        WeaponEffect effBase = new WeaponEffect(pathOfEffectFile);
        weaponEffects.add(effBase);
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
     * Every list of the list represent all the player in a certain square.
     *
     * @param square position from where you want to know the target visible for that specific weapon
     * @return all the target visible to be hitted by the weapon
     * @suthor: Luca Iovine
     */
    public List<List<Player>> getTarget(Square square, DistanceDictionary distDictionary) {
        int idx = -1;

        List<Square> targetPos = distDictionary.getTargetPosition(weaponEffects.get(0).getEffAoe(), square);
        targetPlayerBySquare = new ArrayList<>();

        for(Square s: targetPos){
            targetPlayerBySquare.add(new ArrayList<>());
            idx++;
            for(Player p: s.getPlayerOnSquare()){
                targetPlayerBySquare.get(idx).add(p);
            }
        }

        return targetPlayerBySquare;
    }

    /**
     * Activate the base effect of the weapon.
     *
     * @param targetPlayer list of the player to be damaged by the weapon
     * @param fromPlayer player who does the damage
     * @param targetMove list of the movement of tragetted players (1:1 with targetPlayer list)
     * @param userMove movement of the player who deals damage
     * @suthor: Luca Iovine
     */
    public void useBaseEff(List<Player> targetPlayer, Player fromPlayer, List<Square> targetMove, Square userMove){
        for(Player p: targetPlayer)
            weaponEffects.get(0).activateEffect(p, fromPlayer);

        if(targetMove.size() != 0){
            for(int i = 0; i < targetMove.size(); i++)
                targetPlayer.get(i).moveTo(targetMove.get(i));
        }

        if(userMove != null) {
            fromPlayer.moveTo(userMove);
        }
    }
}
