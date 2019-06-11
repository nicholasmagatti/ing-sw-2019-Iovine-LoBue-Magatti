package it.polimi.ProgettoIngSW2019.view;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.LightModel.WeaponLM;
import it.polimi.ProgettoIngSW2019.common.Message.toController.InfoRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.WeaponsCanReloadResponse;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nicholas Magatti
 */
public class ReloadState extends Observable<Event> implements IState, Observer<Event>{
    InfoOnView infoOnView;
    StateManager stateManager;
    SpawnState spawnState;
    private InputScanner inputScanner = new InputScanner();
    private boolean exit = false;
    Event eventToSend;

    /**
     * Send a message to the controller that requests the reloadable weapons for this user.
     * @param stateManager
     */
    @Override
    public void startState(StateManager stateManager){
        String jsonMsg = new Gson().toJson(new InfoRequest(infoOnView.getMyId()));
        eventToSend = new Event(EventType.REQUEST_WEAPONS_CAN_RELOAD, jsonMsg);
        notify(eventToSend);
    }

    private void errorAndRestart(String jsonMsg){
        String s = new Gson().fromJson(jsonMsg, String.class);
        System.out.println(s);
        //restart the state from the beginning
        startState(stateManager);
    }

    private void chooseWeaponToReload(String jsonMsg){
        int chosenOption; //typed by user
        int chosenWeapon; // to send to view
        WeaponsCanReloadResponse reloadInfo = new Gson().fromJson(jsonMsg, WeaponsCanReloadResponse.class);
        List<WeaponLM>weapons = reloadInfo.getWeaponsCanReload();
        if(weapons.isEmpty()){
            stateManager.triggerNextState(spawnState);
        }
        else {
            //TODO: make a general method for this IF POSSIBLE
            //options
            System.out.println("Which weapon do you want to reload? Choose an option:");
            for(int i=0; i < weapons.size(); i++) {
                System.out.println(i + 1 + " : " + weapons.get(i).getName() +
                        ", cost: " + Payment.costToString(reloadInfo.getListPaymentReload().get(i).getAmmoCost()));
            }
            System.out.println(GeneralInfo.NO + ": don't reload");
            System.out.println(GeneralInfo.EXIT + ": " + GeneralInfo.DESCRIPTION_EXIT);
            /*
            System.out.println("DETAILS WEAPONS: ");
            for(WeaponLM weaponLM : weapons){
                System.out.println("Name: " + weaponLM.getName());
                System.out.println("Description: " + weaponLM.getDescription());
            }*/
            //TODO: make a general method for this IF POSSIBLE
            List<String>acceptableInputs = new ArrayList<>();
            acceptableInputs.add(GeneralInfo.NO);
            acceptableInputs.add(GeneralInfo.EXIT);
            for(int i=0; i <= weapons.size(); i++){
                acceptableInputs.add(Integer.toString(i));
            }
            //TODO: make a general method for this IF POSSIBLE
            do {
                inputScanner.read();
                if(acceptableInputs.contains(inputScanner.getInputValue())){
                    exit = true;
                }

            }while(!exit);
            inputScanner.close();
            if(inputScanner.getInputValue().equals(GeneralInfo.NO)){
                stateManager.triggerNextState(spawnState);
            }
            if(inputScanner.getInputValue().equals(GeneralInfo.EXIT)){
                //TODO
            }
            chosenOption = Integer.parseInt(inputScanner.getInputValue());

        }
    }

    @Override
    public void update(Event message){

        EventType command = message.getCommand();
        String jsonMessage = message.getMessageInJsonFormat();

        if(command == EventType.ERROR){
            errorAndRestart(jsonMessage);
        }

        if(command == EventType.RESPONSE_REQUEST_WEAPONS_CAN_RELOAD){
            chooseWeaponToReload(jsonMessage);
        }

    }
}




