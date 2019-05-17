package it.polimi.ProgettoIngSW2019.common.LightModel;

import java.util.List;


/**
 * Class WeaponLM: little model with only the information visible for the user
 * @author Priscilla Lo Bue
 */
public class WeaponLM {
    private int idWeapon;
    private String name;
    private String description;
    private List<String> enemyVisible;


    /**
     * Get the id of the weapon to visualize
      * @return     int, the id
     */
    public int getIdWeapon() {
        return this.idWeapon;
    }


    /**
     * Get the name of the weapon
     * @return  String, the name
     */
    public String getName() {
        return this.name;
    }


    /**
     * Get the description of the Weapon effect
     * @return  string, the description
     */
    public String getDescription() {
        return this.description;
    }



    /**
     * Get the enemy visible with this weapon
     * @return  List<String>, enemy names
     */
    public List<String> getEnemyVisible() {
        return this.enemyVisible;
    }
}
