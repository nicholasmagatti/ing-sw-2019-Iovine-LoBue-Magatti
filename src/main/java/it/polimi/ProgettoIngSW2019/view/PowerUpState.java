package it.polimi.ProgettoIngSW2019.view;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.LightModel.PowerUpLM;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author Nicholas Magatti
 */
public class PowerUpState extends State {

    private PowerUpLM powerup = null;

    private ActionState actionState;

    PowerUpState(ActionState actionState){
        this.actionState = actionState;
    }

    void setPowerupToUse(PowerUpLM powerup) {
        this.powerup = powerup;
    }

    @Override
    void startState() {
        if(powerup == null){
            throw new Error("Powerup is null: you should have set the powerup to use before trigger the powerUpState.");
        }

        switch(powerup.getName()){
            case GeneralInfo.NEWTON:
                //TODO
                break;

            case GeneralInfo.TARGETING_SCOPE:
                //TODO
                break;

            case GeneralInfo.TELEPORTER:
                //TODO
                break;

            case GeneralInfo.TAGBACK_GRENADE:
                //TODO
                break;

                default:
                    throw new RuntimeException("The name of the powerup is " + powerup.getName() +
                            " and does not correspond to any of the names available.");

        }
    }

    @Override
    public void update(Event event) {

        EventType command = event.getCommand();
        String jsonMessage = event.getMessageInJsonFormat();

        //TODO: ricevi messaggio per tag qualcos grenade che inserisce le info negli attributi e fa triggerNextState(this)

        //TODO: analogo per targeting scope

    }

    /**
     * Assuming the user has already given consent to the use of one powerup, choose powerup to use between a non-empty list
     * @param powerups - non-empty list of powerups
     */
    void choosePowerupAndTriggerPowerupState(List<PowerUpLM> powerups){
        PowerUpLM chosenPwUp;
        String userInput;
        if(powerups.isEmpty()){
            throw new RuntimeException("This method should not be called to 'choose' between a 'list' of one powerup.");
        }
        if(powerups.size() > 1) {
            List<String> acceptableInputs = new ArrayList<>();
            System.out.println("Choose the powerup to use:");
            for (int i = 0; i < powerups.size(); i++) {
                PowerUpLM p = powerups.get(i);
                System.out.println((i + 1) + ": " + p.getName() + "(" +
                        ToolsView.ammoTypeToString(p.getGainAmmoColor()) + ")");
                acceptableInputs.add(Integer.toString(i + 1));
            }

            ToolsView.printGeneralOptions();
            System.out.print(GeneralInfo.ASK_INPUT);

            userInput = ToolsView.readUserChoice(acceptableInputs, true);

            if (userInput != null) {
                int posInList = Integer.parseInt(userInput) - 1;
                chosenPwUp = powerups.get(posInList);
                setPowerupToUse(chosenPwUp);
                StateManager.triggerNextState(this);
            }
        }
        else{//if there is only one powerup to use, the user has already given consent to use it, so don't ask anything
            setPowerupToUse(powerups.get(0));
            StateManager.triggerNextState(this);
        }
    }

    /**
     * Ask the user if he/she wants to use one of the powerups of the presented list and
     * return null if the timer expired, otherwise return the user's answer (yes or no).
     * The exact strings for the answer (yes or no) are accessible with GeneralInfo.YES_COMMAND and GeneralInfo.NO_COMMAND.
     * @param usablePowerups - non-empty list of powerups that the player can you in the presented situation
     * @return null if the timer expired, otherwise return the user's answer (yes or no)
     */
    String askUsePowerupsAndReturnAnswer(List<PowerUpLM> usablePowerups){
        if(usablePowerups.isEmpty()){
            throw new RuntimeException("This method should not be called with an empty list of powerups.");
        }
        ToolsView.printListOfPowerups(usablePowerups);
        System.out.println(GeneralInfo.YES_COMMAND + ": use powerup");
        System.out.println(GeneralInfo.NO_COMMAND + ": don't");
        ToolsView.printGeneralOptions();
        System.out.print(GeneralInfo.ASK_INPUT);
        List<String> acceptableInputs = new ArrayList<>();
        acceptableInputs.add(GeneralInfo.YES_COMMAND);
        acceptableInputs.add(GeneralInfo.NO_COMMAND);
        return ToolsView.readUserChoice(acceptableInputs, true);
    }
}
