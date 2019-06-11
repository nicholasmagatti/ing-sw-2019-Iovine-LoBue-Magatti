package it.polimi.ProgettoIngSW2019.view;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.utilities.*;

import java.util.List;
import java.util.Scanner;

/**
 * @author Nicholas Magatti
 */
public class SpawnState extends Observable<Event> implements IState{
    EnemyTurnState enemyTurnState;
    MyActionsState myActionsState;
    //TODO: remove scanner
    Scanner scanner;
    List<Integer> idPowerUp;
    String yesnoChoice;
    int powerupChoice;

    @Override
    public void startState(StateManager stateManager) {
        System.out.println("Questi sono i power up che hai in mano.");
        for(Integer id: idPowerUp){
            //TODO: mostrare il nome carta
            System.out.println(id);
        }
        System.out.println("Vuoi vedere gli effetti prima di scartare (y/n)?");
        yesnoChoice = scanner.nextLine();

        if(yesnoChoice.equalsIgnoreCase("y")){
            for(Integer id: idPowerUp){
                //TODO: mostrare il nome carta
                showPowerUpInfo(id);
            }
        }

        System.out.println("Scegli la carta da scartare, il colore della stanza indica il punto di respawn: ");
        for(Integer id: idPowerUp){
            //TODO: mostrare il nome carta e colore dell'ammo
            System.out.println(id);
        }
        powerupChoice = scanner.nextInt();

        //TODO: if else
        stateManager.triggerNextState(myActionsState);
        stateManager.triggerNextState(enemyTurnState);
    }

    private void showPowerUpInfo(int idPowerUp){

    }
}
