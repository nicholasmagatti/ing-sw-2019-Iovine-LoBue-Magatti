package it.polimi.ProgettoIngSW2019.model.dictionary;

import it.polimi.ProgettoIngSW2019.model.Square;

import java.util.List;

/**
 * Square that are at least two movement of distance and that it could be seen
 * @author Luca Iovine
 */
public class CanSeeAtLeastTwo extends Distance {

    /**
     * Constructor
     * @param board
     */
    protected CanSeeAtLeastTwo(Square[][] board) {
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
     * It calculate all the square that are at least two movement of distance and that it could be seen
     * from the position passed as parameter
     * It can be called from the other distance type class in order to do more complex calculation.
     *
     * @param fromPosition position from where calculate the possible position
     * @return list of square that indicates the position visible.
     * @author: Luca Iovine
     */
    //TESTED --> canSeeAtLeastTwoFromP2
    protected static List<Square> calculateDistance(Square fromPosition){
        List<Square> squareCanSee = CanSee.calculateDistance(fromPosition);
        List<Square> squareToRemove = UpToOneMovement.calculateDistance(fromPosition);

        if(squareCanSee.isEmpty()){
            return null;
        }

        squareCanSee.remove(fromPosition);
        for(Square toRemoveSquare: squareToRemove){
            if(toRemoveSquare != null) {
                if (squareCanSee.contains(toRemoveSquare)) {
                    squareCanSee.remove(toRemoveSquare);
                }
            }
        }
        return squareCanSee;
    }
}
