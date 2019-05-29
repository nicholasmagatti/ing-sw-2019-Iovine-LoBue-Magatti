package it.polimi.ProgettoIngSW2019.common.LightModel;

import it.polimi.ProgettoIngSW2019.common.enums.SquareType;

import java.util.List;

/**
 * @author Nicholas Magatti
 */
public abstract class SquareLM {
    private List<PlayerDataLM> players;
    private SquareType squareType;

    /**
     * Constructor
     * @param players - players on the square
     * @param squareType
     */
    public SquareLM(List<PlayerDataLM> players, SquareType squareType){
        this.players = players;
        this.squareType = squareType;
    }

    /**
     * Get the players on the square
     * @return list of players on the square
     */
    public List<PlayerDataLM> getPlayers() {
        return players;
    }

    /**
     * Return the square type
     * @return the square type
     */
    public SquareType getSquareType() {
        return squareType;
    }
}
