package it.polimi.ProgettoIngSW2019.model;

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

    @Override
    public void activateEffect () {

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

    public void effect(ArrayList<Boolean> paramShootList){
        //TODO
    }

    public void activateBaseEffect() {
    }

    public void activateAdvancedEffect() {
    }

    public void activateOptional1Effect() {
    }

    public void activateOptional2Effect() {
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
