package it.polimi.ProgettoIngSW2019.common.Message;

import it.polimi.ProgettoIngSW2019.common.LightModel.WeaponLM;

import java.util.List;

/**
 * Reload info class
 * @author Priscilla Lo Bue
 */
public class ReloadInfoResponse extends Info {
    private List<WeaponLM> weaponsCanReload;


    /**
     * Constructor from controller to view
     * @param idPlayer              id player that wants to reload a weapon
     * @param weaponsCanReload      list of weapon the player can reload
     */
    public ReloadInfoResponse(int idPlayer, List<WeaponLM> weaponsCanReload) {
        super(idPlayer);
        this.weaponsCanReload = weaponsCanReload;
    }


    /**
     * get list of weapons the player can reload
     * @return  list of weapons
     */
    public List<WeaponLM> getWeaponsCanReload() {
        return weaponsCanReload;
    }
}
