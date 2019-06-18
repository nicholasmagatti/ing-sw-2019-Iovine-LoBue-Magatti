package it.polimi.ProgettoIngSW2019.common.Message.toController;

/**
 * MoveChoiceRequest class
 * the player decided to move in the square x,y
 */
public class MoveChoiceRequest extends PlayerChoiceRequest {
    private int x;
    private int y;


    /**
     * Constructor
     * @param hostNamePlayer    host name client
     * @param idPlayer          id player
     * @param x                 coordinate x
     * @param y                 coordinate y
     */
    public MoveChoiceRequest(String hostNamePlayer, int idPlayer, int x, int y) {
        super(hostNamePlayer, idPlayer);
        if(x < 0)
            throw new IllegalArgumentException("the position x cannot be negative");
        if(y < 0)
            throw new IllegalArgumentException("the position y cannot be negative");
        this.x = x;
        this.y = y;
    }


    /**
     * get the x coordinate
     * @return x
     */
    public int getX() {
        return x;
    }



    /**
     * get the Y coordinate
     * @return  y
     */
    public int getY() {
        return y;
    }
}
