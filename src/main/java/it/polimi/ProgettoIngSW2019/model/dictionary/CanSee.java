package it.polimi.ProgettoIngSW2019.model.dictionary;

import it.polimi.ProgettoIngSW2019.model.Square;

import java.util.List;

public class CanSee extends Distance {
    protected CanSee(Square[][] board) {
        super(board);
    }

    @Override
    protected List<Square> getDistance(Square fromPosition) {
        return calculateDistance(fromPosition);
    }

    protected static List<Square> calculateDistance(Square fromPosition){
        List<Square> sameRoomSquare = SameRoom.calculateDistance(fromPosition);
        List<Square> nearRoomSquare = NearRoom.calculateDistance(fromPosition);

        for(Square near: nearRoomSquare){
            sameRoomSquare.add(near);
        }
        return sameRoomSquare;
    }

}
