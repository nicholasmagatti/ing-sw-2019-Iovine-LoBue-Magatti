package it.polimi.ProgettoIngSW2019.model.dictionary;

import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.model.Square;

import java.util.ArrayList;
import java.util.List;

public class SouthDirection extends Distance {
    /**
     * Constructor of the class
     *
     * @param board of the game
     * @author: Luca Iovine
     */
    protected SouthDirection(Square[][] board) {
        super(board);
    }

    @Override
    protected List<Square> getDistance(Square fromPosition) {
        List<Square> southDirection = new ArrayList<>();

        int[] playerAxisPos = fromPosition.getCoordinates(getBoard());
        int rowPlayer = playerAxisPos[0];
        int colPlayer = playerAxisPos[1];

        for(int i = rowPlayer; i <= GeneralInfo.ROWS_MAP - 1; i++){
            if(getBoard()[i][colPlayer] != null)
                southDirection.add(getBoard()[i][colPlayer]);
        }

        return southDirection;
    }

}
