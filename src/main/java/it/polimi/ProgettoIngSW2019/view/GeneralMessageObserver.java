package it.polimi.ProgettoIngSW2019.view;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.Observer;

/**
 * Part of the view that manages the messages for visualization received from the server.
 * @author Nicholas Magatti
 */
public class GeneralMessageObserver implements Observer<Event> {

    /**
     * Print the error received from the controller and restart the state in which it was
     * @param message - error message to print to the user
     */
    private void printErrorAndRestart(String message){
        System.out.println(message);
        //restart the current state from the beginning
        StateManager.restartCurrentState();
    }

    @Override
    public void update(Event event){

        EventType command = event.getCommand();
        String jsonMessage = event.getMessageInJsonFormat();

        if(command == EventType.ERROR) {
            String message = new Gson().fromJson(jsonMessage, String.class);
            printErrorAndRestart(message);
        }

        if(command == EventType.INPUT_TIME_EXPIRED){
            ToolsView.getInputScanner().close();
            System.out.println("Time expired! You have been suspended.");
            System.out.println("You can ");
        }

        if(command == EventType.MSG_POWERUP_DISCARDED_TO_SPAWN){
            //TODO: dire quello che è successo

        }
    }
}
