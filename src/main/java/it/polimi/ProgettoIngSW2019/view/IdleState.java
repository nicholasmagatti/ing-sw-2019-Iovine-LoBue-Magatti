package it.polimi.ProgettoIngSW2019.view;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.LightModel.SquareLM;
import it.polimi.ProgettoIngSW2019.common.Message.toView.MessageActionLeft;
import it.polimi.ProgettoIngSW2019.common.Message.toView.SetupInfo;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.TypeAdapterSquareLM;

import java.sql.Statement;

public class IdleState extends State{
    boolean firstSpawn = false;
    boolean startAction = false;
    boolean revive = false;
    ActionState actionState;
    SpawnState spawnState;
    SetupGameState setupGameState;
    SetupInfo setupInfo;
    boolean goInSetup;
    String username;
    String hostname;

    @Override
    void startState() {
        while(!startAction && !firstSpawn && !goInSetup){
            ToolsView.waitServerResponse();
        }

        if(startAction) {
            startAction = false;
            actionState.startState();
        }
        if(firstSpawn) {
            firstSpawn = false;
            spawnState.triggerFirstSpawn();
        }
        if(revive){
            revive = false;
            StateManager.triggerNextState(spawnState);
        }
        if(goInSetup){
            goInSetup = false;
            StateManager.triggerNextState(setupGameState);
        }
    }

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
    }

    public void linkState(ActionState actionState, SpawnState spawnState, SetupGameState setupGameState){
        this.actionState = actionState;
        this.spawnState = spawnState;
        this.setupGameState = setupGameState;
    }

    public void setInfoBeforeSetup(String username, String hostname){
        this.username = username;
        this.hostname = hostname;
    }

    public SetupInfo deserialize(String json){
        Gson gsonReader = new GsonBuilder()
                .registerTypeAdapter(SquareLM.class, new TypeAdapterSquareLM())
                .create();

        return gsonReader.fromJson(json, SetupInfo.class);
    }
}
