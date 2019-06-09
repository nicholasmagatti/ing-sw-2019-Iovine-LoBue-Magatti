package it.polimi.ProgettoIngSW2019.model.dictionary;

import it.polimi.ProgettoIngSW2019.model.Square;

import java.util.ArrayList;
import java.util.List;

public class ExactlyTwoMovement extends Distance {

    protected ExactlyTwoMovement(Square[][] board) {
        super(board);
    }

    @Override
    protected List<Square> getDistance(Square fromPosition) {
        return calculateDistance(fromPosition);
    }

    protected static List<Square> calculateDistance(Square fromPosition){
        List<Square> twoMovementAway = new ArrayList<>();
        List<Square> oneMovementAway = ExactlyOneMovement.calculateDistance(fromPosition);

        twoMovementAway.addAll(oneMovementAway);
        for(Square s: oneMovementAway){
            for(Square secondSquare: ExactlyOneMovement.calculateDistance(s)){
                if(!twoMovementAway.contains(secondSquare))
                    twoMovementAway.add(secondSquare);
            }
        }
        return twoMovementAway;
    }

}
