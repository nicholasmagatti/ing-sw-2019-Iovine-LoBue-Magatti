package it.polimi.ProgettoIngSW2019.view;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.utilities.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class LoginState extends Observable<Event> implements IState{
    Scanner scanner;
    String name;
    boolean firstUser;

    @Override
    public void menu(StateContext stateContext){
        scanner = new Scanner(System.in);

        System.out.println("###########################################");
        System.out.println("##       Benvenuto su adrenalina!        ##");
        System.out.println("###########################################\n");
        System.out.print("Con quale nickname vorresti connetterti: ");

        name = scanner.nextLine();

        System.out.println("\nChe piacere "+name+"!");
        System.out.println("Ora verrai indirizzato nella sala d'attesa, sai bisogna essere almeno in 3 per giocare");


        //TODO: recuperare se è il primo utente o meno

        if(firstUser){
            stateContext.setState(new SetupGameState());
            stateContext.startMenu();
        }
        else{
            stateContext.setState(new WaitState());
            stateContext.startMenu();
        }

    }
}
