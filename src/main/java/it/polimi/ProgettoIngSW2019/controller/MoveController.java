package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.Message.toController.InfoRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toController.MoveChoiceRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.MoveInfoResponse;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.model.GameTable;
import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.model.TurnManager;
import it.polimi.ProgettoIngSW2019.common.utilities.Observer;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Priscilla Lo Bue
 */
public class MoveController extends Controller {
    private Player ownerPlayer;
    private List<Square> squaresAvailable = new ArrayList<>();
    private Square squareToGrab;



    public MoveController(TurnManager turnManager, VirtualView virtualView, IdConverter idConverter, CreateJson createJson, HostNameCreateList hostNameCreateList) {
        super(turnManager, virtualView, idConverter, createJson, hostNameCreateList);
    }


    public void update(Event event) {
        if (event.getCommand().equals(EventType.REQUEST_MOVE_INFO)) {
            checkInfoFromView(event.getMessageInJsonFormat());
        }

        if (event.getCommand().equals(EventType.REQUEST_MOVE)) {
            movePlayer(event.getMessageInJsonFormat());
        }
    }


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


    private void movePlayer(String messageJson) {
        MoveChoiceRequest moveChoiceRequest = new Gson().fromJson(messageJson, MoveChoiceRequest.class);

        if(ownerPlayer.getIdPlayer() == moveChoiceRequest.getIdPlayer() && ownerPlayer.getHostname().equals(moveChoiceRequest.getHostNamePlayer())) {
            squareToGrab = convertSquare(ownerPlayer, moveChoiceRequest.getCoordinates());

            if((squareToGrab != null) && (squaresAvailable.contains(squareToGrab))) {
                ownerPlayer.moveTo(squareToGrab);
                getTurnManager().decreaseActionsLeft();

                String updatePlayer = getCreateJson().createPlayerLMJson(ownerPlayer);
                sendInfo(EventType.UPDATE_PLAYER_INFO, updatePlayer, getHostNameCreateList().addAllHostName());

                String updateMap = getCreateJson().createMapLMJson();
                sendInfo(EventType.UPDATE_MAP, updateMap, getHostNameCreateList().addAllHostName());

                String messageActionsLeftJson = getCreateJson().createMessageActionsLeftJson(ownerPlayer);
                sendInfo(EventType.MSG_MY_N_ACTION_LEFT, messageActionsLeftJson, getHostNameCreateList().addOneHostName(ownerPlayer));
            }
        }
        else {
            String message = "ERROR: Qualcosa è andato storto!";
            sendInfo(EventType.ERROR, message, getHostNameCreateList().addOneHostName(ownerPlayer));
        }
    }


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

        if(!north)
            squares.add(squareNorth);

        if(!south)
            squares.add(squareSouth);

        if(!east)
            squares.add(squareEast);

        if(!west)
            squares.add(squareWest);

        return squares;
    }



    private void getSquaresToGo(){
        Square squarePlayer = ownerPlayer.getPosition();

        List<Square> firstList = new ArrayList<>();
        List<List<Square>> queue = new ArrayList<>();
        List<List<Square>> queueTemp = new ArrayList<>();

        firstList.add(squarePlayer);
        queue.add(firstList);

        for(int i = 0; i < 3; i++) {
            queueTemp = scoreListSquares(queue);
            queue.clear();
            queue = queueTemp;
        }

        searchDuplicateSquare();

        String moveInfoResponsejson = createMoveInfoResponseJson();
        sendInfo(EventType.RESPONSE_REQUEST_MOVE_INFO, moveInfoResponsejson, getHostNameCreateList().addOneHostName(ownerPlayer));
    }


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



    private String createMoveInfoResponseJson() {
        MoveInfoResponse moveInfoResponse = new MoveInfoResponse(ownerPlayer.getIdPlayer(), squaresAvailable);
        return new Gson().toJson(moveInfoResponse);
    }



}
