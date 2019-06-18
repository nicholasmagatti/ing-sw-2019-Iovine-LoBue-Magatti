package it.polimi.ProgettoIngSW2019.common.Message.toController;


/**
 * Grab Choice request from a square
 * @author Priscilla Lo Bue
 */
public class GrabChoiceRequest extends PlayerChoiceRequest {
    private int[] coordinates;


    /**
     * Constructor
     * @param idPlayer      idPlayer
     * @param coordinates   array int of 2 coordinates of the square
     */
    public GrabChoiceRequest(int idPlayer, int[] coordinates) {
        super(idPlayer);
        this.coordinates = coordinates;
    }


    /**
     * @return      array int of 2 coordinates of the square
     */
    public int[] getCoordinates() {
        return coordinates;
    }
}
