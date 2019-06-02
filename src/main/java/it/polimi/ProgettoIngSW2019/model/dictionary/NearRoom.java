package it.polimi.ProgettoIngSW2019.model.dictionary;

import it.polimi.ProgettoIngSW2019.model.Square;

import java.util.ArrayList;
import java.util.List;

public class NearRoom extends Distance {
    protected NearRoom(Square[][] board) {
        super(board);
    }

    @Override
    protected List<Square> getDistance(Square fromPosition) {
        return calculateDistance(fromPosition);
    }

    protected static List<Square> calculateDistance(Square fromPosition){
        int idRoom;
        List<Integer> listIdRoom = new ArrayList<>();
        List<Square> targetPosition = new ArrayList<>();

        for(Square cardinalSquare: fromPosition.getSquaresVisibleFromHere()){
            if(!listIdRoom.contains(cardinalSquare.getIdRoom()) &&
                    cardinalSquare.getIdRoom() != fromPosition.getIdRoom()){
                listIdRoom.add(cardinalSquare.getIdRoom());
            }
        }

        for(Square[] s: getBoard()){
            for(Square square: s){
                idRoom = square.getIdRoom();
                if(listIdRoom.contains(idRoom)){
                    if(!square.getPlayerOnSquare().isEmpty()){
                        targetPosition.add(square);
                    }
                }
            }
        }

        return targetPosition;
    }
}
