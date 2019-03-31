package it.polimi.ProgettoIngSW2019.model;

import java.util.ArrayList;

public abstract class WeaponCard extends Card{
    private String name;
    private String description;
    private ArrayList<AmmoType> ammoCost;
    private ArrayList<Boolean> paramShootList;
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

    public void paramShootList(){
        //TODO
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

    public void dealDamage(Player player, int nrDamage){
        //TODO
    }

    public void markPlayer(Player player, int nrDamage){
        //TODO
    }

    public void moveTargetPlayer(Player player, int nrSquare){
        //TODO
    }
}
