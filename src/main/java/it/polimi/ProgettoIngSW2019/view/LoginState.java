package it.polimi.ProgettoIngSW2019.view;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * @author Nicholas Magatti
 */
public class LoginState extends Observable<Event> implements Observer<Event>, IState{

    private InputScanner inputScanner = new InputScanner();
    private String name;
    private boolean firstUser;



    @Override
    public void menu(StateContext stateContext){

        System.out.println("###########################################");
        System.out.println("##       Welcome on adrenalina!        ##");
        System.out.println("###########################################\n");
        System.out.print("Choose te name for your character!");
        System.out.println("Name: ");


        boolean gotAcceptableResult = false;
        while(!inputScanner.isTimeExpired() && !gotAcceptableResult){
            inputScanner.read();
            if(!inputScanner.isTimeExpired()){
                inputScanner.getInputValue();
                //TODO: what do I do here?
            }
        }
        inputScanner.close();

        if(gotAcceptableResult){
            //TODO
        }
        else{
            //TODO
        }

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

    @Override
    public void update(Event message){
        if(message.getCommand() == EventType.INPUT_TIME_EXPIRED){
            inputScanner.close();
        }
    }
}
