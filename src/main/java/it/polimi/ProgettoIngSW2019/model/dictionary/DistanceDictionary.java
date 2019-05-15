package it.polimi.ProgettoIngSW2019.model.dictionary;

import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.common.enums.AreaOfEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

interface ITargetDistance{
    List<Square> fromPosition(Square pos);
}

/**
 * Class DistanceDictionary
 * Used to locate a distance from a given position.
 * It will return all possible square no matter if it hasn't player on it.
 * It is just all the square visible from a certain position.
 *
 * @Author: Luca Iovine
 */
public class DistanceDictionary{
    HashMap<AreaOfEffect, ITargetDistance> distanceDictionary = new HashMap<>();

    /**
     * Constructor
     * All the lambda function will be created here and associated to a key in an hash table
     *
     * @param board the actual map of the game
     * @Author: Luca Iovine
     */
    public DistanceDictionary(Square[][] board){
        /**
         * @return the rooms it can see from the current position through the doors
         * @Author: Luca Iovine
         */
        ITargetDistance nearRoomVisible = (currentPosition) ->{
            int idRoom;
            List<Integer> listIdRoom = new ArrayList<>();
            List<Square> targetPosition = new ArrayList<>();

            for(Square cardinalSquare: currentPosition.getSquaresVisibleFromHere()){
                if(!listIdRoom.contains(cardinalSquare.getIdRoom()) &&
                        cardinalSquare.getIdRoom() != currentPosition.getIdRoom()){
                    listIdRoom.add(cardinalSquare.getIdRoom());
                }
            }

            for(Square[] s: board){
                for(Square square: s){
                    idRoom = square.getIdRoom();
                    if(listIdRoom.contains(idRoom)){
                        if(!square.getPlayerOnSquare().isEmpty()){
                            targetPosition.add(square);
                        }
                    }
                }
            }

            return targetPosition;
        };
        distanceDictionary.put(AreaOfEffect.NEAR_ROOM_VISIBLE, nearRoomVisible);

        /**
         * @return the squares which are in the same room of the current position
         * @Author: Luca Iovine
         */
        ITargetDistance sameRoom = (currentPosition) ->{
            List<Square> targetPosition = new ArrayList<>();

            for(Square[] s: board){
                for(Square square: s){
                    if(currentPosition.getIdRoom() == square.getIdRoom()){
                        if(!square.getPlayerOnSquare().isEmpty()){
                            targetPosition.add(square);
                        }
                    }
                }
            }

            return targetPosition;
        };
        distanceDictionary.put(AreaOfEffect.SAME_ROOM, sameRoom);

        /**
         * @return all the squares visible from the current position.
         * @Author: Luca Iovine
         */
        ITargetDistance canSee = (currentPosition)->{
            BiFunction<ITargetDistance, ITargetDistance, List<Square>> canSeeFunction = (nearRoomVisibleLambda, sameRoomLambda) -> {
                List<Square> sameRoomSquare = nearRoomVisibleLambda.fromPosition(currentPosition);
                List<Square> nearRoomSquare = sameRoomLambda.fromPosition(currentPosition);

                for(Square near: nearRoomSquare){
                    sameRoomSquare.add(near);
                }
                return sameRoomSquare;
            };

            return canSeeFunction.apply(nearRoomVisible, sameRoom);
        };
        distanceDictionary.put(AreaOfEffect.CAN_SEE, canSee);

        /**
         * @return the current position
         * @Author: Luca Iovine
         */
        ITargetDistance sameSquare = (currentPosition) ->{
            List<Square> targetPosition = new ArrayList<>();
            targetPosition.add(currentPosition);

            return targetPosition;
        };
        distanceDictionary.put(AreaOfEffect.SAME_SQUARE, sameSquare);

        /**
         * @return the squares visible from the current position except the current position itself
         * @Author: Luca Iovine
         */
        ITargetDistance canSeeAtLeastOne = (currentPosition)->{
            BiFunction<ITargetDistance, ITargetDistance, List<Square>> atLeastOneFunction = (canSeeLambda, sameSquareLambda) -> {
                List<Square> squareCanSee = canSeeLambda.fromPosition(currentPosition);
                List<Square> squareToRemove = sameSquareLambda.fromPosition(currentPosition);

                if(squareCanSee.isEmpty()){
                    return null;
                }

                for(Square toRemoveSquare: squareToRemove){
                    if(squareCanSee.contains(toRemoveSquare)){
                        squareCanSee.remove(toRemoveSquare);
                    }
                }
                return squareCanSee;
            };

            return atLeastOneFunction.apply(canSee, sameSquare);
        };
        distanceDictionary.put(AreaOfEffect.CAN_SEE_ATLEAST_ONE, canSeeAtLeastOne);

        /**
         * @return the squares that are exactlty one movement away from the current position
         * @Author: Luca Iovine
         */
        ITargetDistance exactlyOneMovement = (currentPosition)-> currentPosition.getSquaresVisibleFromHere();
        distanceDictionary.put(AreaOfEffect.EXACTLY_ONE, exactlyOneMovement);

        /**
         * @return the squares visible from the current position which are at least two movement away from the current position
         * @Author: Luca Iovine
         */
        ITargetDistance canSeeAtLeastTwo = (currentPosition)->{
            BiFunction<ITargetDistance, ITargetDistance, List<Square>> atLeastTwoFunction = (canSeeLambda, exactlyOneMovementLambda) -> {
                List<Square> squareCanSee = canSeeLambda.fromPosition(currentPosition);
                List<Square> squareToRemove = exactlyOneMovementLambda.fromPosition(currentPosition);

                if(squareCanSee.isEmpty()){
                    return null;
                }

                squareCanSee.remove(currentPosition);
                for(Square toRemoveSquare: squareToRemove){
                    if(squareCanSee.contains(toRemoveSquare)){
                        squareCanSee.remove(toRemoveSquare);
                    }
                }
                return squareCanSee;
            };

            return atLeastTwoFunction.apply(canSee, exactlyOneMovement);
        };
        distanceDictionary.put(AreaOfEffect.CAN_SEE_ATLEAST_TWO, canSeeAtLeastTwo);

        /**
         * @return the square which are NOT visible from the current position
         * @Author: Luca Iovine
         */
        ITargetDistance cannotSee = (currentPosition) ->{
            Function<ITargetDistance, List<Square>> cannotSeeFunction = (canSeeLambda)->{
                List<Square> targetPosition = new ArrayList<>();
                List<Square> toRemoveSquare = canSeeLambda.fromPosition(currentPosition);

                for(Square[] s: board){
                    for(Square square: s){
                        if(!toRemoveSquare.contains(square)){
                            targetPosition.add(square);
                        }
                    }
                }
                return targetPosition;
            };

            return cannotSeeFunction.apply(canSee);
        };
        distanceDictionary.put(AreaOfEffect.CANNOT_SEE, cannotSee);

        /**
         * @return all the squares of the map
         * @Author: Luca Iovine
         */
        ITargetDistance all = (currentPosition)->{
            List<Square> targetPosition = new ArrayList<>();
            for(Square[] s: board){
                for(Square square: s){
                    targetPosition.add(square);
                }
            }
            return targetPosition;
        };
        distanceDictionary.put(AreaOfEffect.ALL, all);
    }

    /**
     * @param aoe key value to determine which function search in the distance dictionary
     * @param fromPosition position where the distance would be calculated
     * @return the list of squares visible from that position based on the area of effect chosen
     * @Author: Luca Iovine
     */
    public List<Square> getTargetPosition(AreaOfEffect aoe, Square fromPosition){
        return distanceDictionary.get(aoe).fromPosition(fromPosition);
    }
}


