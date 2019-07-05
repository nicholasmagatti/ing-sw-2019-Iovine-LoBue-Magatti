package it.polimi.ProgettoIngSW2019.view;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.LightModel.PowerUpLM;
import it.polimi.ProgettoIngSW2019.common.Message.toController.*;
import it.polimi.ProgettoIngSW2019.common.Message.toView.EnemyInfo;
import it.polimi.ProgettoIngSW2019.common.Message.toView.MessagePowerUpsDiscarded;
import it.polimi.ProgettoIngSW2019.common.Message.toView.NewtonInfoResponse;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.Message.toView.ShootPowerUpInfo;
import it.polimi.ProgettoIngSW2019.common.utilities.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Ask the user to
 * @author Nicholas Magatti
 */
public class PowerUpState extends State {
    private IdleState idleState;
    private ShootPowerUpInfo shootPowerUpInfo = null;
    private int idPowerUp = -1;
    private NewtonInfoResponse newtonInfoResponse = null;
    private boolean useNewton = false;

    /**
     * Constructor
     * @param idleState
     */
    public PowerUpState(IdleState idleState){
        this.idleState = idleState;
    }

    /**
     * Get the information sent from the server and store it or immediately use it, relatively to the specific situation.
     */
    @Override
    void startState() {

        if(shootPowerUpInfo == null){
            throw new NullPointerException("The info about the powerup to use etc should have " +
                                            "been inserted before calling this method.");
        }

        List<PowerUpLM> usablePowerups = shootPowerUpInfo.getPowerUpUsableList();
        /*ask the user to choose the powerup to use (or
          , if there is only one, just use that one without asking
         */
        PowerUpLM chosenPwUp = choosePowerup(usablePowerups);
        if(chosenPwUp != null) { //timer not expired
            idPowerUp = chosenPwUp.getIdPowerUp();
            //if NEWTON or TELEPORTER
            if (forBeforeOrAfterAction(usablePowerups)) {

                switch (chosenPwUp.getName()) {
                    case GeneralInfo.NEWTON:
                        //keep ONLY the chosen powerup in usablePowerUps: remove the others
                        PowerUpLM chosenPow = null;
                        for(PowerUpLM pw : usablePowerups) {
                            if(pw.getIdPowerUp() == idPowerUp) {
                                chosenPow = pw;
                            }
                        }
                        if(chosenPow == null)
                            throw new Error();

                        usablePowerups.clear();
                        usablePowerups.add(chosenPow);

                        newtonInfoRequest();
                        newton();
                        break;
                    case GeneralInfo.TELEPORTER:
                        teleporter();
                        break;
                    default:
                        throw new Error("The chosen weapon has the wrong name: " + chosenPwUp.getName());
                }

            }
            else{//if GRENADE or TARGETING SCOPE
                if (everyPowerupNameIs(usablePowerups, GeneralInfo.TAGBACK_GRENADE)) {
                    grenade();
                }
                else{
                    if (everyPowerupNameIs(usablePowerups, GeneralInfo.TARGETING_SCOPE)) {
                        targetingScope();
                    }
                    else{
                        throw new Error("Something went wrong.");
                    }
                }
            }
        }
        else{ //reset shootPowerUpInfo to null
            shootPowerUpInfo = null;
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


        if(command == EventType.RESPONSE_NEWTON_INFO){
            newtonInfoResponse = new Gson().fromJson(jsonMessage, NewtonInfoResponse.class);
            if(newtonInfoResponse.getEnemyInfoMovement().isEmpty()){
                System.out.println("You cannot use this powerup because you have no targets available for its effect.");
            }
            else {
            /*
            I insert the info here in the attribute of the class to avoid the situation in which the programmer
            tries to access the info about newtonInfoResponse.getEnemyInfoMovement() from the shootPowerUpInfo
            and gets an empty list instead of the correct one. But inserting it in the shootPowerUpInfo this
            possible mistake is prevented.
             */
                shootPowerUpInfo = new ShootPowerUpInfo(
                        shootPowerUpInfo.getPowerUpUsableList(), newtonInfoResponse.getEnemyInfoMovement(),
                        shootPowerUpInfo.getPowerUpAsPayment(), shootPowerUpInfo.getAmmoAsPayment());
                idPowerUp = shootPowerUpInfo.getPowerUpUsableList().get(0).getIdPowerUp();
                /*I removed this from here to avoid deadlock
                newton();*/
            }
        }

        //reset inf in shootPowerUpInfo after the use of a powerup by THIS user has worked successfully
        if(command == EventType.MSG_USE_POWERUP){
            MessagePowerUpsDiscarded messagePowerUpsDiscarded = new Gson().fromJson(jsonMessage, MessagePowerUpsDiscarded.class);
            if(messagePowerUpsDiscarded.getIdPlayer() == InfoOnView.getMyId()){
                shootPowerUpInfo = null;
            }
        }
    }

    /**
     * Ask the player if he/she wants to use a powerup and proceed if the answer is yes
     * @param jsonMessage
     * @deprecated
     */
    private void askUsePowerUpAndTriggerStateIfYes(String jsonMessage){
        ShootPowerUpInfo shootPowerUpInfo = new Gson().fromJson(jsonMessage, ShootPowerUpInfo.class);
        String userAnswer = askUsePowerup(shootPowerUpInfo.getPowerUpUsableList());

        if(userAnswer != null && userAnswer.equals(GeneralInfo.YES_COMMAND)){
            triggerPowerupState(shootPowerUpInfo);
        }
    }

    /**
     * Ask the user if he/she wants to use one of the powerups of the presented list and
     * return null if the timer expired, otherwise return the user's answer (yes or no).
     * The exact strings for the answer (yes or no) are accessible with GeneralInfo.YES_COMMAND and GeneralInfo.NO_COMMAND.
     * @param usablePowerups - non-empty list of powerups that the player can you in the presented situation
     * @return null if the timer expired, otherwise return the user's answer (yes or no)
     */
    String askUsePowerup(List<PowerUpLM> usablePowerups){
        if(usablePowerups.isEmpty()){
            throw new RuntimeException("This method should not be called with an empty list of powerups.");
        }
        System.out.println("Do you want to use one of these powerups?");
        ToolsView.printListOfPowerups(usablePowerups);
        System.out.print("\n");
        System.out.println(GeneralInfo.YES_COMMAND + ": use powerup");
        System.out.println(GeneralInfo.NO_COMMAND + ": don't");
        ToolsView.printGeneralOptions();
        System.out.print(GeneralInfo.ASK_INPUT);
        List<String> acceptableInputs = new ArrayList<>();
        acceptableInputs.add(GeneralInfo.YES_COMMAND);
        acceptableInputs.add(GeneralInfo.NO_COMMAND);
        return ToolsView.readUserChoice(acceptableInputs, true);
    }

    /**
     * Assuming the user has already given consent to the use of one powerup, trigger PowerUpState.
     * Warning: use this method when the only information to pass to the PowerUpState is the list of powerups.
     * @param usablePowerups - non-empty list of usable powerups
     */
    void triggerPowerupState(List<PowerUpLM> usablePowerups){
        shootPowerUpInfo = new ShootPowerUpInfo(usablePowerups, new ArrayList<>(), new ArrayList<>(), new int[0]);
        StateManager.triggerNextState(this);
    }

    /**
     * Assuming the user has already given consent to the use of one powerup, trigger PowerUpState.
     * @param shootPowerUpInfo
     */
    void triggerPowerupState(ShootPowerUpInfo shootPowerUpInfo){
        this.shootPowerUpInfo = shootPowerUpInfo;
        StateManager.triggerNextState(this);
    }

    /**
     * Assuming the user has already given consent to the use of one powerup, choose powerup to use between a non-empty list
     * and if there are more than two powerups to choose, return the user's choice if the timer did not expire
     * first, return null otherwise. If there is only one powerup, don't ask anything and return that powerup.
     * @param powerups - non-empty list of powerups
     * @return the powerup in the list if there is only one, the one chosen from the player if
     * there are at least two and the timer did not expire first, null otherwise
     */
    private PowerUpLM choosePowerup(List<PowerUpLM> powerups){
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
                return chosenPwUp;
            }
            else{//time expired
                return null;
            }
        }
        else{//if there is only one powerup to use, the user has already given consent to use it, so don't ask anything
            chosenPwUp = powerups.get(0);
            return chosenPwUp;
        }
    }

    /**
     * Return true if the received list of usable powerups is composed (only) by the
     * powerups you can use before or after an action during your turn.
     * @param usablePowerups
     * @return true if the received list of usable powerups is composed (only) by the
     * powerups you can use before or after an action during your turn; false otherwise.
     */
    private boolean forBeforeOrAfterAction(List<PowerUpLM> usablePowerups) {
        for (PowerUpLM pwUp : usablePowerups) {
            if (!pwUp.getName().equals(GeneralInfo.TELEPORTER) && !pwUp.getName().equals(GeneralInfo.NEWTON)){
                return false;
            }
        }
        return true;
    }

    /**
     * Reurn true if every powerup has the name requested in the parameters, return false otherwise.
     * @param powerups
     * @param name
     * @return true if every powerup has the name requested in the parameters, return false otherwise.
     */
    private boolean everyPowerupNameIs(List<PowerUpLM> powerups, String name){
        for(PowerUpLM pwUp : powerups){
            if(!pwUp.getName().equals(name)){
                return false;
            }
        }
        return true;
    }

    /**
     * Indicate the specific card to use and send this info to the server
     */
    private void newtonInfoRequest(){
        //set id
        //notify server
        PowerUpChoiceRequest powerUpChoiceRequest =
                new PowerUpChoiceRequest(InfoOnView.getHostname(), InfoOnView.getMyId(), idPowerUp);
        notifyEvent(powerUpChoiceRequest, EventType.REQUEST_NEWTON_INFO);
    }

    /**
     *  Ask the user to set what is necessary for the use of the Newton powerup and
     *  if the timer does not expire first, send the choice to the server
     */
    private void newton(){
        Integer idTarget = ToolsView.readTargetChoice(shootPowerUpInfo.getEnemy());
        if(idTarget != null){//time not expired
            System.out.println("Where do you want to move the target?");

            List<int[]> possibleDestinations = new ArrayList<>();

            for(EnemyInfo enemyInfo : shootPowerUpInfo.getEnemy()){
                possibleDestinations.addAll(enemyInfo.getMovement());
            }

            int[] chosenDestination = ToolsView.chooseDestination(possibleDestinations);
            if(chosenDestination != null){
                //notify server
                NewtonRequest newtonRequest = new NewtonRequest(InfoOnView.getHostname(),
                        InfoOnView.getMyId(), idPowerUp,idTarget, chosenDestination);
                notifyEvent(newtonRequest, EventType.NEWTON);
            }
            else{//reset shootPowerUpInfo (time expired)
                shootPowerUpInfo = null;
            }
        }
        else{ //reset shootPowerUpInfo (time expired)
            shootPowerUpInfo = null;
        }
    }

    /**
     *  Ask the user to set what is necessary for the user of the Teleporter and
     *  if the timer does not expire first, send the choice to the server
     */
    private void teleporter(){
        System.out.println("Where do you want to teleport yourself?");
        int[] chosenDestination = ToolsView.chooseDestination(InfoOnView.allNonNullSquarePositions());
        if(chosenDestination != null){ //time not expired
            //notify server
            TeleporterRequest teleporterRequest = new TeleporterRequest(InfoOnView.getHostname(),
                    InfoOnView.getMyId(), idPowerUp, chosenDestination);
            notifyEvent(teleporterRequest, EventType.TELEPORTER);
        }
        else{ //reset shootPowerUpInfo
            shootPowerUpInfo = null;
        }
    }

    /**
     *  Ask the user to set what is necessary for the user of the tagback grenade and
     *  if the timer does not expire first, send the choice to the server
     */
    private void grenade(){
        Integer idTarget = ToolsView.readTargetChoice(shootPowerUpInfo.getEnemy());
        if(idTarget != null){ //time not expired
            //send choice to server
            TagBackGrenadeRequest tagBackGrenadeRequest =
                    new TagBackGrenadeRequest(InfoOnView.getHostname(), InfoOnView.getMyId(), idPowerUp, idTarget);
            notifyEvent(tagBackGrenadeRequest, EventType.TAGBACK_GRENADE);
            StateManager.triggerNextState(idleState);
        }
        else{//reset shootPowerUpInfo to null
            shootPowerUpInfo = null;
        }
    }

    /**
     *  Ask the user to set what is necessary for the user of the Targeting Scope and
     *  if the timer does not expire first, send the choice to the server
     */
    private void targetingScope(){

        AmmoType ammoToSpend = null; //it will be inserted if an ammo unit is chosen to pay
        int idPowerUpToSpend = -1; //it will be inserted if a powerup is chosen to pay
        //choose how to pay
        PaymentChoiceInfo paymentChoiceInfo = ToolsView.
                payOneAmmo(shootPowerUpInfo.getAmmoAsPayment(), shootPowerUpInfo.getPowerUpAsPayment());
        if(paymentChoiceInfo != null){//time not expired
            //insert the chosen ammoToSpend/idPowerUpToSpend
            if(paymentChoiceInfo.getIdPowerUpToDiscard().isEmpty()){ //insert ammo
                for(int i=0; i < paymentChoiceInfo.getAmmoToDiscard().length; i++){
                    if(paymentChoiceInfo.getAmmoToDiscard()[i] == 1){
                        for(AmmoType ammoType : AmmoType.values()){
                            if(AmmoType.intFromAmmoType(ammoType) == i) {
                                ammoToSpend = ammoType;
                            }
                        }
                    }
                }
            }
            else{// insert powerup
                idPowerUpToSpend = paymentChoiceInfo.getIdPowerUpToDiscard().get(0);
            }
            //choose the target: if there is only one option, don't even ask
            Integer idTarget = ToolsView.readTargetChoice(shootPowerUpInfo.getEnemy());
            if (idTarget != null) { //time not expired
                //notify choice to server
                TargetingScopeRequest targetingScopeRequest = new
                        TargetingScopeRequest(InfoOnView.getHostname(), InfoOnView.getMyId(), idPowerUp,
                        idTarget, ammoToSpend, idPowerUpToSpend);
                notifyEvent(targetingScopeRequest, EventType.TARGETING_SCOPE);
            }
            else{//reset shootPowerUpInfo to null if the time has expired
                shootPowerUpInfo = null;
            }
        }
        else{ //reset shootPowerUpInfo to null if the time has expired
            shootPowerUpInfo = null;
        }
    }


}
