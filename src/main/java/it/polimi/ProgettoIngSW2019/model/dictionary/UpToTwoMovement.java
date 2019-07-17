package it.polimi.ProgettoIngSW2019.model.dictionary;

import it.polimi.ProgettoIngSW2019.model.Square;

import java.util.ArrayList;
import java.util.List;

/**
 * Squares at not more that two movements away
 * @author Luca Iovine
 */
public class UpToTwoMovement extends Distance {

    /**
     * Constructor
     * @param board
     */
    protected UpToTwoMovement(Square[][] board) {
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
     * It calculate all the square that are up to two movement of distance and that it could be seen
     * from the position passed as parameter
     * It can be called from the other distance type class in order to do more complex calculation.
     *
     * @param fromPosition position from where calculate the possible position
     * @return list of square that indicates the position visible.
     * @author: Luca Iovine
     */
    //TESTED --> UpToTwoFromP6
    protected static List<Square> calculateDistance(Square fromPosition){
        List<Square> upToTwoMovement = new ArrayList<>();
        List<Square> upToOneMovement = UpToOneMovement.calculateDistance(fromPosition);

        upToTwoMovement.add(fromPosition);
        upToTwoMovement.addAll(upToOneMovement);
        for(Square s: upToOneMovement){
            for(Square secondSquare: UpToOneMovement.calculateDistance(s)){
                if(secondSquare != null) {
                    if (!upToTwoMovement.contains(secondSquare) && !upToOneMovement.contains(secondSquare))
                        upToTwoMovement.add(secondSquare);
                }
            }
        }
        return upToTwoMovement;
    }

}
