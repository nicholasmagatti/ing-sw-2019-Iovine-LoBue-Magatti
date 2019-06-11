package it.polimi.ProgettoIngSW2019.view;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.*;

/**
 * @author Nicholas Magatti
 */
public class LoginState extends Observable<Event> implements Observer<Event>, IState{

    private InputScanner inputScanner = new InputScanner();
    private String name;
    private boolean firstUser;



    @Override
    public void startState(StateManager stateManager){

        System.out.println("###########################################");
        System.out.println("##       Welcome on adrenalina!        ##");
        System.out.println("###########################################\n");
        System.out.print("Choose te name for your character!");
        System.out.println("Name: ");

        //TODO: REMEMBER update does inputScanner.close() if time expires
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
            stateManager.triggerNextState(new SetupGameState());
        }
        else{
            stateManager.triggerNextState(new WaitState());
        }

    }
    //TODO: evaluate: delete or keep?
    @Override
    public void update(Event message){
        if(message.getCommand() == EventType.INPUT_TIME_EXPIRED){
            inputScanner.close();
        }
    }
}
