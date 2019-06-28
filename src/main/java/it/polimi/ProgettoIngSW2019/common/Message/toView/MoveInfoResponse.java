package it.polimi.ProgettoIngSW2019.common.Message.toView;

import it.polimi.ProgettoIngSW2019.model.Square;
import java.util.List;

/**
 * Move info class
 * message with the request and response move info
 * @author Priscilla Lo Bue
 */
public class MoveInfoResponse extends InfoResponse {
    private List<int[]> coordinates;


    /**
     * Constructor
     * @param idPlayer              id Player
     * @param coordinates           coordinates you can move
     */
    public MoveInfoResponse(int idPlayer, List<int[]> coordinates) {
        super(idPlayer);
        this.coordinates = coordinates;
    }


    /**
     * @return  list of coordinates
     */
    public List<int[]> getCoordinates() {
        return coordinates;
    }
}
