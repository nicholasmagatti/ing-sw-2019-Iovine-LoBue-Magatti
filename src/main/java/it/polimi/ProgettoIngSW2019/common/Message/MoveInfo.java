package it.polimi.ProgettoIngSW2019.common.Message;

import java.util.List;

/**
 * Move info class
 * message with the request and response move info
 */
public class MoveInfo extends Info {
    private List<Integer[]> availablePosition;


    /**
     * @return  list of position
     */
    public List<Integer[]> getAvailablePosition() {
        return availablePosition;
    }
}
