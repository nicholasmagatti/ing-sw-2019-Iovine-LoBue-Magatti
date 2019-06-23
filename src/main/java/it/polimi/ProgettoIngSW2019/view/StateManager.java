package it.polimi.ProgettoIngSW2019.view;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;

/**
 * @author Nicholas Magatti
 */
public class StateManager {
    private static State currentState;

    public StateManager(LoginState loginState){
        //TODO: evaluate if I should create all here or not
        currentState = loginState;
        //TODO: evaluate if I should observers here or not
        currentState.startState();
    }

    protected static void restartCurrentState(){
        currentState.startState();
    }

    static void triggerNextState(State nextState){
        currentState = nextState;
        currentState.startState();
    }

    /**
     * @deprecated
     * @return
     */
    public static State getCurrentState() {
        return currentState;
    }

}
