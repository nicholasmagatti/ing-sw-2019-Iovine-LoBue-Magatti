package it.polimi.ProgettoIngSW2019.common.LightModel;

import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;

/**
 * @author Nicholas Magatti
 */
public class MapLM {
    //[][] stands for [row][column]
    private SquareLM[][] map;

    /**
     * Constructor
     * @param map
     */
    public MapLM(SquareLM[][] map){
        this.map = map;
    }

    /**
     * Get the map for the view
     * @return map to be visualized by the view
     */
    public SquareLM[][] getMap() {
        return map;
    }
}
