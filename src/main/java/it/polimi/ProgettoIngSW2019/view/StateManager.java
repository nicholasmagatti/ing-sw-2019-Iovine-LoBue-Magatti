package it.polimi.ProgettoIngSW2019.view;

/**
 * @author Nicholas Magatti
 */
public class StateManager {
    private IState currentState;

    public StateManager(){
        currentState = new LoginState();
        //TODO: evaluate if I should create all here or not
        //TODO: evaulate if I should observers here or not
        startMenu();
    }

    public void triggerNextState(IState nextState){
        currentState = nextState;
        startMenu();
    }

    public IState getCurrentState() {
        return currentState;
    }

    private void startMenu(){
        currentState.startState(this);
    }
}
