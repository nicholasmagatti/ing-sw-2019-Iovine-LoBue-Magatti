package it.polimi.ProgettoIngSW2019.view;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.Message.toController.InfoRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toController.MoveChoiceRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.MoveInfoResponse;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;

import java.util.ArrayList;
import java.util.List;

public class MoveState extends State{
    Gson gsonReader = new Gson();
    ActionState actionState;
    StringBuilder sb;
    String msg;

    MoveInfoResponse moveInfo;

    /**
     * @author: Luca Iovine
     */
    public MoveState(ActionState actionState){
        this.actionState = actionState;
    }

    @Override
    void startState() {
        InfoRequest infoRequest = new InfoRequest(InfoOnView.getHostname(), InfoOnView.getMyId());
        notifyEvent(infoRequest, EventType.REQUEST_MOVE_INFO);

        interaction(moveInfo);
    }

    @Override
    public void update(Event event) {
        if(event.getCommand().equals(EventType.RESPONSE_REQUEST_MOVE_INFO)){
            moveInfo  = gsonReader.fromJson(event.getMessageInJsonFormat(), MoveInfoResponse.class);
        }
    }

    private void interaction(MoveInfoResponse moveInfo){
        sb = new StringBuilder();
        int i;
        int[] positionChosen;
        List<String> possibleChoice = new ArrayList<>();

        List<int[]> movementList = moveInfo.getCoordinates();

        msg = "You can move to one of these squares: \n";
        sb.append(msg);

        for(i = 0; i < movementList.size(); i++){
            possibleChoice.add(Integer.toString(i+1));
            msg = (i+1) + ": " + ToolsView.coordinatesForUser(movementList.get(i))[0] + ToolsView.coordinatesForUser(movementList.get(i))[1] + "\n";
            sb.append(msg);
        }

        msg = GeneralInfo.CHOOSE_OPTION + i + ") to indicate where you want to move to: ";
        sb.append(msg);
        System.out.print(sb);

        String userChoice = ToolsView.readUserChoice(possibleChoice, false);

        if(userChoice != null){
            positionChosen = movementList.get(Integer.parseInt(userChoice) - 1);

            MoveChoiceRequest moveChoiceRequest = new MoveChoiceRequest(InfoOnView.getHostname(), InfoOnView.getMyId(), positionChosen);
            notifyEvent(moveChoiceRequest, EventType.REQUEST_MOVE);
        }
    }
}
