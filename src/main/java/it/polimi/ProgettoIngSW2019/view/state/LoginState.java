package it.polimi.ProgettoIngSW2019.view.state;

import it.polimi.ProgettoIngSW2019.common.message.ConnectionMessage;
import it.polimi.ProgettoIngSW2019.utilities.Observable;
import it.polimi.ProgettoIngSW2019.utilities.Observer;

import java.util.Scanner;

public class LoginState extends Observable<ConnectionMessage> implements IState, Observer<Boolean>{
    Scanner scanner;
    String name;
    ConnectionMessage connectionMsg;
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

        connectionMsg = new ConnectionMessage(name);
        notify(connectionMsg);

        //TODO: recuperare se è il primo utente o meno

        if(firstUser){
            stateContext.setState(new SetupGameState());
            stateContext.startMenu();
        }
        else{
            stateContext.setState(new SetupGameState());
            stateContext.startMenu();
        }

    }

    @Override
    public void update(Boolean isFirstUser) {

    }
}
