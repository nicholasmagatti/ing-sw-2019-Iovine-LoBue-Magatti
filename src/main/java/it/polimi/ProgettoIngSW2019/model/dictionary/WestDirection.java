package it.polimi.ProgettoIngSW2019.model.dictionary;

import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.model.Square;

import java.util.ArrayList;
import java.util.List;

/**
 * Squares at west
 * @author Luca Iovine
 */
public class WestDirection extends Distance {
    /**
     * Constructor of the class
     *
     * @param board of the game
     * @author: Luca Iovine
     */
    protected WestDirection(Square[][] board) {
        super(board);
    }

    /**
     * Get all the squares at west of the specified square
     * @param fromPosition position from where calculate the possible position
     * @return list of all the squares at west of the specified square
     */
    @Override
    protected List<Square> getDistance(Square fromPosition) {
        List<Square> westDirection = new ArrayList<>();

        int[] playerAxisPos = fromPosition.getCoordinates(getBoard());
        int rowPlayer = playerAxisPos[0];
        int colPlayer = playerAxisPos[1];

        for(int i = colPlayer; i >= 0; i--){
            if(getBoard()[rowPlayer][i] != null)
                westDirection.add(getBoard()[rowPlayer][i]);
        }

        return westDirection;
    }
}
