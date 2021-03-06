package it.polimi.ProgettoIngSW2019.view;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.LightModel.WeaponLM;
import it.polimi.ProgettoIngSW2019.common.Message.toController.GrabChoiceRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toController.GrabWeaponChoiceRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toController.InfoRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toController.PaymentChoiceInfo;
import it.polimi.ProgettoIngSW2019.common.Message.toView.GrabInfoResponse;
import it.polimi.ProgettoIngSW2019.common.Message.toView.PayAmmoList;
import it.polimi.ProgettoIngSW2019.common.Message.toView.WeaponsCanPayResponse;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * State of the view that manages the action of grabbing
 * @author Luca Iovine
 */
public class GrabState extends State{
    private Gson gsonReader = new Gson();
    private ActionState actionState;
    private StringBuilder sb;
    private String msg;

    private List<WeaponLM> weaponToDiscardList;
    private List<String> possibleChoice;
    private GrabInfoResponse grabInfo;
    private WeaponsCanPayResponse weaponInfo;
    boolean youAreInSpawningPoint = false;
    boolean youHaveToDiscardWeapon = false;
    private boolean stopGrabState = false;

    final String EXPLANATION_GRAB = ") to indicate where you want to move and grab: ";

    /**
     * Enters in this state when triggered and manage all the possible interactions.
     * @author: Luca Iovine
     */
    public GrabState(ActionState actionState){
        this.actionState = actionState;
    }

    /**
     * Enters in this state when triggered and manage all the possible interactions.
     */
    @Override
    void startState() {
        InfoRequest infoRequest = new InfoRequest(InfoOnView.getHostname(), InfoOnView.getMyId());
        notifyEvent(infoRequest, EventType.REQUEST_GRAB_INFO);

        sendPositionToGrab(grabInfo);

        if(youAreInSpawningPoint && !stopGrabState){
            sendWeaponToBuy(weaponInfo, weaponToDiscardList);
        }
    }

    /**
     * Get the information sent from the server and store it or immediately use it, relatively to the specific situation.
     * @param event
     */
    @Override
    public void update(Event event) {
        possibleChoice= new ArrayList<>();
        sb = new StringBuilder();
        if(event.getCommand().equals(EventType.RESPONSE_REQUEST_GRAB_INFO)){
            youAreInSpawningPoint = false;
            youHaveToDiscardWeapon = false;
            grabInfo = gsonReader.fromJson(event.getMessageInJsonFormat(), GrabInfoResponse.class);
        }else if(event.getCommand().equals(EventType.WEAPONS_CAN_BUY)){
            youAreInSpawningPoint = true;
            weaponInfo = gsonReader.fromJson(event.getMessageInJsonFormat(), WeaponsCanPayResponse.class);
        }else if(event.getCommand().equals(EventType.DISCARD_WEAPON)){
            youHaveToDiscardWeapon = true;
            weaponToDiscardList = gsonReader.fromJson(event.getMessageInJsonFormat(), List.class);
        }
    }

    /**
     * Notify position to grab o the server
     * @param grabInfo
     */
    private void sendPositionToGrab(GrabInfoResponse grabInfo){
        int i;
        int[] positionChosen;
        possibleChoice = new ArrayList<>();
        sb = new StringBuilder();

        msg = "You can choose one of this positions to move to and grab what's on that square\n";
        sb.append(msg);

        for(i = 0; i < grabInfo.getCoordinatesSquareToGrab().size(); i++){
            possibleChoice.add(Integer.toString(i+1));
            msg = (i+1) + ": " + ToolsView.coordinatesForUser(grabInfo.getCoordinatesSquareToGrab().get(i))[0] + ToolsView.coordinatesForUser(grabInfo.getCoordinatesSquareToGrab().get(i))[1]  + "\n";
            sb.append(msg);
        }

        msg = GeneralInfo.CHOOSE_OPTION + i + EXPLANATION_GRAB;
        sb.append(msg);
        System.out.print(sb);

        String userChoice = ToolsView.readUserChoice(possibleChoice, false);

        if(userChoice != null){
            positionChosen = grabInfo.getCoordinatesSquareToGrab().get(Integer.parseInt(userChoice) - 1);

            GrabChoiceRequest moveChoiceRequest = new GrabChoiceRequest(InfoOnView.getHostname(), InfoOnView.getMyId(), positionChosen);
            notifyEvent(moveChoiceRequest, EventType.REQUEST_GRAB);
        }
        else {
            actionState.setTimeExipred();
            stopGrabState = false;
        }
    }

    /**
     * Notify weapon to buy to the controller
     * @param weaponInfo
     * @param weaponToDiscardList
     */
    private void sendWeaponToBuy(WeaponsCanPayResponse weaponInfo, List<WeaponLM> weaponToDiscardList){
        int idWeaponChosen;
        int idWeaponToDiscard = -2;
        PaymentChoiceInfo payment = null;
        possibleChoice = new ArrayList<>();
        sb = new StringBuilder();


        idWeaponChosen = askWeapon(weaponInfo);
        //if time expired, stop here and notify that to action state
        if(idWeaponChosen == -1){
            actionState.setTimeExipred();
            return;
        }
        if(youHaveToDiscardWeapon)
            idWeaponToDiscard = askDiscardWeapon(weaponToDiscardList);
        //if time expired, stop here and notify that to action state
        if(idWeaponChosen == -1){
            actionState.setTimeExipred();
            return;
        }
        //Cerco l'arma corrispondente
        for(PayAmmoList payAmmo: weaponInfo.getListPaymentReload()){
            if(payAmmo.getIdWeapon() == idWeaponChosen) {
                payment = ToolsView.askPayment(payAmmo.getAmmoCost(), payAmmo.getAmmoInAmmoBox(), payAmmo.getAmmoInPowerUp());
                break;
            }
        }
        if(idWeaponChosen != -1 && idWeaponToDiscard != - 1 && payment != null) {
            GrabWeaponChoiceRequest grabWeapon = new GrabWeaponChoiceRequest(InfoOnView.getHostname(), InfoOnView.getMyId(), idWeaponToDiscard, idWeaponChosen, payment.getAmmoToDiscard(), payment.getIdPowerUpToDiscard());
            notifyEvent(grabWeapon, EventType.REQUEST_GRAB_WEAPON);
        }
        if(payment == null){
            actionState.setTimeExipred();
        }
    }

    /**
     * Get the cost to pay as a string understandable to the user
     * @param weapon
     * @param payAmmoLists
     * @return
     */
    private String getCost(WeaponLM weapon, List<PayAmmoList> payAmmoLists){
        String cost = "";
        for(PayAmmoList payment: payAmmoLists){
            if(payment.getIdWeapon() == weapon.getIdWeapon()){
                cost = ToolsView.costToString(payment.getAmmoCost());
                break;
            }
        }

        return cost;
    }

    /**
     * Ask the weapons to use
     * @param weaponInfo
     * @return
     */
    private int askWeapon(WeaponsCanPayResponse weaponInfo){
        int i;
        String cost;
        possibleChoice = new ArrayList<>();
        sb = new StringBuilder();

        for (i = 0; i < weaponInfo.getWeaponsCanReload().size(); i++) {
            cost = getCost(weaponInfo.getWeaponsCanReload().get(i), weaponInfo.getListPaymentReload());
            possibleChoice.add(Integer.toString(i+1));
            msg = (i+1) + ": " + weaponInfo.getWeaponsCanReload().get(i).getName() + " Buy cost: " + cost + "\n";
            sb.append(msg);
        }

        msg = GeneralInfo.CHOOSE_OPTION + i + EXPLANATION_GRAB;
        sb.append(msg);
        System.out.print(sb);

        String userChoice = ToolsView.readUserChoice(possibleChoice, false);

        if(userChoice != null)
            return weaponInfo.getWeaponsCanReload().get(Integer.parseInt(userChoice) - 1).getIdWeapon();
        else
            return -1;
    }

    /**
     * Ask the user to choose the weapon to discard to get the new one
     * @param weaponToDiscardList
     * @return
     */
    private int askDiscardWeapon( List<WeaponLM> weaponToDiscardList){
        int i;
        int idWeaponToDiscard = -1;
        possibleChoice = new ArrayList<>();
        sb = new StringBuilder();

        msg = "Too many cards on your hand! Choose one to discard between the following:\n";
        sb.append(msg);

        for(i = 0; i < weaponToDiscardList.size(); i++){
            possibleChoice.add(Integer.toString(i+1));
            msg = (i+1) + ": " + weaponToDiscardList.get(i).getName() + "\n";
            sb.append(msg);
        }

        msg = GeneralInfo.CHOOSE_OPTION + i + EXPLANATION_GRAB;
        sb.append(msg);
        System.out.print(sb);

        String userChoice = ToolsView.readUserChoice(possibleChoice, false);

        if(userChoice != null){
            idWeaponToDiscard = weaponToDiscardList.get(Integer.parseInt(userChoice) - 1).getIdWeapon();
        }

        return idWeaponToDiscard;
    }
}
