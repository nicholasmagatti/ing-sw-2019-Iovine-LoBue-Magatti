package it.polimi.ProgettoIngSW2019.common.LightModel;

import it.polimi.ProgettoIngSW2019.common.enums.SquareType;
import it.polimi.ProgettoIngSW2019.model.SpawningPoint;

import java.util.List;

/**
 * Spawn point for view
 * @author Nicholas Magatti
 */
public class SpawnPointLM extends SquareLM {

    private List<WeaponLM> weapons;

    /**
     * Constructor
     * @param players - list of ids of the players on the square
     * @param weapons - weapons available on the spawn point
     */
    public SpawnPointLM(List<Integer> players, List<WeaponLM> weapons,
                        boolean blockedAtNorth, boolean blockedAtEast, boolean blockedAtSouth, boolean blockedAtWest, int idRoom){

        super(players, SquareType.SPAWNING_POINT,
                blockedAtNorth, blockedAtEast, blockedAtSouth, blockedAtWest, idRoom);
        this.weapons = weapons;
    }

    /**
     * Get weapons on the spawn point
     * @return list of weapons on the spawn point
     */
    public List<WeaponLM> getWeapons() {
        return weapons;
    }
}
