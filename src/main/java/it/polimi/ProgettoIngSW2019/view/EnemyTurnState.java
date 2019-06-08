package it.polimi.ProgettoIngSW2019.view;

import java.util.Scanner;

/**
 * @author Nicholas Magatti
 */
public class EnemyTurnState implements IState {
    //TODO: remove scanner
    private Scanner scanner;
    private String yesnoChoice;
    private int infoChoice;
    private boolean exit = false;
    private MyActionsState myActionsState;

    @Override
    public void menu(StateManager stateManager) {
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
        stateManager.triggerNextState(myActionsState);
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
}