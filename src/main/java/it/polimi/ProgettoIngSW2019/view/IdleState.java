package it.polimi.ProgettoIngSW2019.view;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.Message.toView.MessageActionLeft;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;

import java.sql.Statement;

public class IdleState extends State{
    boolean firstSpawn = false;
    boolean startAction = false;
    boolean revive = false;
    ActionState actionState;
    SpawnState spawnState;

    @Override
    void startState() {
        while(!startAction && !firstSpawn){
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
    }

    @Override
    public void update(Event event) {
        EventType command = event.getCommand();
        if(event.getCommand().equals(EventType.MSG_MY_N_ACTION_LEFT) && StateManager.getCurrentState().equals(this)) {
            actionState.setInfoStart(new Gson().fromJson(event.getMessageInJsonFormat(), MessageActionLeft.class));
            startAction = true;
        }

        //trigger first spawn
        if(event.getCommand().equals(EventType.MSG_FIRST_TURN_PLAYER)){
            System.out.println("Before start your first turn, you have to spawn!");
            firstSpawn = true;
        }

        //trigger standard spawn
        if(command == EventType.MSG_PLAYER_SPAWN){
            System.out.println("You died so now you will spawn again.");
            revive = true;
        }
    }

    public void linkState(ActionState actionState, SpawnState spawnState){
        this.actionState = actionState;
        this.spawnState = spawnState;
    }
}
