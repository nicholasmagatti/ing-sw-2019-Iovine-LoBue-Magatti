package it.polimi.ProgettoIngSW2019.common.Message.toView;

import java.util.ArrayList;
import java.util.List;

/**
 * GrabInfo response class
 * send to view a list of coordinates
 * @author Priscilla Lo Bue
 */
public class GrabInfoResponse extends InfoResponse {
    private List<int[]> coordinatesSquareToGrab;


    /**
     * constructor
     * @param idPlayer                      idPlayer
     * @param coordinatesSquareToGrab       list of coordinates
     */
    public GrabInfoResponse(int idPlayer, List<int[]> coordinatesSquareToGrab) {
        super(idPlayer);

        if(coordinatesSquareToGrab == null)
            throw new NullPointerException("square To Grab List cannot be null");

        this.coordinatesSquareToGrab = new ArrayList<>(coordinatesSquareToGrab);
    }


    /**
     * @return  list of coordinates
     */
    public List<int[]> getCoordinatesSquareToGrab() {
        return coordinatesSquareToGrab;
    }
}
