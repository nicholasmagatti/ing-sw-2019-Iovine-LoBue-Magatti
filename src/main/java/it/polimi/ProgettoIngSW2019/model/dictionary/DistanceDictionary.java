package it.polimi.ProgettoIngSW2019.model.dictionary;

import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.common.enums.AreaOfEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Class DistanceDictionary
 * Used to locate a distance from a given position.
 * It will return all possible square no matter if it hasn't player on it.
 * It is just all the square visible from a certain position.
 *
 * @Author: Luca Iovine
 */
public class DistanceDictionary{
    private static HashMap<AreaOfEffect, Distance> dictionary = new HashMap<>();
    private static Square[][] board;
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
    public static List<Square> getTargetPosition(AreaOfEffect aoe, Square fromPosition){
        Distance d = dictionary.get(aoe);
        return d.getDistance(fromPosition);
    }

    private void addDistance(AreaOfEffect aoe, Distance distanceType){
        dictionary.put(aoe, distanceType);
    }

    private void init(){
        addDistance(AreaOfEffect.ALL, new All(board));
        addDistance(AreaOfEffect.CANNOT_SEE, new CannotSee(board));
        addDistance(AreaOfEffect.CAN_SEE, new CanSee(board));
        addDistance(AreaOfEffect.CAN_SEE_ATLEAST_ONE, new CanSeeAtLeastOne(board));
        addDistance(AreaOfEffect.CAN_SEE_ATLEAST_TWO, new CanSeeAtLeastTwo(board));
        addDistance(AreaOfEffect.EXACTLY_ONE, new ExactlyOneMovement(board));
        addDistance(AreaOfEffect.NEAR_ROOM_VISIBLE, new NearRoom(board));
        addDistance(AreaOfEffect.SAME_ROOM, new SameRoom(board));
        addDistance(AreaOfEffect.SAME_SQUARE, new SameSquare(board));
    }
}


