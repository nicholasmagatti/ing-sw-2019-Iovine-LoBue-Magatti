package it.polimi.ProgettoIngSW2019.view;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.Message.toController.InfoRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toController.MoveChoiceRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.MoveInfoResponse;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;

import java.util.ArrayList;
import java.util.List;

public class MoveState extends State{
    Gson gsonReader;
    ActionState actionState;
    StringBuilder sb;
    String msg;

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
    }

    @Override
    public void update(Event event) {
        if(event.getCommand().equals(EventType.RESPONSE_REQUEST_MOVE_INFO)){
            sb = new StringBuilder();
            int i;
            int[] positionChosen;
            List<String> possibleChoice = new ArrayList<>();
            MoveInfoResponse moveInfo = gsonReader.fromJson(event.getMessageInJsonFormat(), MoveInfoResponse.class);
            List<int[]> movementList = moveInfo.getCoordinates();

            msg = "Puoi muoverti in una di queste caselle: \n";
            sb.append(msg);

            for(i = 0; i < movementList.size(); i++){
                possibleChoice.add(Integer.toString(i+1));
                msg = (i+1) + ": " + ToolsView.coordinatesForUser(movementList.get(i)) + "\n";
                sb.append(msg);
            }

            msg = "Scegli un numero (da 1 a" + i + ") per indicare dove vuoi muoverti: ";
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
}
