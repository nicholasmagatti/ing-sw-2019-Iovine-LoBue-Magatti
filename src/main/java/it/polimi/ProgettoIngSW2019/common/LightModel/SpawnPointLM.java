package it.polimi.ProgettoIngSW2019.common.LightModel;

import it.polimi.ProgettoIngSW2019.common.enums.SquareType;
import it.polimi.ProgettoIngSW2019.model.SpawningPoint;

import java.util.List;

/**
 * @author Nicholas Magatti
 */
public class SpawnPointLM extends SquareLM {

    private List<WeaponLM> weapons;

    /**
     * Constructor
     * @param players - players on the square
     * @param weapons - weapons available on the spawn point
     */
    public SpawnPointLM(List<PlayerDataLM> players, List<WeaponLM> weapons){

        super(players, SquareType.SPAWNING_POINT);
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
