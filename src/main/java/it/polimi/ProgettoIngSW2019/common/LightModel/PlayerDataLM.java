package it.polimi.ProgettoIngSW2019.common.LightModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Class PlayerDataLM
 * User information
 * @author Priscilla Lo Bue
 */
public class PlayerDataLM {
    private int idPlayer;
    private String nickname;
    private List<WeaponLM> unloadedWeapons;
    private int nRedAmmo;
    private int nBlueAmmo;
    private int nYellowAmmo;
    private int nLoadedWeapons;
    private int nPowerUps;
    private int nSkulls;
    private boolean active;
    private boolean playerDown;
    private List<String> damageLine;
    private List<String> markLine;


    /**
     * Constructor
     * @param idPlayer              id player
     * @param nickname              name player
     * @param unloadedWeapons       list of unloaded weapons
     * @param nRedAmmo              number red ammo
     * @param nBlueAmmo             number blue ammo
     * @param nYellowAmmo           number yellow ammo
     * @param nSkulls               number of skulls
     * @param active                player is/not active
     * @param playerDown            player is/not down
     * @param damageLine            list of player name who hits owner player
     * @param markLine              list of player name who marks owner player
     */
    public PlayerDataLM(int idPlayer, String nickname, List<WeaponLM> unloadedWeapons, int nRedAmmo, int nBlueAmmo, int nYellowAmmo, int nSkulls, boolean active, boolean playerDown, List<String> damageLine, List<String> markLine, int nLoadedWeapons, int nPowerUps) {
        if(idPlayer < 0)
            throw new IllegalArgumentException("IdPlayer cannot be negative");
        this.idPlayer = idPlayer;

        if(nickname == null)
            throw new NullPointerException("Nickname of the player cannot be null");
        this.nickname = nickname;

        this.unloadedWeapons = new ArrayList<>(unloadedWeapons);
        this.nRedAmmo = nRedAmmo;
        this.nBlueAmmo = nBlueAmmo;
        this.nYellowAmmo = nYellowAmmo;
        this.nSkulls = nSkulls;
        this.active = active;
        this.playerDown = playerDown;
        this.damageLine = new ArrayList<>(damageLine);
        this.markLine = new ArrayList<>(markLine);
        this.nLoadedWeapons = nLoadedWeapons;
        this.nPowerUps = nPowerUps;
    }



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
     * get number of red ammo
     * @return  number of red ammo
     */
    public int getnRedAmmo() {
        return nRedAmmo;
    }


    /**
     * get number of blue ammo
     * @return  number of blue ammo
     */
    public int getnBlueAmmo() {
        return nBlueAmmo;
    }


    /**
     * get number of yellow ammo
     * @return  number of yellow ammo
     */
    public int getnYellowAmmo() {
        return nYellowAmmo;
    }


    /**
     * get number of skulls
     * @return number of skulls
     */
    public int getnSkulls() {
        return nSkulls;
    }


    /**
     * get the list of players who hits
     * @return  list of name players
     */
    public List<String> getDamageLine() {
        return damageLine;
    }


    /**
     * get the list of players who marks
     * @return  list of name players
     */
    public List<String> getMarkLine() {
        return markLine;
    }


    /**
     * get if the player is active or not
     * @return      is active or not
     */
    public boolean getActive () {
        return active;
    }


    /**
     * get if the player is dead or not
     * @return  is dead or not
     */
    public boolean getDown() {
        return playerDown;
    }


    /**
     * get n loaded weapons cards
     * @return      n loaded weapons cards
     */
    public int getnMyLoadedWeapons() {
        return nLoadedWeapons;
    }


    /**
     * get n powerUps cards
     * @return      n powerUps cards
     */
    public int getnPowerUps() {
        return nPowerUps;
    }
}
