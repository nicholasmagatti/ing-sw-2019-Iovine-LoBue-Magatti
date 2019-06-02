package it.polimi.ProgettoIngSW2019.model.dictionary;

import it.polimi.ProgettoIngSW2019.model.Square;

import java.util.ArrayList;
import java.util.List;

public class CannotSee extends Distance {

    protected CannotSee(Square[][] board) {
        super(board);
    }

    @Override
    protected List<Square> getDistance(Square fromPosition) {
        return calculateDistance(fromPosition);
    }

    protected static List<Square> calculateDistance(Square fromPosition){
        List<Square> targetPosition = new ArrayList<>();
        List<Square> toRemoveSquare = CanSee.calculateDistance(fromPosition);

        for(Square[] s: getBoard()){
            for(Square square: s){
                if(!toRemoveSquare.contains(square)){
                    targetPosition.add(square);
                }
            }
        }
        return targetPosition;
    }
}
