package it.polimi.ProgettoIngSW2019.view;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.Message.toView.MessageActionLeft;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.*;
import java.util.ArrayList;
import java.util.List;

/**
 * State that manages the actions in a player's turn and also the use of
 * those powerups whose use is available before/after an action.
 * @author Nicholas Magatti
 */
public class ActionState extends State {

    private MessageActionLeft infoStart;

    private boolean moveGrabShootLinked = false;

    private ShootState shootState;
    private GrabState grabState;
    private MoveState moveState;
    private PowerUpState powerUpState;
    private ReloadState reloadState;
    private IdleState idleState;
    private boolean timeExpiredFromOtherState = false;

    private  int actionsLeft;


    /**
     * Constructor
     * @param reloadState
     */
    public ActionState(ReloadState reloadState, IdleState idleState) {

        this.reloadState = reloadState;
        this.idleState = idleState;
    }

    /**
     * Used after the creation of the parameters to link them to this state
     * @param moveState
     * @param grabState
     * @param shootState
     */
    public void linkToMoveGrabShootPwUp(MoveState moveState, GrabState grabState,
                                           ShootState shootState, PowerUpState powerUpState) {
        this.moveState = moveState;
        this.grabState = grabState;
        this.shootState = shootState;
        this.powerUpState = powerUpState;
        moveGrabShootLinked = true;
    }

    /**
     * Enters in this state when triggered and manage all the possible interactions.
     */
    @Override
    public void startState() {

        if (!moveGrabShootLinked) {
            throw new RuntimeException("The attributes moveState, grabState and ShootState have not been assigned.");
        }
        InfoOnView.printEverythingVisible();

        String userAnswer;

        /*
        //wait the update of n actions left and usable powerups
        while(infoStart == null)
            ToolsView.waitServerResponse();
        */
        actionsLeft = infoStart.getnActionsLeft();
        if (actionsLeft > 0) {
            String actionOrActions;
            if (actionsLeft > 1) {
                actionOrActions = "actions";
            } else {
                actionOrActions = "action";
            }
            System.out.println("You have " + actionsLeft + " " + actionOrActions + " available for this turn.");
        } else { //no actions left
            System.out.println("You don't have any action left for this turn.");
        }
        if (!infoStart.getPowerUpsCanUse().isEmpty()) {
            if(actionsLeft > 0 ) {
                System.out.print("But first: ");
            }
            userAnswer = powerUpState.askUsePowerup(infoStart.getPowerUpsCanUse());
            if (userAnswer != null) {
                if (userAnswer.equals(GeneralInfo.YES_COMMAND)) {
                    powerUpState.triggerPowerupState(infoStart.getPowerUpsCanUse());
                }
                else if (userAnswer.equals(GeneralInfo.NO_COMMAND)) {
                    proceedWithActionOrReload();
                }
            }
            else return;
        } else {
            proceedWithActionOrReload();
        }

        if(!timeExpiredFromOtherState) {
            //reset infoStart
            //infoStart = null;
            StateManager.triggerNextState(this);
        }
    }

    /**
     * Get the information sent from the server and store it or immediately use it, relatively to the specific situation.
     * @param event
     */
    @Override
    public void update(Event event) {

        EventType command = event.getCommand();
        String jsonMessage = event.getMessageInJsonFormat();

        if (command == EventType.MSG_MY_N_ACTION_LEFT &&
                !StateManager.getCurrentState().equals(idleState)) {
            infoStart = new Gson().fromJson(jsonMessage, MessageActionLeft.class);
        }
    }

    /**
     * Store the information about the possibilities for the next action (including the possibility to use powerups)
     * @param infoStart
     */
    void setInfoStart(MessageActionLeft infoStart){
        this.infoStart = infoStart;
    }

    /**
     * Set time expired from the outside to stop the loop in this state after the expiration
     * of the timer in a state called from this (action state).
     */
    void setTimeExipred(){
        timeExpiredFromOtherState = true;
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

    /**
     * Make the user choose the specific action an proceed with it.
     */
    private void chooseAction() {
        String userAnswer;
        List<String> acceptableInputs = new ArrayList<>();
        System.out.println("Choose one of these actions: ");
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
