package it.polimi.ProgettoIngSW2019.common.Message.toView;

import it.polimi.ProgettoIngSW2019.common.Message.toView.InfoResponse;
import it.polimi.ProgettoIngSW2019.model.Square;

import java.util.ArrayList;
import java.util.List;

/**
 * Move info class
 * message with the request and response move info
 */
public class MoveInfoResponse extends InfoResponse {
    private List<Square> availablePosition = new ArrayList<>();


    /**
     * Constructor
     * @param idPlayer              id Player
     * @param availablePosition     position you can move
     */
    public MoveInfoResponse(int idPlayer, List<Square> availablePosition) {
        super(idPlayer);
        this.availablePosition = availablePosition;
    }


    /**
     * @return  list of position
     */
    public List<Square> getAvailablePosition() {
        return availablePosition;
    }
}
