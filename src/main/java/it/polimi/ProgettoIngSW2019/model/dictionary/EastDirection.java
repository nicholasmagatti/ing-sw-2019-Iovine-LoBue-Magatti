package it.polimi.ProgettoIngSW2019.model.dictionary;

import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.model.Square;

import java.util.ArrayList;
import java.util.List;

/**
 * Squares at east
 * @author Luca Iovine
 */
public class EastDirection extends Distance {
    /**
     * Constructor of the class
     *
     * @param board of the game
     * @author: Luca Iovine
     */
    protected EastDirection(Square[][] board) {
        super(board);
    }

    /**
     * Get all the squares at east of the specified square
     * @param fromPosition position from where calculate the possible position
     * @return list of all the squares at east of the specified square
     */
    @Override
    protected List<Square> getDistance(Square fromPosition) {
        List<Square> eastDirection = new ArrayList<>();

        int[] playerAxisPos = fromPosition.getCoordinates(getBoard());
        int rowPlayer = playerAxisPos[0];
        int colPlayer = playerAxisPos[1];

        for(int i = colPlayer; i <= GeneralInfo.COLUMNS_MAP - 1; i++){
            if(getBoard()[rowPlayer][i] != null)
                eastDirection.add(getBoard()[rowPlayer][i]);
        }

        return eastDirection;
    }
}
