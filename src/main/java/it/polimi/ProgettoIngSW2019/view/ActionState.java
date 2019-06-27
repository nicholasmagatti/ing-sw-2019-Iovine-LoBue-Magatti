package it.polimi.ProgettoIngSW2019.view;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.LightModel.PowerUpLM;
import it.polimi.ProgettoIngSW2019.common.Message.toView.MessageActionLeft;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nicholas Magatti
 */
public class ActionState extends State {

    private MessageActionLeft infoStart;
    /**
     * @deprecated
     */
    private boolean moveGrabShootLinked = false;

    private ShootState shootState;
    private GrabState grabState;
    private MoveState moveState;
    private PowerUpState powerUpState;
    private ReloadState reloadState;


    /**
     * Constructor
     * @param moveState
     * @param grabState
     * @param shootState
     * @param powerUpState
     * @param reloadState
     */
    public ActionState(MoveState moveState, GrabState grabState, ShootState shootState,
                       PowerUpState powerUpState, ReloadState reloadState) {
        this.moveState = moveState;
        this.grabState = grabState;
        this.shootState = shootState;
        this.powerUpState = powerUpState;
        this.reloadState = reloadState;
    }

    /**
     * Used after the creation of the parameters to link them to this state.
     *
     * @param moveState
     * @param grabState
     * @param shootState
     * @deprecated
     */
    public void linkToMoveGrabShootPowerup(MoveState moveState, GrabState grabState,
                                           ShootState shootState, PowerUpState powerUpState) {
        this.moveState = moveState;
        this.grabState = grabState;
        this.shootState = shootState;
        this.powerUpState = powerUpState;
        moveGrabShootLinked = true;
    }

    @Override
    public void startState() {
        /*
        if (!moveGrabShootLinked) {
            throw new RuntimeException("The attributes moveState, grabState and ShootState have not been assigned.");
        }*/
        InfoOnView.printEverything();

        String userAnswer;
        int actionsLeft = infoStart.getnActionsLeft();
        if (actionsLeft > 0) {
            String actionOrActions;
            if (actionsLeft > 1) {
                actionOrActions = "actions";
            }
            else{
                actionOrActions = "action";
            }
            System.out.println("You have " + actionsLeft + " " + actionOrActions + " available for this turn.");
            if (!infoStart.getPowerUpsCanUse().isEmpty()) {
                System.out.println("Do you want to use one of these powerups before the next action?");
            }
        }
        else{
            System.out.println("You don't have any action left for this turn. You can only reload now.");
            if(!infoStart.getPowerUpsCanUse().isEmpty()) {
                System.out.println("But first, do you want to use one of these powerups?");
            }
        }
        if(!infoStart.getPowerUpsCanUse().isEmpty()){
            userAnswer = powerUpState.askUsePowerupsAndReturnAnswer(infoStart.getPowerUpsCanUse());
            if (userAnswer != null) {
                if (userAnswer.equals(GeneralInfo.YES_COMMAND)) {
                    powerUpState.choosePowerupAndTriggerPowerupState(infoStart.getPowerUpsCanUse());
                }
                if (userAnswer.equals(GeneralInfo.NO_COMMAND)) {
                    proceedWithActionOrReload();
                }
            }
        }
        else{
            proceedWithActionOrReload();
        }
    }

    @Override
    public void update(Event event) {

        EventType command = event.getCommand();
        String jsonMessage = event.getMessageInJsonFormat();

        if (command == EventType.MSG_MY_N_ACTION_LEFT) {
            infoStart = new Gson().fromJson(jsonMessage, MessageActionLeft.class);
            StateManager.triggerNextState(this);
        }
    }

    /**
     * Proceed with choosing the next action, or if not possible, go to Reload State.
     */
    private void proceedWithActionOrReload(){
        if(infoStart.getnActionsLeft() > 0) {
            chooseAction();
        }
        else {
            StateManager.triggerNextState(reloadState);
        }
    }

    private void chooseAction() {
        String userAnswer;
        List<String> acceptableInputs = new ArrayList<>();
        System.out.println("Choose one of this actions: ");
        System.out.println(GeneralInfo.MOVE_COMMAND + ": move your player");
        System.out.println(GeneralInfo.GRAB_COMMAND + ": grab ammo card or weapon from a square");
        System.out.println(GeneralInfo.SHOOT_COMMAND + ": use a weapon");
        ToolsView.printGeneralOptions();
        System.out.print(GeneralInfo.ASK_INPUT);
        acceptableInputs.add(GeneralInfo.MOVE_COMMAND);
        acceptableInputs.add(GeneralInfo.GRAB_COMMAND);
        acceptableInputs.add(GeneralInfo.SHOOT_COMMAND);
        userAnswer = ToolsView.readUserChoice(acceptableInputs, true);
        if (userAnswer != null) {
            switch (userAnswer) {
                case GeneralInfo.MOVE_COMMAND:
                    StateManager.triggerNextState(moveState);
                    break;
                case GeneralInfo.GRAB_COMMAND:
                    StateManager.triggerNextState(grabState);
                    break;
                case GeneralInfo.SHOOT_COMMAND:
                    StateManager.triggerNextState(shootState);
                    break;
                default:
                    throw new Error("If the command is not either the one for grab, shoot or move," +
                            "it should not even have got here (in this switch-case).");
            }
        }
    }



}
