package it.polimi.ProgettoIngSW2019.common.Message.toView;

import it.polimi.ProgettoIngSW2019.common.LightModel.WeaponLM;

import java.util.ArrayList;
import java.util.List;

/**
 * GrabWeaponSwap
 * list of player's weapons
 * the player had to choose a weapon to swap with a weapon from a spawning point
 * @author Priscilla Lo Bue
 */
public class GrabWeaponSwap extends PlayerChoiceResponse {
    private List<WeaponLM> weapons;


    /**
     * Constructor
     * @param idPlayer      idPlayer
     * @param weapons       list of player's weapons
     */
    public GrabWeaponSwap(int idPlayer, List<WeaponLM> weapons) {
        super(idPlayer);
        this.weapons = new ArrayList<>(weapons);
    }


    /**
     * get list of player's weapons
     * @return      list of player's weapons
     */
    public List<WeaponLM> getWeapons() {
        return weapons;
    }
}
