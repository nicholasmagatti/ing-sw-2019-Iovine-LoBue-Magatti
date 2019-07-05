package it.polimi.ProgettoIngSW2019.model.dictionary;

import it.polimi.ProgettoIngSW2019.model.Square;

import java.util.List;

public class CanSee extends Distance {
    protected CanSee(Square[][] board) {
        super(board);
    }

    /**
     * @param fromPosition position from where calculate the possible position
     * @return list of square that indicates the position visible.
     * @author: Luca Iovine
     */
    @Override
    //NOT TO BE TESTED
    protected List<Square> getDistance(Square fromPosition) {
        return calculateDistance(fromPosition);
    }

    /**
     * It calculate all the square that could be seen from the position passed as parameter.
     * It can be called from the other distance type class in order to do more complex calculation.
     *
     * @param fromPosition position from where calculate the possible position
     * @return list of square that indicates the position visible.
     * @author: Luca Iovine
     */
    //TESTED --> canSeeFromP2
    protected static List<Square> calculateDistance(Square fromPosition){
        List<Square> sameRoomSquare = SameRoom.calculateDistance(fromPosition);
        List<Square> nearRoomSquare = NearRoom.calculateDistance(fromPosition);

        for(Square near: nearRoomSquare){
            if(near != null)
                sameRoomSquare.add(near);
        }
        return sameRoomSquare;
    }

}
