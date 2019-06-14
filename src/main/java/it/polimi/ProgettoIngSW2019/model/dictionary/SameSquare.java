package it.polimi.ProgettoIngSW2019.model.dictionary;

import it.polimi.ProgettoIngSW2019.model.Square;

import java.util.ArrayList;
import java.util.List;

public class SameSquare extends Distance{

    protected SameSquare(Square[][] board) {
        super(board);
    }

    /**
     * @param fromPosition position from where calculate the possible position
     * @return list of square that indicates the position visible.
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    @Override
    protected List<Square> getDistance(Square fromPosition) {
        return calculateDistance(fromPosition);
    }

    /**
     * It return the samme square of the position passed as parameter
     *
     * @param fromPosition position from where calculate the possible position
     * @return list of square that indicates the position visible.
     * @author: Luca Iovine
     */
    //TESTED --> sameSquareP2
    protected static List<Square> calculateDistance(Square fromPosition){
        List<Square> targetPosition = new ArrayList<>();
        targetPosition.add(fromPosition);

        return targetPosition;
    }
}
