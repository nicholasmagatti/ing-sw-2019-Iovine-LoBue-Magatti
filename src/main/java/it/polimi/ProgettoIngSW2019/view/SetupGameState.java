package it.polimi.ProgettoIngSW2019.view;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.LightModel.MapLM;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.Observable;

import java.util.List;
import java.util.Scanner;

/**
 * State in which the first user is when he/she is choosing the map to use and the number of skulls for the game
 * @author Nicholas Magatti
 */
public class SetupGameState extends State {
    boolean chooseGameSettings;
    List<MapLM> maps;


    @Override
    public void startState() {


        /*
        //TODO: remove scanner
        Scanner scanner = new Scanner(System.in);
        int mapChoice;
        int skullChoice;
        boolean checkAnswer = false;



        System.out.println("###########################################\n");
        System.out.println("Ma che piacevole sorpresa, quindi tu saresti il primo giocatore a connettersi.");
        System.out.println("Per te ho un compito importante, mi dovrai dire con che mappa vorrai giocare e con quali regole");
        System.out.println("Which map do you choose?");

        //TODO: in futuro farà notify con un messaggio di tipo "RequestMap" per fargli vedere le mappe prima di scegliere
        //TODO: do this with a for loop instead(so that it is scalable if the number of maps changes)
        System.out.println("1 - Mappa A");
        System.out.println("2 - Mappa B");
        System.out.println("3 - Mappa C");
        System.out.println("4 - Mappa D");

        System.out.println("Which map do you choose?");

        while(!checkAnswer) {
            System.out.print("Scegli [1 - 4] per selezionare la mappa: ");
            mapChoice = scanner.nextInt();
            //TODO: write maps.size() instead of 4
            if(mapChoice >= 1 && mapChoice <= 4)
                checkAnswer = true;
            else
                System.out.println("La scelta fatta non è ammissibile. Ritenta, sarai più fortunato.\n");
        }

        checkAnswer = false;

        System.out.println("Great! How many skulls do you want to use for this game? Choose a number between 5 and 8.");
        System.out.println("Remember: more skulls mean longer game.");
        while(!checkAnswer) {
            System.out.println("Number of skulls: ");
            skullChoice = scanner.nextInt();
            if(skullChoice >= 5 && skullChoice <= 8)
                checkAnswer = true;
            else
                System.out.println("Wrong input. Please: choose a number between 5 and 8.");
        }

        System.out.println("Setting the game...");
        //TODO

        System.out.println("Waiting for the other players...");
        System.out.println("We are almost ready!");

        stateManager.triggerNextState(new WaitState());*/
    }

    @Override
    public void update(Event event) {
        EventType command = event.getCommand();
        String jsonMessage = event.getMessageInJsonFormat();

        /*//initialize the boolean youCanChooseGameSettings, then trigger this state as next state
        if(command == EventType.GO_IN_GAME_SETUP){
            MessageConnection messageConnection = new Gson().fromJson(jsonMessage, MessageConnection.class);
            if(messageConnection.getId() == InfoOnView.getMyId()){
                youCanChooseGameSettings = true;
            }
            else{
                youCanChooseGameSettings = false;
            }
            StateManager.triggerNextState(this);
        }*/
    }

    void setChooseGameSettings(boolean chooseGameSettings) {
        this.chooseGameSettings = chooseGameSettings;
    }

    void setMaps(List<MapLM> maps) {
        this.maps = maps;
    }
}
