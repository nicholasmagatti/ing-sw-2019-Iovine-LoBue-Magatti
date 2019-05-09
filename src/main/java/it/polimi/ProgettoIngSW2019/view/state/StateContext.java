package it.polimi.ProgettoIngSW2019.view.state;

public class StateContext {
    private IState state;

    public StateContext(){
        state = new LoginState();
    }

    public void setState(IState nextState){
        state = nextState;
    }

    public void startMenu(){
        state.menu(this);
    }
}
