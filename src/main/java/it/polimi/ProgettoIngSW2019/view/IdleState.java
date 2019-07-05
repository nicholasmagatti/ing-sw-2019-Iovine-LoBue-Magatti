package it.polimi.ProgettoIngSW2019.view;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.LightModel.SquareLM;
import it.polimi.ProgettoIngSW2019.common.Message.toView.MessageActionLeft;
import it.polimi.ProgettoIngSW2019.common.Message.toView.SetupInfo;
import it.polimi.ProgettoIngSW2019.common.Message.toView.ShootPowerUpInfo;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.common.utilities.TypeAdapterSquareLM;

import java.sql.Statement;

/**
 * Class that manages the situation of inactivity.
 */
public class IdleState extends State{
    private boolean canUseGrenade = false;
    private boolean firstSpawn = false;
    private boolean startAction = false;
    private boolean revive = false;
    private boolean goInSetup = false;
    private ActionState actionState;
    private SpawnState spawnState;
    private SetupGameState setupGameState;
    private PowerUpState powerUpState;
    private SetupInfo setupInfo;
    private ShootPowerUpInfo shootPowerUpInfo = null;
    private String username;
    private String hostname;

    /**
     * Enters in this state when triggered and manage all the possible interactions.
     */
    @Override
    void startState() {
        while(!startAction && !firstSpawn && !goInSetup && !canUseGrenade){
            ToolsView.waitServerResponse();
        }

        if(startAction) {
            startAction = false;
            actionState.startState();
        }
        else if(firstSpawn) {
            firstSpawn = false;
            spawnState.triggerFirstSpawn();
        }
        else if(revive){
            revive = false;
            StateManager.triggerNextState(spawnState);
        }
        else if(goInSetup){
            goInSetup = false;
            StateManager.triggerNextState(setupGameState);
        }
        else if(canUseGrenade){
            //reset the boolean
            canUseGrenade = false;
            String userAnswer = powerUpState.askUsePowerup(shootPowerUpInfo.getPowerUpUsableList());
            if (userAnswer != null) {
                if (userAnswer.equals(GeneralInfo.YES_COMMAND)) {
                    powerUpState.triggerPowerupState(shootPowerUpInfo);
                }
                else if (userAnswer.equals(GeneralInfo.NO_COMMAND)) {
                    StateManager.triggerNextState(this);
                }
            }
        }
    }

    /**
     * Get the information sent from the server and store it or immediately use it, relatively to the specific situation.
     * @param event
     */
    @Override
    public void update(Event event) {
        EventType command = event.getCommand();
        if(event.getCommand().equals(EventType.MSG_MY_N_ACTION_LEFT) && (StateManager.getCurrentState().equals(this) || StateManager.getCurrentState().equals(spawnState))) {
            actionState.setInfoStart(new Gson().fromJson(event.getMessageInJsonFormat(), MessageActionLeft.class));
            startAction = true;
        }

        //trigger first spawn
        if(event.getCommand().equals(EventType.MSG_FIRST_TURN_PLAYER)){
            InfoOnView.printEverythingVisible();
            System.out.println("Before start your first turn, you have to spawn!");
            firstSpawn = true;
        }

        //trigger standard spawn
        if(command == EventType.MSG_PLAYER_SPAWN){
            System.out.println("You died so now you will spawn again.");
            revive = true;
        }

        if (command == EventType.GO_IN_GAME_SETUP && StateManager.getCurrentState().equals(this)) {
            setupInfo = deserialize(event.getMessageInJsonFormat());
            setupGameState.setInfoBeforeStartGame(username, hostname, setupInfo.getMapLMList(), setupInfo.getUsername());
            goInSetup = true;
        }

        if(command == EventType.CAN_USE_TAGBACK){
            canUseGrenade = true;
            shootPowerUpInfo = new Gson().fromJson(event.getMessageInJsonFormat(), ShootPowerUpInfo.class);
        }
    }

    /**
     * Link the necessary states to communicate with after the creation of the class.
     * @param actionState
     * @param spawnState
     * @param setupGameState
     * @param powerUpState
     */
    public void linkState(ActionState actionState, SpawnState spawnState, SetupGameState setupGameState, PowerUpState powerUpState){
        this.actionState = actionState;
        this.spawnState = spawnState;
        this.setupGameState = setupGameState;
        this.powerUpState = powerUpState;
    }

    /**
     * Set the necessary info before the start of this state
     * @param username
     * @param hostname
     */
    public void setInfoBeforeSetup(String username, String hostname){
        this.username = username;
        this.hostname = hostname;
    }

    /**
     * Deserialize the squares for the light model.
     * @param json
     * @return the information for setup
     */
    public SetupInfo deserialize(String json){
        Gson gsonReader = new GsonBuilder()
                .registerTypeAdapter(SquareLM.class, new TypeAdapterSquareLM())
                .create();

        return gsonReader.fromJson(json, SetupInfo.class);
    }
}
