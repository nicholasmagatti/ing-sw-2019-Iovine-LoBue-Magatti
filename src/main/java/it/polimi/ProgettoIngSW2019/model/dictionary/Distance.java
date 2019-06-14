package it.polimi.ProgettoIngSW2019.model.dictionary;

import it.polimi.ProgettoIngSW2019.model.Square;

import java.util.List;

/**
 * Abstract class Distance
 *
 * @author: Luca Iovine
 */
public abstract class Distance {
    private static Square[][] board;

    /**
     * Constructor of the class
     *
     * @param board of the game
     * @author: Luca Iovine
     */
    protected Distance(Square[][] board){
        this.board = board;
    }

    /**
     * @return the map of the game
     * @author: Luca Iovine
     */
    protected static Square[][] getBoard() {
        return board;
    }

    /**
     * @param fromPosition position from where calculate the possible position
     * @return list of square that indicates the position visible.
     * @author: Luca Iovine
     */
    protected List<Square> getDistance(Square fromPosition){
        return null;
    }
}
