package it.polimi.ProgettoIngSW2019.model;

import it.polimi.ProgettoIngSW2019.model.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.model.interfaces.WeaponEffect;

import java.util.ArrayList;

public class WeaponCard extends Card {

    private String name;
    private String description;
    private ArrayList<AmmoType> ammoCost;
    private ArrayList<Boolean> paramShootList;
    private WeaponEffect weaponEffect;
    private Boolean loaded;

    WeaponCard(String name, String description, ArrayList<AmmoType> ammoCost){
        this.name  = name;
        this.description = description;
        this.ammoCost = ammoCost;
    }


    public Boolean isLoaded(){
        return this.loaded;
    }

    public void reload(){
        //TODO
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public ArrayList<AmmoType> getAmmoCost(){
        return this.ammoCost;
    }

    /*
     * paramShootList represents all the possible effects: base, advanced, optional1, optional2.
     * It is used to specify which ones are going to be used in the specific action
     */
    public void effect(ArrayList<Boolean> paramShootList){
        //TODO
    }

    public void dealDamage(int nrDamage, Player targetPlayer){
        //TODO
    }

    public void markPlayer(int nrMark, Player targetPlayer){
        //TODO
    }

    public void moveTargetPlayer(int nrSquare, Player player){
        //TODO
    }

    public void showWeaponCardInfo () {

    }

    public void setParamShoot () {

    }
}
