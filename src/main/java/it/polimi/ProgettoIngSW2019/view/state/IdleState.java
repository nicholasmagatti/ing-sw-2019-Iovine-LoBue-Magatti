package it.polimi.ProgettoIngSW2019.view.state;

import it.polimi.ProgettoIngSW2019.utilities.Observer;

import java.util.Scanner;

public class IdleState implements IState {
    Scanner scanner;
    String yesnoChoice;
    int infoChoice;
    boolean exit = false;

    @Override
    public void menu(StateContext stateContext) {
        while(!exit) {
            System.out.print("Mentre sei in attesa, vuoi vedere le info di qualcosa (y/n)? ");
            if (yesnoChoice.equalsIgnoreCase("y")) {
                System.out.println("Cosa vuoi vedere: ");
                System.out.println("1 - Armi");
                System.out.println("2 - Power Up");
                System.out.println("3 - Tuoi punti ferita");
                System.out.println("4 - Punti ferita avversario");
                System.out.print("Scelta: ");

                infoChoice = scanner.nextInt();

                switch (infoChoice) {
                    case 1:
                        showWeapon();
                        break;
                    case 2:
                        showPowerUp();
                        break;
                    case 3:
                        showYourDamageLine();
                        break;
                    case 4:
                        showEnemyDamageLine();
                        break;
                }
            }
        }
        stateContext.setState(new PlayerChoiceState());
        stateContext.startMenu();
    }

    public void showWeapon(){
        //TODO
    }

    public void showPowerUp(){
        //TODO
    }

    public void showYourDamageLine(){
        //TODO
    }

    public void showEnemyDamageLine(){
        //TODO
    }

    private class ChangeContext implements Observer<Boolean> {
        @Override
        public void update(Boolean message) {
            exit = message;
        }
    }
}
