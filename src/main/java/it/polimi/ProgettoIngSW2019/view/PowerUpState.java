package it.polimi.ProgettoIngSW2019.view;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.utilities.*;

import java.util.List;
import java.util.Scanner;

/**
 * @deprecated
 * //TODO: decide if I want to keep this or not (this could be easily be done by some of the other state classes)
 */
public class PowerUpState extends Observable<Event> implements IState {
    //TODO: remove scanner
    List<Integer> idPowerUp;
    Scanner scanner;
    String yesnoChoice;

    @Override
    public void startState(StateManager stateManager) {
        scanner = new Scanner(System.in);

        System.out.println("Hai delle power up che possono essere attivate.");
        System.out.println("Vuoi vedere gli effetti (y/n)? ");
        yesnoChoice = scanner.nextLine();
        if(yesnoChoice.equalsIgnoreCase("y")){
            for(Integer id: idPowerUp)
                showPowerUpInfo(id);
        }

        for(Integer id: idPowerUp) {
            System.out.print("Vuoi attivare la carta " + id +" (y/n)? ");
               yesnoChoice = scanner.nextLine();
               if(yesnoChoice.equalsIgnoreCase("y")){
                   //TODO:
               }
        }
    }

    private String showPowerUpInfo(int idPowerUp){
        return "";
    }
}
