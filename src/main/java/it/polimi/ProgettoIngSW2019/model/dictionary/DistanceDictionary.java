package it.polimi.ProgettoIngSW2019.model.dictionary;

import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.common.enums.AreaOfEffect;

import java.util.HashMap;
import java.util.List;

/**
 * Class DistanceDictionary
 * Used to locate a distance from a given position.
 * It will return all possible square no matter if it hasn't player on it.
 * It is just all the square visible from a certain position.
 *
 * @Author: Luca Iovine
 */
public class DistanceDictionary{
    private HashMap<AreaOfEffect, Distance> dictionary = new HashMap<>();
    private Square[][] board;
    /**
     * Constructor
     * All the lambda function will be created here and associated to a key in an hash table
     *
     * @param board the actual map of the game
     * @Author: Luca Iovine
     */
    public DistanceDictionary(Square[][] board){
        this.board = board;
        init();
    }

    /**
     * @param aoe key value to determine which function search in the distance dictionary
     * @param fromPosition position where the distance would be calculated
     * @return the list of squares visible from that position based on the area of effect chosen
     * @Author: Luca Iovine
     */
    //NOT TO BE TESTED
    public List<Square> getTargetPosition(AreaOfEffect aoe, Square fromPosition){
        Distance d = dictionary.get(aoe);
        return d.getDistance(fromPosition);
    }

    /**
     * Add an element to the dictionary.
     * @param aoe area of effect. Is the key value
     * @param distanceType is the distance calculator associated to the aoe.
     */
    //NOT TO BE TESTED
    private void addDistance(AreaOfEffect aoe, Distance distanceType){
        dictionary.put(aoe, distanceType);
    }

    /**
     * Setup of the dictionary
     */
    //NOT TO BE TESTED
    private void init(){
        addDistance(AreaOfEffect.ALL, new All(board));
        addDistance(AreaOfEffect.CANNOT_SEE, new CannotSee(board));
        addDistance(AreaOfEffect.CAN_SEE, new CanSee(board));
        addDistance(AreaOfEffect.CAN_SEE_ATLEAST_ONE, new CanSeeAtLeastOne(board));
        addDistance(AreaOfEffect.CAN_SEE_ATLEAST_TWO, new CanSeeAtLeastTwo(board));
        addDistance(AreaOfEffect.UP_TO_ONE, new UpToOneMovement(board));
        addDistance(AreaOfEffect.NEAR_ROOM_VISIBLE, new NearRoom(board));
        addDistance(AreaOfEffect.SAME_ROOM, new SameRoom(board));
        addDistance(AreaOfEffect.SAME_SQUARE, new SameSquare(board));
        addDistance(AreaOfEffect.UP_TO_TWO, new UpToTwoMovement(board));
        addDistance(AreaOfEffect.NORTH_DIRECTION, new NorthDirection(board));
        addDistance(AreaOfEffect.SOUTH_DIRECTION, new SouthDirection(board));
        addDistance(AreaOfEffect.EAST_DIRECTION, new EastDirection(board));
        addDistance(AreaOfEffect.WEST_DIRECTION, new WestDirection(board));
    }
}


