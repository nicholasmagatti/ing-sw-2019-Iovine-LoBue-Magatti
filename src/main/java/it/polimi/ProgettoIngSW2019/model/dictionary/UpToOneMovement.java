package it.polimi.ProgettoIngSW2019.model.dictionary;

import it.polimi.ProgettoIngSW2019.model.Square;

import java.util.ArrayList;
import java.util.List;

public class UpToOneMovement extends Distance {

    protected UpToOneMovement(Square[][] board) {
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
     * It calculate all the square that are up to one movement of distance and that it could be seen
     * from the position passed as parameter
     * It can be called from the other distance type class in order to do more complex calculation.
     *
     * @param fromPosition position from where calculate the possible position
     * @return list of square that indicates the position visible.
     * @author: Luca Iovine
     */
    //TESTED --> UpToOneFromP2
    protected static List<Square> calculateDistance(Square fromPosition){
        List<Square> upToOneMovement = new ArrayList<>();
        upToOneMovement.add(fromPosition);
        upToOneMovement.addAll(fromPosition.getSquaresVisibleFromHere());
        return upToOneMovement;
    }
}
