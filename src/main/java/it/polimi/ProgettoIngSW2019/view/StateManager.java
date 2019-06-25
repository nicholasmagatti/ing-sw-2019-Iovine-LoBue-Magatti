package it.polimi.ProgettoIngSW2019.view;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;

/**
 * @author Nicholas Magatti
 */
public class StateManager {
    private static State currentState = null;

    /**
     * Get the current state
     * @return the current state set in StateManager. Note: it can be null.
     */
    static State getCurrentState() {
        return currentState;
    }

    /**
     * Restart from the beginning of the current state. Used when an error in the communication with the controller occurs.
     */
    static void restartCurrentState(){
        currentState.startState();
    }

    /**
     * Trigger the specified stated from any part of the view.
     * @param nextState - state to trigger
     */
    static void triggerNextState(State nextState){
        currentState = nextState;
        currentState.startState();
    }

    /**
     * Trigger the specified state from any part of the program: used outside the package 'view'.
     * @param state
     */
    public void triggerState(State state){
        triggerNextState(state);
    }

    /**
     * Set the current state to null: the user is just watching the game until an action from him/her will be requested.
     */
    static void setNullState(){
        currentState = null;
    }

}
