package it.polimi.ProgettoIngSW2019.common.LightModel;


/**
 * Class WeaponLM: little model with only the information visible for the user
 * @author Priscilla Lo Bue
 */
public class WeaponLM {
    private int idWeapon;
    private String name;
    private String description;
    private int[] ammoCostReload;
    private int[] ammoCostBuy;


    /**
     * constructor with enemyVisible
     * @param idWeapon      id weapon card
     * @param name          name of the weapon
     * @param description   description of the weapon
     */
    public WeaponLM(int idWeapon, String name, String description, int[] ammoCostReload, int[] ammoCostBuy) {
        if(idWeapon < 0)
            throw new IllegalArgumentException("The id cannot be negative");
        this.idWeapon = idWeapon;

        if(name == null)
            throw new NullPointerException("Name of the weapon cannot be null");
        this.name = name;

        if(description == null)
            throw new NullPointerException("Description of the weapon cannot be null");
        this.description = description;

        this.ammoCostReload = ammoCostReload;
        this.ammoCostBuy = ammoCostBuy;
    }



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
     *
     * @return  cost in ammo of the reload
     */
    public int[] getAmmoCostReload() {
        return ammoCostReload;
    }


    /**
     *
     * @return  cost in ammo of the weapon
     */
    public int[] getAmmoCostBuy() {
        return ammoCostBuy;
    }
}
