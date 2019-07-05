package it.polimi.ProgettoIngSW2019.model.dictionary;

import it.polimi.ProgettoIngSW2019.model.Square;

import java.util.ArrayList;
import java.util.List;

public class CannotSee extends Distance {

    protected CannotSee(Square[][] board) {
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
     * It calculate all the square that cannot be seen from the position passed as parameter
     * It can be called from the other distance type class in order to do more complex calculation.
     *
     * @param fromPosition position from where calculate the possible position
     * @return list of square that indicates the position visible.
     * @author: Luca Iovine
     */
    //TESTED --> cannotFromP2
    protected static List<Square> calculateDistance(Square fromPosition){
        List<Square> targetPosition = new ArrayList<>();
        List<Square> toRemoveSquare = CanSee.calculateDistance(fromPosition);

        for(Square[] s: getBoard()){
            for(Square square: s){
                if(square != null) {
                    if (!toRemoveSquare.contains(square)) {
                        targetPosition.add(square);
                    }
                }
            }
        }
        return targetPosition;
    }
}
