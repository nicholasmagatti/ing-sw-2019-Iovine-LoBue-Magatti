package it.polimi.ProgettoIngSW2019.view;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.LightModel.PowerUpLM;
import it.polimi.ProgettoIngSW2019.common.Message.toController.InfoRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toController.SpawnChoiceRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.DrawCardsInfoResponse;
import it.polimi.ProgettoIngSW2019.common.Message.toView.MessagePowerUpsDiscarded;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nicholas Magatti
 */
public class SpawnState extends State{

    private boolean firstSpawn = false;
    private IdleState idleState;
    private DrawCardsInfoResponse drawCardsInfo;

    /**
     * Constructor
     * @param idleState
     */
    public SpawnState(IdleState idleState){
        this.idleState = idleState;
    }

    /**
     * Enters in this state when triggered and manage all the possible interactions.
     */
    @Override
    public void startState() {

        //InfoOnView.printEverythingVisible();

        InfoRequest infoRequest = new InfoRequest(InfoOnView.getHostname(), InfoOnView.getMyId());
        EventType eventType;
        if(firstSpawn){
            eventType = EventType.REQUEST_INITIAL_SPAWN_CARDS;
            System.out.println("Before start your first turn, you have to spawn!");
        }
        else{
            eventType = EventType.REQUEST_SPAWN_CARDS;
        }
        notifyEvent(infoRequest, eventType);

        InfoOnView.getMyPowerUps().getPowerUps().addAll(drawCardsInfo.getDrawnPowerUps());

        choosePowerupToDiscard(drawCardsInfo.getDrawnPowerUps());
        StateManager.triggerNextState(idleState);

    }

    /**
     * Get the information sent from the server and store it or immediately use it, relatively to the specific situation.
     * @param event
     */
    @Override
    public void update(Event event) {


        EventType command = event.getCommand();
        String jsonMessage = event.getMessageInJsonFormat();

        if(command == EventType.RESPONSE_REQUEST_INITIAL_SPAWN_CARDS || command == EventType.RESPONSE_REQUEST_SPAWN_CARDS){
            /*
                if this is a first spawn but has not been triggered by the view,
                set firstSpawn to true, to remember this is a first spawn and restart
                correctly in case of error
             */
            if(!firstSpawn && command == EventType.RESPONSE_REQUEST_INITIAL_SPAWN_CARDS){
                firstSpawn = true;
            }
            drawCardsInfo = new Gson().fromJson(jsonMessage, DrawCardsInfoResponse.class);
        }

        //if first spawn is true, after your successful spawn, reset firstSpawn to false
        if(firstSpawn && command == EventType.MSG_POWERUP_DISCARDED_TO_SPAWN){
            MessagePowerUpsDiscarded messagePowerUpsDiscarded = new Gson().fromJson(jsonMessage, MessagePowerUpsDiscarded.class);
            int idPlayerJustSpawned = messagePowerUpsDiscarded.getIdPlayer();
            if(idPlayerJustSpawned == InfoOnView.getMyId()){
                firstSpawn = false;
            }
        }

    }

    /**
     * Make the user choose the powerup to discard to determine where to spawn and send answer to the server.
     * @param powerUps
     */
    void choosePowerupToDiscard(List<PowerUpLM> powerUps){
        String userInput;
        System.out.println("Choose one of the powerups on your hand to discard: the color of " +
                "that powerup will determine the spawn point in which you will spawn.");
        List<String> options = new ArrayList<>();
        for(int i=0; i < powerUps.size(); i++){
            int option = i+1;
            options.add(Integer.toString(option));
            System.out.println(option + ": " + powerUps.get(i).getName() + " (" +
                    ToolsView.ammoTypeToString(powerUps.get(i).getGainAmmoColor()) + ")");
        }
        ToolsView.printGeneralOptions();
        System.out.print(GeneralInfo.ASK_INPUT);
        userInput = ToolsView.readUserChoice(options, true);
        if(userInput != null){
            //notify answer to server
            int idPowerup = powerUps.get(Integer.parseInt(userInput) - 1).getIdPowerUp();
            SpawnChoiceRequest spawnChoiceRequest =
                    new SpawnChoiceRequest(InfoOnView.getHostname(), InfoOnView.getMyId(), idPowerup);
            if(firstSpawn) {
                notifyEvent(spawnChoiceRequest, EventType.REQUEST_INITIAL_SPAWN);
                firstSpawn = false;
            }
            else
                notifyEvent(spawnChoiceRequest, EventType.REQUEST_SPAWN);
        }
        else{//timer expired
            firstSpawn = false; //reset firstSpawn to false (in case it was true)
        }
    }

    /**
     * Trigger first spawn
     */
    void triggerFirstSpawn(){
        firstSpawn = true;
        StateManager.triggerNextState(this);
    }

}
