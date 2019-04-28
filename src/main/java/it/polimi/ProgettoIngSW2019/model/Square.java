package it.polimi.ProgettoIngSW2019.model;

import it.polimi.ProgettoIngSW2019.model.enums.RoomColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Class Square
 *
 * @Author: Luca Iovine
 */
public abstract class Square {
    private int idRoom;
    private List<Player> playerOnSquare;
    private RoomColor roomColor;
    private Square weastSquare;
    private Square eastSquare;
    private Square northSquare;
    private Square southSquare;
    private Boolean isBlockedAtWeast;
    private Boolean isBlockedAtEast;
    private Boolean isBlockedAtNorth;
    private Boolean isBlockedAtSouth;

    /**
     * Constructor
     *
     * @param roomColor
     * @param idRoom
     * @param isBlockedAtWeast
     * @param isBlockedAtEast
     * @param isBlockedAtNorth
     * @param isBlockedAtSouth
     * @Author: Luca Iovine
     */
    Square(RoomColor roomColor, int idRoom, Boolean isBlockedAtWeast, Boolean isBlockedAtEast, Boolean isBlockedAtNorth, Boolean isBlockedAtSouth){
        this.roomColor = roomColor;;
        this.idRoom = idRoom;
        this.isBlockedAtEast = isBlockedAtEast;
        this.isBlockedAtWeast = isBlockedAtWeast;
        this.isBlockedAtNorth = isBlockedAtNorth;
        this.isBlockedAtSouth = isBlockedAtSouth;
    }

    /**
     * Get the room's color
     *
     * @return room color value
     * @Author: Luca Iovine
     */
    public RoomColor getRoomColor(){
        return this.roomColor;
    }

    /**
     * Get the id room to identify which room the square compose
     *
     * @return id room of the square
     * @Author: Luca Iovine
     */
    public int getIdRoom() {
        return idRoom;
    }

    /**
     * It let you know which square is at the right of the current context of square in order to know what surronds it..
     *
     * @return the square at right
     * @Author: Luca Iovine
     */
    public Square getEastSquare() {
        return eastSquare;
    }
    /**
     * It let you know which square is at the up of the current context of square in order to know what surronds it.
     *
     * @return the square at up
     * @Author: Luca Iovine
     */
    public Square getNorthSquare() {
        return northSquare;
    }
    /**
     * It let you know which square is at the down of the current context of square in order to know what surronds it.
     *
     * @return the square at down
     * @Author: Luca Iovine
     */
    public Square getSouthSquare() {
        return southSquare;
    }
    /**
     * It let you know which square is at the left of the current context of square in order to know what surronds it.
     *
     * @return the square at left
     * @Author: Luca Iovine
     */
    public Square getWestSquare() {
        return weastSquare;
    }
    /**
     * This is used whenever you have to setup the eastSquare value properly
     *
     * @return if there is a wall at right direction
     * @Author: Luca Iovine
     */
    public Boolean getIsBlockedAtEast() {
        return isBlockedAtEast;
    }
    /**
     * This is used whenever you have to setup the northSquare value properly
     *
     * @return if there is a wall at up direction
     * @Author: Luca Iovine
     */
    public Boolean getIsBlockedAtNorth() {
        return isBlockedAtNorth;
    }
    /**
     * This is used whenever you have to setup the westSquare value properly
     *
     * @return if there is a wall at left direction
     * @Author: Luca Iovine
     */
    public Boolean getIsBlockedAtWeast() {
        return isBlockedAtWeast;
    }
    /**
     * This is used whenever you have to setup the southSquare value properly
     *
     * @return if there is a wall at down direction
     * @Author: Luca Iovine
     */
    public Boolean getIsBlockedAtSouth() {
        return isBlockedAtSouth;
    }

    /**
     * Add player on the square. A given square could have more then one player.
     *
     * @param p player to add
     * @Author: Luca Iovine
     */
    public void addPlayerOnSquare(Player p) {
        playerOnSquare.add(p);
    }
    /**
     * Remove player from square whenever he/she move or die
     *
     * @param p player to remove
     * @Author: Luca Iovine
     */
    public void removePlayerOnSquare(Player p) {
        playerOnSquare.remove(p);
    }

    /**
     * @return all the player on the square
     * @Author: Luca Iovine
     */
    public List<Player> getPlayerOnSquare() {
        return playerOnSquare;
    }

    /**
     * @return a list of squares that surronds it
     * @Author: Luca Iovine
     */
    public List<Square> getSquaresVisibleFromHere(){
        List<Square> cardinal = new ArrayList<>();

        if(eastSquare != null){
            cardinal.add(eastSquare);
        }
        if(weastSquare != null){
            cardinal.add(weastSquare);
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
     * @Author: Luca Iovine
     */
    public void reset(Deck deck){}
}
