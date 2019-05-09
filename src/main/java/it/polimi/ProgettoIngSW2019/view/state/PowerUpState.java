package it.polimi.ProgettoIngSW2019.view.state;

import it.polimi.ProgettoIngSW2019.common.message.*;
import it.polimi.ProgettoIngSW2019.utilities.Observable;

import java.util.List;
import java.util.Scanner;

public class PowerUpState extends Observable<UserInputMessage> implements IState {
    List<Integer> idPowerUp;
    Scanner scanner;
    String yesnoChoice;

    @Override
    public void menu(StateContext stateContext) {
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
                   //TODO: nel messaggio c'è l'id della carta che viene attivata
                   notify(new PowerUpMessage());
               }
        }
    }

    private String showPowerUpInfo(int idPowerUp){
        return "";
    }
}
