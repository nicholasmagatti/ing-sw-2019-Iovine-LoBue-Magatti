package it.polimi.ProgettoIngSW2019.view.state;

import it.polimi.ProgettoIngSW2019.common.message.*;
import it.polimi.ProgettoIngSW2019.common.message.reloadmsg.*;
import it.polimi.ProgettoIngSW2019.utilities.Observable;

import java.util.List;
import java.util.Scanner;

public class ReloadState extends Observable<UserInputMessage> implements IState {
    Scanner scanner;
    String yesnoChoice;
    List<Integer> idUnloadedWeapon;
    int weaponChoice;
    boolean exit = false;

    @Override
    public void menu(StateContext stateContext) {
        scanner = new Scanner(System.in);

        System.out.println("Sei arrivato alla fine del turno.");
        notify(new RequestUnloadedWeaponMessage());

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
                    //TODO: costruire il messaggio
                    notify(new ReloadMessage());
                    notify(new RequestUnloadedWeaponMessage());
                }
            }
            else
                exit = true;
        }
    }

    private void showReloadCost(){

    }
}
