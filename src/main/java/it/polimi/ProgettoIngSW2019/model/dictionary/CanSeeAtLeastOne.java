package it.polimi.ProgettoIngSW2019.model.dictionary;

import it.polimi.ProgettoIngSW2019.model.Square;

import java.util.List;

public class CanSeeAtLeastOne extends Distance {

    protected CanSeeAtLeastOne(Square[][] board) {
        super(board);
    }

    @Override
    protected List<Square> getDistance(Square fromPosition) {
        return calculateDistance(fromPosition);
    }

    protected static List<Square> calculateDistance(Square fromPosition){
        List<Square> squareCanSee = CanSee.calculateDistance(fromPosition);
        List<Square> squareToRemove = SameSquare.calculateDistance(fromPosition);

        if(squareCanSee.isEmpty()){
            return null;
        }

        for(Square toRemoveSquare: squareToRemove){
            if(squareCanSee.contains(toRemoveSquare)){
                squareCanSee.remove(toRemoveSquare);
            }
        }
        return squareCanSee;
    }
}
