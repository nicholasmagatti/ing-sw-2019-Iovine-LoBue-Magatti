package it.polimi.ProgettoIngSW2019.view;

/**
 * State of wait before the start of the game
 * @author Nicholas Magatti
 */
public class WaitState implements IState {

    EnemyTurnState enemyTurnState;
    MyActionsState myActionsState;
    StateManager stateManager;

    //TODO: create (with new etc) all the states that will be used during the game (playerchoice, powerupstate etc)

    @Override
    public void startState(StateManager stateManager) {

        //TODO if else
        stateManager.triggerNextState(enemyTurnState);
        stateManager.triggerNextState(myActionsState);
    }
}
