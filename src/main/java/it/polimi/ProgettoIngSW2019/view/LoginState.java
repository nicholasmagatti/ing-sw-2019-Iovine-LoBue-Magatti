package it.polimi.ProgettoIngSW2019.view;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.utilities.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * @author Nicholas Magatti
 */
public class LoginState extends Observable<Event> implements IState{

    /*
    TODO: if possible, create the object of imputScanner only once in a calss in the View, and pass it
            to the current state with the constructor of the state
    */
    private InputScanner inputScanner = new InputScanner();
    private String name;
    private boolean firstUser;


    @Override
    public void menu(StateContext stateContext){

        System.out.println("###########################################");
        System.out.println("##       Welcome on adrenalina!        ##");
        System.out.println("###########################################\n");
        System.out.print("Choose the name for your character!");
        System.out.println("Name: ");

        inputScanner.read();
        if(inputScanner.isTimeExpired()) {
            inputScanner.close();
            //TODO
        }else{
            name = inputScanner.getInputValue();
            //TODO: check if the input is acceptable:
            //TODO: if yes, do what you have to do
            //TODO: if no
        }

        //TODO: check if this name is acceptable
        /*if (name already chose by someone else)
                write this name has already been chosen by someone else
          if (name is not acceptable, like empty string or all spaces)
                write that the name is not acceptable
          if( everything is all right):
         */

        System.out.println("Che piacere "+name+"!");
        System.out.println("Ora verrai indirizzato nella sala d'attesa, sai bisogna essere almeno in 3 per giocare");

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
