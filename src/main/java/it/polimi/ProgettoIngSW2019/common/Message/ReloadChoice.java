package it.polimi.ProgettoIngSW2019.common.Message;

/**
 * @author Priscilla Lo Bue
 */
public class ReloadChoice extends PlayerChoice {
    private int idWeaponToReload;


    /**
     * Constructor
     * @param idPlayer          id Player
     * @param idWeaponToReload  id Weapon to reload
     */
    public ReloadChoice(int idPlayer, int idWeaponToReload) {
        super(idPlayer);

        if(idWeaponToReload < 0)
            throw new IllegalArgumentException("IdWeapon cannot be negative");
        this.idWeaponToReload = idWeaponToReload;
    }


    /**
     * get the id of the weapon to reload
     * @return id weapon
     */
    public int getIdWeaponToReload() {
        return idWeaponToReload;
    }
}
