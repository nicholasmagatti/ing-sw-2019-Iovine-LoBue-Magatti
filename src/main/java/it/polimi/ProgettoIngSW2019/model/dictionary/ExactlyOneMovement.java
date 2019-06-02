package it.polimi.ProgettoIngSW2019.model.dictionary;

import it.polimi.ProgettoIngSW2019.model.Square;

import java.util.List;

public class ExactlyOneMovement extends Distance {

    protected ExactlyOneMovement(Square[][] board) {
        super(board);
    }

    @Override
    protected List<Square> getDistance(Square fromPosition) {
        return calculateDistance(fromPosition);
    }

    protected static List<Square> calculateDistance(Square fromPosition){
       return fromPosition.getSquaresVisibleFromHere();
    }
}
