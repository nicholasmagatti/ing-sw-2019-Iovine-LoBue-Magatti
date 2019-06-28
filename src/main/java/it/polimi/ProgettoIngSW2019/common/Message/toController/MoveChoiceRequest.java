package it.polimi.ProgettoIngSW2019.common.Message.toController;

/**
 * MoveChoiceRequest class
 * the player decided to move in the square x,y
 * @author Priscilla Lo Bue
 */
public class MoveChoiceRequest extends PlayerChoiceRequest {
    private int[] coordinates;


    /**
     * Constructor
     * @param hostNamePlayer    host name client
     * @param idPlayer          id player
     */
    public MoveChoiceRequest(String hostNamePlayer, int idPlayer, int[] coordinates) {
        super(hostNamePlayer, idPlayer);

        if((coordinates[0] < 0) || (coordinates[1] < 0))
            throw new IllegalArgumentException("the position x or y cannot be negative");

        this.coordinates = coordinates;
    }


    /**
     * @return  the coordinates of the square to grab
     */
    public int[] getCoordinates() {
        return coordinates;
    }
}
