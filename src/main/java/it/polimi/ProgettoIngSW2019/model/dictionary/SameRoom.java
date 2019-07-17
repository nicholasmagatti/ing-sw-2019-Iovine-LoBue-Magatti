package it.polimi.ProgettoIngSW2019.model.dictionary;

import it.polimi.ProgettoIngSW2019.model.Square;

import java.util.ArrayList;
import java.util.List;

/**
 * Get the squares belonging to the same room
 * @author Luca Iovine
 */
public class SameRoom extends Distance {
    protected SameRoom(Square[][] board) {
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
     * It calculate all the square that are in the same room as the position passed as parameter.
     * It can be called from the other distance type class in order to do more complex calculation.
     *
     * @param fromPosition position from where calculate the possible position
     * @return list of square that indicates the position visible.
     * @author: Luca Iovine
     */
    //TESTED --> sameRoomFromP2
    protected static List<Square> calculateDistance(Square fromPosition){
        List<Square> targetPosition = new ArrayList<>();

        for(Square[] s: getBoard()){
            for(Square square: s){
                if(square != null) {
                    if (fromPosition.getIdRoom() == square.getIdRoom()) {
                        if (!square.getPlayerOnSquare().isEmpty()) {
                            targetPosition.add(square);
                        }
                    }
                }
            }
        }

        return targetPosition;
    }
}
