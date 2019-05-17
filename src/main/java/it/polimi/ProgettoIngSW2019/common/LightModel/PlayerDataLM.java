package it.polimi.ProgettoIngSW2019.common.LightModel;

import java.util.List;

/**
 * Class PlayerDataLM
 * User information
 * @author Priscilla Lo Bue
 */
public class PlayerDataLM {
    private int idPlayer;
    private String nickname;
    private List<WeaponLM> loadedWeapons;
    private List<WeaponLM> unloadedWeapons;
    private List<EnemyPlayerLM> enemyPlayers;
    private List<PowerUpLM> powerUps;
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
     * get the user loaded Weapons
     * @return List<Weapons> loaded weapons
     */
    public List<WeaponLM> getLoadedWeapons() {
        return loadedWeapons;
    }


    /**
     * get the user unloaded Weapons
     * @return  user unloaded weapons
     */
    public List<WeaponLM> getUnloadedWeapons() {
        return unloadedWeapons;
    }


    /**
     * get the user powerUps
     * @return  list of powerUps
     */
    public List<PowerUpLM> getPowerUps() {
        return powerUps;
    }


    /**
     * get the user ammoBox
     * @return  ammobox
     */
    public AmmoBoxLM getAmmoBox() {
        return ammoBox;
    }


    /**
     * get a list of enemy user Players
     * @return  enemy Players
     */
    public List<EnemyPlayerLM> getEnemyPlayer() {
        return enemyPlayers;
    }

}
