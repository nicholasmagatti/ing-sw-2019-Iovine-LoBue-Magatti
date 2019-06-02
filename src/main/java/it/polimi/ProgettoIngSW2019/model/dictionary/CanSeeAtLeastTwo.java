package it.polimi.ProgettoIngSW2019.model.dictionary;

import it.polimi.ProgettoIngSW2019.model.Square;

import java.util.List;

public class CanSeeAtLeastTwo extends Distance {

    protected CanSeeAtLeastTwo(Square[][] board) {
        super(board);
    }

    @Override
    protected List<Square> getDistance(Square fromPosition) {
        return calculateDistance(fromPosition);
    }

    protected static List<Square> calculateDistance(Square fromPosition){
        List<Square> squareCanSee = CanSee.calculateDistance(fromPosition);
        List<Square> squareToRemove = ExactlyOneMovement.calculateDistance(fromPosition);

        if(squareCanSee.isEmpty()){
            return null;
        }

        squareCanSee.remove(fromPosition);
        for(Square toRemoveSquare: squareToRemove){
            if(squareCanSee.contains(toRemoveSquare)){
                squareCanSee.remove(toRemoveSquare);
            }
        }
        return squareCanSee;
    }
}
