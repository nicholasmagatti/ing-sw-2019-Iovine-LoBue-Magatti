package it.polimi.ProgettoIngSW2019.common.LightModel;

import java.util.List;

/**
 * Class EnemyPlayerLM
 * what you can see from user enemies
 * @author Priscilla Lo Bue
 */
public class EnemyPlayerLM {
    private int idPlayer;
    private String nickname;
    private List<WeaponLM> unloadedWeapons;
    private AmmoBoxLM ammoBox;



    /**
     * get the Id of the user Player
     * @return int, id Player
     */
    public int getIdPlayer() {
        return idPlayer;
    }


    /**
     * get the nickname of the userPlayer
     * @return String, the nickname
     */
    public String getNickname() {
        return nickname;
    }



    /**
     * get the user unloaded Weapons
     * @return  user unloaded weapons
     */
    public List<WeaponLM> getUnloadedWeapons() {
        return unloadedWeapons;
    }



    /**
     * get the user ammoBox
     * @return  ammobox
     */
    public AmmoBoxLM getAmmoBox() {
        return ammoBox;
    }
}
