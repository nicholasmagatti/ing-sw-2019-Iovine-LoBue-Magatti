package it.polimi.ProgettoIngSW2019.model.dictionary;

import it.polimi.ProgettoIngSW2019.model.Square;

import java.util.ArrayList;
import java.util.List;

/**
 * Squares at north
 * @author Luca Iovine
 */
public class NorthDirection extends Distance {
    /**
     * Constructor of the class
     *
     * @param board of the game
     * @author: Luca Iovine
     */
    protected NorthDirection(Square[][] board) {
        super(board);
    }

    /**
     * Get all the squares at north of the specified square
     * @param fromPosition position from where calculate the possible position
     * @return list of all the squares at north of the specified square
     */
    @Override
    protected List<Square> getDistance(Square fromPosition) {
        List<Square> northDirection = new ArrayList<>();

        int[] playerAxisPos = fromPosition.getCoordinates(getBoard());
        int rowPlayer = playerAxisPos[0];
        int colPlayer = playerAxisPos[1];

        for(int i = rowPlayer; i >= 0; i--){
            if(getBoard()[i][colPlayer] != null)
                northDirection.add(getBoard()[i][colPlayer]);
        }

        return northDirection;
    }
}
