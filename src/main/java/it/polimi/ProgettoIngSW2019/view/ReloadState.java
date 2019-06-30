package it.polimi.ProgettoIngSW2019.view;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.LightModel.WeaponLM;
import it.polimi.ProgettoIngSW2019.common.Message.toController.InfoRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toController.PaymentChoiceInfo;
import it.polimi.ProgettoIngSW2019.common.Message.toController.ReloadChoiceRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.PayAmmoList;
import it.polimi.ProgettoIngSW2019.common.Message.toView.WeaponsCanPayResponse;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nicholas Magatti
 */
public class ReloadState extends State{

    private String input;

    /**
     * Send a message to the controller that requests the reloadable weapons for this user.
     */
    @Override
    public void startState(){
        //notify to controller
        InfoRequest infoRequest = new InfoRequest(InfoOnView.getHostname(), InfoOnView.getMyId());
        notifyEvent(infoRequest, EventType.REQUEST_WEAPONS_CAN_RELOAD);
    }

    @Override
    public void update(Event event){

        EventType command = event.getCommand();
        String jsonMessage = event.getMessageInJsonFormat();

        if(command == EventType.RESPONSE_REQUEST_WEAPONS_CAN_RELOAD){
            WeaponsCanPayResponse reloadInfo = new Gson().fromJson(jsonMessage, WeaponsCanPayResponse.class);
            chooseWeaponIfPossible(reloadInfo);
        }
    }

    private void chooseWeaponIfPossible(WeaponsCanPayResponse reloadInfo){

        if(reloadInfo.getWeaponsCanReload().isEmpty()){
            //end turn (there is nothing to reload)
            notifyEndTurn();
        }
        else {
            chooseWeaponToReload(reloadInfo);
        }
    }

    private void chooseWeaponToReload(WeaponsCanPayResponse reloadInfo){
        int chosenOption; //typed by user
        int idWeapon; // chosen weapon to send to view
        List<WeaponLM>weapons = reloadInfo.getWeaponsCanReload();
        //options
        System.out.println("Which weapon do you want to reload? Choose an option:");
        for(int i=0; i < weapons.size(); i++) {
            System.out.println(i + 1 + ": " + weapons.get(i).getName() +
                    ", cost: " + ToolsView.costToString(reloadInfo.getListPaymentReload().get(i).getAmmoCost()));
        }
        System.out.println(GeneralInfo.NO_COMMAND + ": don't reload");
        ToolsView.printGeneralOptions();
        List<String>acceptableInputs = new ArrayList<>();
        acceptableInputs.add(GeneralInfo.NO_COMMAND);
        for(int i=0; i <= weapons.size(); i++){
            acceptableInputs.add(Integer.toString(i));
        }
        input = ToolsView.readUserChoice(acceptableInputs, true);
        if(input != null){ //time NOT expired
            if(input.equals(GeneralInfo.NO_COMMAND)) {
                notifyEndTurn();
            }
            else {// pay and notify
                chosenOption = Integer.parseInt(input);
                idWeapon = weapons.get(chosenOption - 1).getIdWeapon(); //to send to server
                PayAmmoList payInfo = reloadInfo.getListPaymentReload().get(chosenOption - 1);
                if(payInfo.getIdWeapon() != idWeapon){
                    throw new Error("This ids should be equal. Something went wrong.");
                }
                /*TODO uncomment after Luca Iovine put askPayment in ToolsView
                PaymentChoiceInfo inputUserPayment = ToolsView.
                        askPayment(payInfo.getAmmoCost(), payInfo.getAmmoInAmmoBox(), payInfo.getAmmoInPowerUp());
                if(inputUserPayment != null){ //time not expired
                    //notify to server
                    ReloadChoiceRequest reloadChoiceRequest =
                            new ReloadChoiceRequest(InfoOnView.getHostname(), InfoOnView.getMyId(), idWeapon,
                                    inputUserPayment.getAmmoToDiscard(), inputUserPayment.getIdPowerUpToDiscard());

                    notifyEvent(reloadChoiceRequest, EventType.REQUEST_RELOAD);
                }*/
            }
        }
    }

    private void notifyEndTurn(){
        InfoRequest infoToSend = new InfoRequest(InfoOnView.getHostname(), InfoOnView.getMyId());
        notifyEvent(infoToSend, EventType.REQUEST_ENDTURN_INFO);
    }

}




