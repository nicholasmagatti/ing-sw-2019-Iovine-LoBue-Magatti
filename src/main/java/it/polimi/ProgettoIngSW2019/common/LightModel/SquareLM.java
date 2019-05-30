package it.polimi.ProgettoIngSW2019.common.LightModel;

import it.polimi.ProgettoIngSW2019.common.enums.SquareType;

import java.util.List;

/**
 * @author Nicholas Magatti
 */
public abstract class SquareLM {
    private List<Integer> players;
    private SquareType squareType;
    private boolean blockedAtNorth;
    private boolean blockedAtEast;
    private boolean blockedAtSouth;
    private boolean blockedAtWest;

    /**
     * Constructor
     * @param players - list of ids of the players on the square
     * @param squareType
     */
    public SquareLM(List<Integer> players, SquareType squareType,
                    boolean blockedAtNorth, boolean blockedAtEast, boolean blockedAtSouth, boolean blockedAtWest){
        this.players = players;
        this.squareType = squareType;
        this.blockedAtNorth = blockedAtNorth;
        this.blockedAtEast = blockedAtEast;
        this.blockedAtSouth = blockedAtSouth;
        this.blockedAtWest = blockedAtWest;
    }

    /**
     * Get the players on the square
     * @return list of players on the square
     */
    public List<Integer> getPlayers() {
        return players;
    }

    /**
     * Return the square type
     * @return the square type
     */
    public SquareType getSquareType() {
        return squareType;
    }

    /**
     * Get true if there is a wall at north, false otherwise
     * @return true if there is a wall at north, false otherwise
     */
    public boolean isBlockedAtNorth() {
        return blockedAtNorth;
    }

    /**
     * Get true if there is a wall at east, false otherwise
     * @return true if there is a wall at east, false otherwise
     */
    public boolean isBlockedAtEast() {
        return blockedAtEast;
    }

    /**
     * Get true if there is a wall at south, false otherwise
     * @return true if there is a wall at south, false otherwise
     */
    public boolean isBlockedAtSouth() {
        return blockedAtSouth;
    }

    /**
     * Get true if there is a wall at west, false otherwise
     * @return true if there is a wall at west, false otherwise
     */
    public boolean isBlockedAtWest() {
        return blockedAtWest;
    }
}
