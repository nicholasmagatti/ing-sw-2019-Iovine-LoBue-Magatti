package it.polimi.ProgettoIngSW2019.model;

import it.polimi.ProgettoIngSW2019.custom_exception.NotPartOfBoardException;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Square
 *
 * @author: Luca Iovine
 */
public abstract class Square {
    private int idRoom;
    private List<Player> playerOnSquare;
    private Square westSquare;
    private Square eastSquare;
    private Square northSquare;
    private Square southSquare;
    private Boolean isBlockedAtWest;
    private Boolean isBlockedAtEast;
    private Boolean isBlockedAtNorth;
    private Boolean isBlockedAtSouth;

    /**
     * Constructor
     *
     * @param idRoom identify the room of appartenance
     * @param isBlockedAtWest identify if there is a wall at its left
     * @param isBlockedAtEast identify if there is a wall at its right
     * @param isBlockedAtNorth identify if there is a wall at its up
     * @param isBlockedAtSouth identify if there is a wall at its down
     * @author: Luca Iovine
     */
    Square(int idRoom, Boolean isBlockedAtNorth, Boolean isBlockedAtEast, Boolean isBlockedAtSouth, Boolean isBlockedAtWest){
        this.idRoom = idRoom;
        this.isBlockedAtEast = isBlockedAtEast;
        this.isBlockedAtWest = isBlockedAtWest;
        this.isBlockedAtNorth = isBlockedAtNorth;
        this.isBlockedAtSouth = isBlockedAtSouth;
        playerOnSquare = new ArrayList<>();
    }

    /**
     * Get the id room to identify which room the square compose
     *
     * @return id room of the square
     * @author: Luca Iovine
     */
    public int getIdRoom() {
        return idRoom;
    }

    /**
     * It let you know which square is at the right of the current context of square in order to know what surronds it..
     *
     * @return the square at right
     * @author: Luca Iovine
     */
    public Square getEastSquare() {
        return eastSquare;
    }
    /**
     * It let you know which square is at the up of the current context of square in order to know what surronds it.
     *
     * @return the square at up
     * @author: Luca Iovine
     */
    public Square getNorthSquare() {
        return northSquare;
    }
    /**
     * It let you know which square is at the down of the current context of square in order to know what surronds it.
     *
     * @return the square at down
     * @author: Luca Iovine
     */
    public Square getSouthSquare() {
        return southSquare;
    }
    /**
     * It let you know which square is at the left of the current context of square in order to know what surronds it.
     *
     * @return the square at left
     * @author: Luca Iovine
     */
    public Square getWestSquare() {
        return westSquare;
    }
    /**
     * This is used whenever you have to setup the eastSquare value properly
     *
     * @return if there is a wall at right direction
     * @author: Luca Iovine
     */
    public Boolean getIsBlockedAtEast() {
        return isBlockedAtEast;
    }
    /**
     * This is used whenever you have to setup the northSquare value properly
     *
     * @return if there is a wall at up direction
     * @author: Luca Iovine
     */
    public Boolean getIsBlockedAtNorth() {
        return isBlockedAtNorth;
    }
    /**
     * This is used whenever you have to setup the westSquare value properly
     *
     * @return if there is a wall at left direction
     * @author: Luca Iovine
     */
    public Boolean getIsBlockedAtWest() {
        return isBlockedAtWest;
    }
    /**
     * This is used whenever you have to setup the southSquare value properly
     *
     * @return if there is a wall at down direction
     * @author: Luca Iovine
     */
    public Boolean getIsBlockedAtSouth() {
        return isBlockedAtSouth;
    }

    /**
     * Add player on the square. A given square could have more then one player.
     *
     * @param p player to add
     * @author: Luca Iovine
     */
    public void addPlayerOnSquare(Player p) {
        playerOnSquare.add(p);
    }
    /**
     * Remove player from square whenever he/she move or die
     *
     * @param p player to remove
     * @author: Luca Iovine
     */
    public void removePlayerOnSquare(Player p) {
        playerOnSquare.remove(p);
    }

    /**
     * @return all the player on the square
     * @author: Luca Iovine
     */
    public List<Player> getPlayerOnSquare() {
        return playerOnSquare;
    }

    /**
     * @return a list of squares that surronds it
     * @author: Luca Iovine
     */
    public List<Square> getSquaresVisibleFromHere(){
        List<Square> cardinal = new ArrayList<>();

        if(eastSquare != null){
            cardinal.add(eastSquare);
        }
        if(westSquare != null){
            cardinal.add(westSquare);
        }
        if(northSquare != null){
            cardinal.add(northSquare);
        }
        if(southSquare != null){
            cardinal.add(southSquare);
        }

        return cardinal;
    }

    /**
     * Overriden by the classes that extend this.
     * At the end of the turn every card missing on the table has to be replaced
     *
     * @param deck could be "weapon deck" or "ammo deck" based on the type of square
     * @author: Luca Iovine
     */
    public void reset(Deck deck){}

    /**
     * It is used to add player on a square after a movement on it
     *
     * @param p player to be added on the square
     * @author: Luca Iovine
     */
    public void setPlayerOnSquare(Player p){
        playerOnSquare.add(p);
    }

    /**
     * It will set what there are around the square.
     *
     * @param board the actual map that is used
     * @author: Luca Iovine
     */
    //TODO: make sure not to have problem in case of squares set as null
    public void setDependency(Square[][] board){
        if(this != null) {
            try {
                int[] axis = getCoordinates(board);
                Square east;
                Square west;
                Square north;
                Square south;
                if (axis[0] + 1 < board.length) {
                    south = board[axis[0] + 1][axis[1]];
                    if(south != null) {
                        if (!isBlockedAtSouth || south.getIdRoom() == this.idRoom) {
                            northSquare = south;
                        }
                    }
                }
                if (axis[0] - 1 >= 0) {
                    north = board[axis[0] - 1][axis[1]];
                    if(north != null) {
                        if (!isBlockedAtNorth || north.getIdRoom() == this.idRoom) {
                            southSquare = north;
                        }
                    }
                }
                if (axis[1] + 1 < board[0].length) {
                    east = board[axis[0]][axis[1] + 1];
                    if(east != null) {
                        if (!isBlockedAtEast || east.getIdRoom() == this.idRoom) {
                            eastSquare = east;
                        }
                    }
                }
                if (axis[1] - 1 >= 0) {
                    west = board[axis[0]][axis[1] - 1];
                    if(west != null) {
                        if (!isBlockedAtWest || west.getIdRoom() == this.idRoom) {
                            westSquare = west;
                        }
                    }
                }
            } catch (NotPartOfBoardException e) {
                System.out.print("Map doesn't own the square you trying to setup");
            }
        }
    }

    /**
     * @param board the actual map that is used
     * @return the coordinates (x,y) in order to know the exact position in the space of the map
     * @throws NotPartOfBoardException it will be thrown only if the square you try to get coordinates are not part of the board
     * @author: Luca Iovine
     */
    public int[] getCoordinates(Square[][] board) throws NotPartOfBoardException {
        int axis[] = new int[2];
        int x = -1;
        int y;
        axis[0] = -1;
        axis[1] = -1;

        for (Square[] s : board) {
            x++;
            y = -1;
            for (Square squareOnBoard : s) {
                y++;
                if (squareOnBoard == this) {
                    axis[0] = x;
                    axis[1] = y;
                }
            }
        }
        if(axis[0] == -1 && axis[1] == -1)
            throw new NotPartOfBoardException();

        return axis;
    }
}
