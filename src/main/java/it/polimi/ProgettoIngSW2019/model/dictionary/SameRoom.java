package it.polimi.ProgettoIngSW2019.model.dictionary;

import it.polimi.ProgettoIngSW2019.model.Square;

import java.util.ArrayList;
import java.util.List;

public class SameRoom extends Distance {
    protected SameRoom(Square[][] board) {
        super(board);
    }

    @Override
    protected List<Square> getDistance(Square fromPosition) {
        return calculateDistance(fromPosition);
    }

    protected static List<Square> calculateDistance(Square fromPosition){
        List<Square> targetPosition = new ArrayList<>();

        for(Square[] s: getBoard()){
            for(Square square: s){
                if(fromPosition.getIdRoom() == square.getIdRoom()){
                    if(!square.getPlayerOnSquare().isEmpty()){
                        targetPosition.add(square);
                    }
                }
            }
        }

        return targetPosition;
    }
}
