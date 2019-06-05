package it.polimi.ProgettoIngSW2019.view;

/**
 * @author Nicholas Magatti
 */
public class StateManager {
    private IState nextState;

    public StateManager(){
        nextState = new LoginState();
        //TODO: evaluate if I should create all here or not
        //TODO: evaulate if I should observers here or not
        startMenu();
    }

    public void triggerNextState(IState nextState){
        this.nextState = nextState;
        startMenu();
    }

    private void startMenu(){
        nextState.menu(this);
    }
}
