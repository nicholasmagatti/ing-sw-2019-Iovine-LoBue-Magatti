package it.polimi.ProgettoIngSW2019.view;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.utilities.*;

import java.util.List;
import java.util.Scanner;

/**
 * @author Nicholas Magatti
 */
public class ReloadState extends Observable<Event> implements IState {
    //TODO: remove scanner
    private Scanner scanner;
    private String yesnoChoice;
    private List<Integer> idUnloadedWeapon;
    private int weaponChoice;
    private boolean exit = false;

    @Override
    public void menu(StateContext stateContext) {
        scanner = new Scanner(System.in);

        System.out.println("Sei arrivato alla fine del turno.");
        //TODO

        while(!idUnloadedWeapon.isEmpty() || exit) {
            System.out.print("Hai delle armi che puoi ricaricare, vuoi procedere (y/n)? ");
            yesnoChoice = scanner.nextLine();

            if (yesnoChoice.equalsIgnoreCase("y")) {
                System.out.println("Scegli l'arma che vuoi ricaricare: ");
                for (Integer id : idUnloadedWeapon) {
                    //TODO: stampare il nome della carta
                    System.out.println(id);
                }
                System.out.print("Scelta: ");
                weaponChoice = scanner.nextInt();

                System.out.println("Utilizzerai le seguenti munizioni, va bene (y/n)?");
                showReloadCost();
                System.out.print("\nScelta: ");
                yesnoChoice = scanner.nextLine();
                if(yesnoChoice.equalsIgnoreCase("y")) {
                    //TODO
                }
            }
            else
                exit = true;
        }
    }

    private void showReloadCost(){

    }
}
