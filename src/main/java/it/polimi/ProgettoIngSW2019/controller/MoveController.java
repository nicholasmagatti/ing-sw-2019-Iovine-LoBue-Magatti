package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.Message.toController.InfoRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toController.MoveChoiceRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.MoveInfoResponse;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller when the player decide to move
 * @author Priscilla Lo Bue
 */
public class MoveController extends Controller {
    private Player ownerPlayer;
    private List<Square> squaresAvailable = new ArrayList<>();
    private List<int[]> coordinates = new ArrayList<>();
    private Square squareToMove;


    /**
     * Constructor
     * @param turnManager           TurnManager
     * @param virtualView           VirtualView
     * @param idConverter           IdConverter
     * @param createJson            CreateJson
     * @param hostNameCreateList    hostNameCreateList
     */
    public MoveController(TurnManager turnManager, VirtualView virtualView, IdConverter idConverter, CreateJson createJson, HostNameCreateList hostNameCreateList) {
        super(turnManager, virtualView, idConverter, createJson, hostNameCreateList);
    }


    /**
     * update to receive the events
     * @param event     event message from view
     */
    public void update(Event event) {
        if (event.getCommand().equals(EventType.REQUEST_MOVE_INFO)) {
            checkInfoFromView(event.getMessageInJsonFormat());
        }

        if (event.getCommand().equals(EventType.REQUEST_MOVE)) {
            checkMoveFromView(event.getMessageInJsonFormat());
        }
    }


    /**
     * Controls if the info from view are correct to request the squares into move
     * @param messageJson       message from view in json format
     */
    private void checkInfoFromView(String messageJson) {
        InfoRequest infoRequest = new Gson().fromJson(messageJson, InfoRequest.class);

        ownerPlayer = convertPlayer(infoRequest.getIdPlayer(), infoRequest.getHostNamePlayer());

        if(ownerPlayer != null) {
            if(checkHostNameCorrect(ownerPlayer, infoRequest.getHostNamePlayer())) {
                if(checkCurrentPlayer(ownerPlayer) && checkHasActionLeft(ownerPlayer)) {
                    getSquaresToGo();
                }
            }
        }
    }


    /**
     * Controls if the info from view are correct to do the move action
     * @param messageJson       message from view in json format for the move action
     */
    private void checkMoveFromView(String messageJson) {
        MoveChoiceRequest moveChoiceRequest = new Gson().fromJson(messageJson, MoveChoiceRequest.class);

        if(ownerPlayer.getIdPlayer() == moveChoiceRequest.getIdPlayer() && ownerPlayer.getHostname().equals(moveChoiceRequest.getHostNamePlayer())) {
            squareToMove = convertSquare(ownerPlayer, moveChoiceRequest.getCoordinates());

            if(squareToMove != null) {
                if(squaresAvailable.contains(squareToMove)) {
                    movePlayer();
                }
                else {
                    String messageError = GeneralInfo.MSG_ERROR;
                    sendInfo(EventType.ERROR, messageError, getHostNameCreateList().addOneHostName(ownerPlayer));
                }
            }
        }
    }


    /**
     * move player action
     */
    private void movePlayer() {
        ownerPlayer.moveTo(squareToMove);
        getTurnManager().decreaseActionsLeft();

        String updatePlayer = getCreateJson().createPlayerLMJson(ownerPlayer);
        sendInfo(EventType.UPDATE_PLAYER_INFO, updatePlayer, getHostNameCreateList().addAllHostName());

        String updateMap = getCreateJson().createMapLMJson();
        sendInfo(EventType.UPDATE_MAP, updateMap, getHostNameCreateList().addAllHostName());

        String mess = "";
        sendInfo(EventType.MSG_BEFORE_ENEMY_ACTION_OR_RELOAD, mess, getHostNameCreateList().addAllExceptOneHostName(ownerPlayer));

        //inside there is the message for Nick
        msgActionLeft(ownerPlayer);
    }


    /**
     * add consecutive squares not duplicated from a square
     * @param square        square to check
     * @return              list of consecutive squares not duplicated
     */
    private List<Square> consecutiveSquares(Square square) {
        List<Square> squares = new ArrayList<>();

        Square squareNorth = null;
        Square squareSouth = null;
        Square squareEast = null;
        Square squareWest = null;

        boolean north = false;
        boolean south = false;
        boolean east = false;
        boolean west = false;

        if(!square.getIsBlockedAtNorth())
            squareNorth = square.getNorthSquare();

        if(!square.getIsBlockedAtSouth())
            squareSouth = square.getSouthSquare();

        if(!square.getIsBlockedAtEast())
            squareEast = square.getEastSquare();

        if(!square.getIsBlockedAtWest())
            squareWest = square.getWestSquare();


        for(Square s:squaresAvailable) {
            if(s == squareNorth)
                north = true;

            if(s == squareSouth)
                south = true;

            if(s == squareEast)
                east = true;

            if(s == squareWest)
                west = true;
        }

        if(!north && squareNorth!=null)
            squares.add(squareNorth);

        if(!south && squareSouth!=null)
            squares.add(squareSouth);

        if(!east && squareEast!=null)
            squares.add(squareEast);

        if(!west && squareWest!=null)
            squares.add(squareWest);

        return squares;
    }


    /**
     * creates a list with all the squares the player can go with the move action
     */
    private void getSquaresToGo(){
        Square squarePlayer = ownerPlayer.getPosition();

        List<Square> firstList = new ArrayList<>();
        List<List<Square>> queue = new ArrayList<>();
        List<List<Square>> queueTemp = new ArrayList<>();

        firstList.add(squarePlayer);
        queue.add(firstList);

        for(int i = 0; i < 4; i++) {
            queueTemp = scoreListSquares(queue);
            queue.clear();
            queue = queueTemp;
        }

        searchDuplicateSquare();

        coordinates.clear();
        for(Square s:squaresAvailable) {
            coordinates.add(s.getCoordinates(getTurnManager().getGameTable().getMap()));
        }

        String moveInfoResponseJson = createMoveInfoResponseJson();
        sendInfo(EventType.RESPONSE_REQUEST_MOVE_INFO, moveInfoResponseJson, getHostNameCreateList().addOneHostName(ownerPlayer));
    }


    /**
     * from the consecutive squares of the square, check for each squares of the consecutive squares his not duplicated consecutive squares
     * @param queue         list of consecutive squares to check
     * @return              list of list of squares
     */
    private List<List<Square>> scoreListSquares( List<List<Square>> queue) {
        List<List<Square>> queueNew = new ArrayList<>();

        for(List<Square> squareLine: queue) {
            for (Square s : squareLine) {
                squaresAvailable.add(s);
                queueNew.add(consecutiveSquares(s));
            }
        }
        return queueNew;
    }


    /**
     * search into squaresAvailable some duplicated squares and remove it
     */
    private void searchDuplicateSquare() {
        for(int i = 0; i < squaresAvailable.size(); i++) {
            for(int j = i+1; j < squaresAvailable.size()-1; j++ ) {
                if(squaresAvailable.get(i) == squaresAvailable.get(j)) {
                    squaresAvailable.remove(j);
                    j = j -1;
                }
            }
        }
    }


    /**
     * generates the message to the view with all the coordinates of the squares the player can move
     * @return         message in json format
     */
    private String createMoveInfoResponseJson() {
        MoveInfoResponse moveInfoResponse = new MoveInfoResponse(ownerPlayer.getIdPlayer(), coordinates);
        return new Gson().toJson(moveInfoResponse);
    }
}
