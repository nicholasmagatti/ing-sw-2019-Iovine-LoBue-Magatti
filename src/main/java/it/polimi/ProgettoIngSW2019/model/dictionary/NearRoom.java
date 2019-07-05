package it.polimi.ProgettoIngSW2019.model.dictionary;

import it.polimi.ProgettoIngSW2019.model.Square;

import java.util.ArrayList;
import java.util.List;

public class NearRoom extends Distance {
    protected NearRoom(Square[][] board) {
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
     * It calculate all the square that are in rooms near the position passed as parameter.
     * Not the same of the position passed.
     * It can be called from the other distance type class in order to do more complex calculation.
     *
     * @param fromPosition position from where calculate the possible position
     * @return list of square that indicates the position visible.
     * @author: Luca Iovine
     */
    //TESTED --> nearRoomFromP2
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
                if(square != null) {
                    idRoom = square.getIdRoom();
                    if (listIdRoom.contains(idRoom)) {
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
