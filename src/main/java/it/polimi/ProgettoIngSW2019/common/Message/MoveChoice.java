package it.polimi.ProgettoIngSW2019.common.Message;

/**
 * MoveChoice class
 * the player decided to move in the square x,y
 */
public class MoveChoice extends PlayerChoice {
    private int x;
    private int y;



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
