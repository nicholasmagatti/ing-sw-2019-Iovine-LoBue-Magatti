package it.polimi.ProgettoIngSW2019.view;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.LightModel.MapLM;
import it.polimi.ProgettoIngSW2019.common.LightModel.SquareLM;
import it.polimi.ProgettoIngSW2019.common.Message.toController.SetupRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.SetupResponse;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.common.utilities.TypeAdapterSquareLM;

import java.util.ArrayList;
import java.util.List;

/**
 * State in which the first user is when he/she is choosing the map to use and the number of skulls for the game
 * @author Nicholas Magatti
 */
public class SetupGameState extends State {

    private boolean gotInfoBeforeStartGame = false;
    private boolean isReconnection = false;
    private String myName;
    private String hostname;
    private String playerWhoSetsTheGame;
    private List<MapLM> maps;
    private IdleState idleState;

    private SpawnState spawnState;

    public SetupGameState(SpawnState spawnState, IdleState idleState){
        this.spawnState = spawnState;
        this.idleState = idleState;
    }

    /**
     * Initial settings for this state
     * @param myName
     * @param hostname
     * @param maps
     * @param playerWhoSetsTheGame
     */
    void setInfoBeforeStartGame(String myName, String hostname, List<MapLM> maps, String playerWhoSetsTheGame){
        this.myName = myName;
        this.hostname = hostname;
        this.maps = maps;
        this.playerWhoSetsTheGame = playerWhoSetsTheGame;
        gotInfoBeforeStartGame = true;
    }

    /**
     * In case of attempted reconnection, set the inserted acceptable credentials.
     * @param myName
     * @param hostname
     */
    void setInfoForReconnection(String myName, String hostname){
        this.myName = myName;
        this.hostname = hostname;
        //gotInfoForReconnection = true;
        isReconnection = true;
    }


    @Override
    public void startState() {
        if(!gotInfoBeforeStartGame){
            throw new RuntimeException("You did not set the required information before the start of the state, with " +
                    "the method setInfoBeforeStart(...).");
        }
        //set beck to false in case the startState is called again without setting the new information of that new context:
        gotInfoBeforeStartGame = false;

        if(playerWhoSetsTheGame.equals(myName)){
            chooseSetting();
            spawnState.triggerFirstSpawn();
        }
        else{
            System.out.println(playerWhoSetsTheGame + " is setting the game. Wait...");
            StateManager.triggerNextState(idleState);
        }

    }

    @Override
    public void update(Event event) {
        EventType command = event.getCommand();
        String jsonMessage = event.getMessageInJsonFormat();

        if(command == EventType.RESPONSE_GAME_DATA ||
                command == EventType.RESPONSE_SETUP){
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(SquareLM.class, new TypeAdapterSquareLM())
                    .create();
            SetupResponse setupResponse = gson.fromJson(jsonMessage, SetupResponse.class);
            createLocalLightModelAndStart(setupResponse);
        }

    }

    /**
     * Set number map and number of skulls and send that to the controller, if the timer does not expire first.
     */
    void chooseSetting(){
        System.out.println("Choose the setting of the game: the map and the number of skulls to use.");
        int map, skulls;
        String inputFromUser;
        inputFromUser = chooseMap();

        if(inputFromUser != null){ //time not expired
            map = Integer.parseInt(inputFromUser);

            inputFromUser = chooseNrSkulls();

            if(inputFromUser != null){ //time not expired
                skulls = Integer.parseInt(inputFromUser);

                //after the player set everything
                System.out.println("Wait a few seconds...");

                //notify choice to server
                SetupRequest setupRequest = new SetupRequest(hostname, map, skulls);
                notifyEvent(setupRequest, EventType.REQUEST_SETUP);
            }
        }
    }

    /**
     * Make the first player choose the map to use for this game and return the chosen option as a number.
     * @return number that represent the chosen map.
     */
    private String chooseMap(){
        //print maps
        List<String> allowedAnswers = new ArrayList<>();
        for(int i=0; i < maps.size(); i++){
            System.out.println("MAP NUMBER " + (i+1) + ":");
            System.out.println();
            ToolsView.printTheSpecifiedMap(maps.get(i));
            System.out.println("\n");
            allowedAnswers.add(Integer.toString(i+1));
        }

        System.out.print("Type the number of the chosen map: ");
        return ToolsView.readUserChoice(allowedAnswers, false);
    }

    /**
     * Make the first player choose number of skulls to use for this game and return the chosen option as a number.
     * @return chosen number of skulls for the game
     */
    private String chooseNrSkulls(){
        System.out.println("Choose a number of skulls for the killsho track between " +
                GeneralInfo.MIN_SKULLS + " and " + GeneralInfo.MAX_SKULLS +".");
        System.out.println("The skulls represent the number of kills to get to the end of the game.");
        System.out.print("Number of skulls: ");
        List<String> allowedAnswers = new ArrayList<>();
        for(int nSkulls = GeneralInfo.MIN_SKULLS; nSkulls <= GeneralInfo.MAX_SKULLS; nSkulls++){
            allowedAnswers.add(Integer.toString(nSkulls));
        }
        return ToolsView.readUserChoice(allowedAnswers, false);
    }

    /**
     * Create first version of light model, then start the game, acting differently based on
     * the fact this is a reconnection or a normal connection before the game starts.
     * @param setupResponse - the object that contains all the information about the light model and the first player
     */
    void createLocalLightModelAndStart(SetupResponse setupResponse){

        InfoOnView.createFirstLightModel(
                setupResponse.getMyId(), myName, setupResponse.getMyHostname(),
                setupResponse.getPlayers(), setupResponse.getMyLoadedWeaponsLM(),
                setupResponse.myPowerUpLM(), setupResponse.getMap(), setupResponse.getKillshotTrack());

        if(isReconnection){ //if the palyer is reconnecting to the ongoing game
            InfoOnView.printEverythingVisible();
        }
        else { //if the game is starting now
            System.out.println("The game starts now. Good luck!");
            InfoOnView.printEverythingVisible();
            //if the first player is the one on this client, trigger the first spawn for him/her
        }
    }

}
