package it.polimi.ProgettoIngSW2019.view;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.utilities.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Class that manages the login
 * @author Nicholas Magatti
 */
public class LoginState extends Observable<Event> implements IState{
    Scanner scanner;
    String name;
    boolean firstUser;

    @Override
    public void menu(StateContext stateContext){
        scanner = new Scanner(System.in);

        System.out.println("###########################################");
        System.out.println("##       Welcome on adrenalina!        ##");
        System.out.println("###########################################\n");
        System.out.print("Choose the name for your character!");
        System.out.println("Name: ");

        name = scanner.nextLine();
        //TODO: check if this name is acceptable
        /*if (name already chose by someone else)
                write this name has already been chosen by someone else
          if (name is not acceptable, like empty string or all spaces)
                write that the name is not acceptable
          if( everything is all right):
         */

        System.out.println("Che piacere "+name+"!");
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
