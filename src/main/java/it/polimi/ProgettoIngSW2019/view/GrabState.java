package it.polimi.ProgettoIngSW2019.view;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.LightModel.PowerUpLM;
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

public class GrabState extends State{
    Gson gsonReader;
    ActionState actionState;
    StringBuilder sb;
    String msg;

    List<WeaponLM> weaponToDiscardList;
    List<String> possibleChoice;

    /**
     * @author: Luca Iovine
     */
    public GrabState(ActionState actionState){
        this.actionState = actionState;
    }

    @Override
    void startState() {
        InfoRequest infoRequest = new InfoRequest(InfoOnView.getHostname(), InfoOnView.getMyId());
        notifyEvent(infoRequest, EventType.REQUEST_GRAB_INFO);
    }

    @Override
    public void update(Event event) {
        possibleChoice= new ArrayList<>();
        sb = new StringBuilder();
        if(event.getCommand().equals(EventType.RESPONSE_REQUEST_GRAB_INFO)){
            weaponToDiscardList = null;
            sendPositionToGrab(event.getMessageInJsonFormat());
        }else if(event.getCommand().equals(EventType.WEAPONS_CAN_BUY)){
            sendWeaponToBuy(event.getMessageInJsonFormat());
        }else if(event.getCommand().equals(EventType.DISCARD_WEAPON)){
            weaponToDiscardList = gsonReader.fromJson(event.getMessageInJsonFormat(), List.class);
        }
    }

    private void sendPositionToGrab(String jsonMessage){
        int i;
        int[] positionChosen;
        GrabInfoResponse grabInfo = gsonReader.fromJson(jsonMessage, GrabInfoResponse.class);

        msg = "Puoi scegliere una di queste posizioni dove spostarti e poi raccogliere ciò che c'è a terra: \n";
        sb.append(msg);

        for(i = 0; i < grabInfo.getCoordinatesSquareToGrab().size(); i++){
            possibleChoice.add(Integer.toString(i+1));
            msg = (i+1) + ": " + ToolsView.coordinatesForUser(grabInfo.getCoordinatesSquareToGrab().get(i)) + "\n";
            sb.append(msg);
        }

        msg = "Scegli un numero (da 1 a" + i + ") per indicare dove vuoi muoverti e raccogliere: ";
        sb.append(msg);
        System.out.print(sb);

        String userChoice = ToolsView.readUserChoice(possibleChoice, false);

        if(userChoice != null){
            positionChosen = grabInfo.getCoordinatesSquareToGrab().get(Integer.parseInt(userChoice) - 1);

            GrabChoiceRequest moveChoiceRequest = new GrabChoiceRequest(InfoOnView.getHostname(), InfoOnView.getMyId(), positionChosen);
            notifyEvent(moveChoiceRequest, EventType.REQUEST_GRAB);
        }
    }

    private void sendWeaponToBuy(String jsonMessage){
        int idWeaponChosen;
        int idWeaponToDiscard = -2;
        PaymentChoiceInfo payment = null;

        WeaponsCanPayResponse weaponInfo = gsonReader.fromJson(jsonMessage, WeaponsCanPayResponse.class);

        idWeaponChosen = askWeapon(weaponInfo);
        if(weaponToDiscardList != null)
            idWeaponToDiscard = askDiscardWeapon(weaponToDiscardList);

        //Cerco l'arma corrispondente
        for(PayAmmoList payAmmo: weaponInfo.getListPaymentReload()){
            if(payAmmo.getIdWeapon() == idWeaponChosen)
                payment = askPayment(payAmmo.getAmmoCost(), payAmmo.getAmmoInAmmoBox(), payAmmo.getAmmoInPowerUp());
                break;
        }
        if(idWeaponChosen != -1 && idWeaponToDiscard != - 1 && payment != null) {
            GrabWeaponChoiceRequest grabWeapon = new GrabWeaponChoiceRequest(InfoOnView.getHostname(), InfoOnView.getMyId(), idWeaponToDiscard, idWeaponChosen, payment.getAmmoToDiscard(), payment.getIdPowerUpToDiscard());
            notifyEvent(grabWeapon, EventType.REQUEST_GRAB_WEAPON);
        }
    }

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

    private int askWeapon(WeaponsCanPayResponse weaponInfo){
        int i;
        String cost;

        for (i = 0; i < weaponInfo.getWeaponsCanReload().size(); i++) {
            cost = getCost(weaponInfo.getWeaponsCanReload().get(i), weaponInfo.getListPaymentReload());
            possibleChoice.add(Integer.toString(i+1));
            msg = (i+1) + ": " + weaponInfo.getWeaponsCanReload().get(i).getName() + " Costo di ricarica: " + cost + "\n";
            sb.append(msg);
        }

        msg = "Scegli un numero (da 1 a" + i + ") per indicare dove vuoi muoverti e raccogliere: ";
        sb.append(msg);
        System.out.print(sb);

        String userChoice = ToolsView.readUserChoice(possibleChoice, false);

        if(userChoice != null)
            return weaponInfo.getWeaponsCanReload().get(Integer.parseInt(userChoice) - 1).getIdWeapon();
        else
            return -1;
    }

    private int askDiscardWeapon( List<WeaponLM> weaponToDiscardList){
        int i;
        int idWeaponToDiscard = -1;
        msg = "Hai troppe carte in mano, scegline una da scartare tra le seguenti: \n";
        sb.append(msg);

        for(i = 0; i < weaponToDiscardList.size(); i++){
            possibleChoice.add(Integer.toString(i+1));
            msg = (i+1) + ": " + weaponToDiscardList.get(i).getName() + "\n";
            sb.append(msg);
        }

        msg = "Scegli un numero (da 1 a" + i + ") per indicare dove vuoi muoverti e raccogliere: ";
        sb.append(msg);
        System.out.print(sb);

        String userChoice = ToolsView.readUserChoice(possibleChoice, false);

        if(userChoice != null){
            idWeaponToDiscard = weaponToDiscardList.get(Integer.parseInt(userChoice) - 1).getIdWeapon();
        }

        return idWeaponToDiscard;
    }

    private PaymentChoiceInfo askPayment(int[] costToPay, int[] ammoInAmmoBox, List<PowerUpLM> ammoInPowerUp){
        int i;
        boolean checkResult = true;
        final String red = "R";
        final String blue = "B";
        final String yellow = "Y";

        int[] ammoChosen = new int[ammoInAmmoBox.length];
        List<Integer> powerUpIdChosenList = new ArrayList<>();

        int[] tmpCostToPay = new int[costToPay.length];
        int[] tmpAmmoInAmmoBox = new int[ammoInAmmoBox.length];
        List<PowerUpLM> tmpAmmoInPowerUp = new ArrayList<>();

        StringBuilder paymentSB = new StringBuilder();
        List<String> responeForAmmo = new ArrayList<>();
        List<String> responseForPowerUp = new ArrayList<>();

        //Creazione liste temporanee per tenere traccia quantità "utilizzate" e del costo rimanente
        for(i = 0; i < costToPay.length; i++){
            tmpCostToPay[i] = costToPay[i];
        }

        for(i = 0; i < ammoInAmmoBox.length; i++){
            tmpAmmoInAmmoBox[i] = ammoInAmmoBox[i];
        }

        tmpAmmoInPowerUp.addAll(ammoInPowerUp);

        //Costruzione interazione utente
        msg = "Devi pagare delle munizioni per proseguire con l'azione. \n" +
                "Puoi utilizzare sia le munizioni che hai nella box delle ammo, che i power up.\n\n" +
                "Questo è il costo che devi pagare: ";
        paymentSB.append(msg);

        msg = ToolsView.costToString(costToPay) + "\n\n";
        paymentSB.append(msg);

        while(checkResult) {
            msg = "Queste sono le munizioni che puoi spendere.\n" +
                    "Ogni volta che selezionerai il colore corrispondente, verrà scalata di uno la quantità: \n";

            paymentSB.append(msg);

            if (tmpAmmoInAmmoBox[GeneralInfo.RED_ROOM_ID] > 0) {
                msg = red + ": Ammo rosse disponibili" + tmpAmmoInAmmoBox[GeneralInfo.RED_ROOM_ID] + "\n";
                paymentSB.append(msg);
                responeForAmmo.add(red);
            }
            if (tmpAmmoInAmmoBox[GeneralInfo.BLUE_ROOM_ID] > 0) {
                msg = blue + ": Ammo blu disponibili" + tmpAmmoInAmmoBox[GeneralInfo.BLUE_ROOM_ID] + "\n";
                paymentSB.append(msg);
                responeForAmmo.add(blue);
            }
            if (tmpAmmoInAmmoBox[GeneralInfo.YELLOW_ROOM_ID] > 0) {
                msg = yellow + ": Ammo Gialle disponibili" + tmpAmmoInAmmoBox[GeneralInfo.YELLOW_ROOM_ID] + "\n";
                paymentSB.append(msg);
                responeForAmmo.add(yellow);
            }

            for (i = 0; i < tmpAmmoInPowerUp.size(); i++) {
                msg = (i + 1) + ": " + tmpAmmoInPowerUp.get(i).getName() + " COLORE: " + tmpAmmoInPowerUp.get(i).getGainAmmoColor() + "\n";
                paymentSB.append(msg);
                responseForPowerUp.add(Integer.toString(i + 1));
            }

            if (!responeForAmmo.isEmpty()) {
                possibleChoice.addAll(responeForAmmo);
                msg = "Scegli una lettera R/B/Y oppure ";
                paymentSB.append(msg);
            }

            possibleChoice.addAll(responseForPowerUp);
            msg = "scegli un numero da 1 a " + i + ": ";
            paymentSB.append(msg);
            System.out.print(sb);

            String userChoice = ToolsView.readUserChoice(possibleChoice, false);

            if (userChoice != null) {
                if (responeForAmmo.contains(userChoice)) {
                    switch (userChoice) {
                        case red:
                            tmpCostToPay[GeneralInfo.RED_ROOM_ID]--;
                            tmpAmmoInAmmoBox[GeneralInfo.RED_ROOM_ID]--;
                            ammoChosen[GeneralInfo.RED_ROOM_ID]++;
                            break;
                        case blue:
                            tmpCostToPay[GeneralInfo.BLUE_ROOM_ID]--;
                            tmpAmmoInAmmoBox[GeneralInfo.BLUE_ROOM_ID]--;
                            ammoChosen[GeneralInfo.BLUE_ROOM_ID]++;
                            break;
                        case yellow:
                            tmpCostToPay[GeneralInfo.YELLOW_ROOM_ID]--;
                            tmpAmmoInAmmoBox[GeneralInfo.YELLOW_ROOM_ID]--;
                            ammoChosen[GeneralInfo.YELLOW_ROOM_ID]++;
                            break;
                    }
                    checkResult = checkIfNeedMore(tmpCostToPay);
                } else if (responseForPowerUp.contains(userChoice)) {
                    powerUpIdChosenList.add(tmpAmmoInPowerUp.get(Integer.parseInt(userChoice) - 1).getIdWeapon());
                    tmpAmmoInPowerUp.remove(Integer.parseInt(userChoice) - 1);
                }
            }
            else
                return null;
        }
        return new PaymentChoiceInfo(ammoChosen, powerUpIdChosenList);
    }

    private boolean checkIfNeedMore(int[] costToPayToCheck){
        if(costToPayToCheck[GeneralInfo.RED_ROOM_ID] > 0 ||
                costToPayToCheck[GeneralInfo.BLUE_ROOM_ID] > 0 ||
                costToPayToCheck[GeneralInfo.YELLOW_ROOM_ID] > 0)
            return true;
        else
            return false;
    }
}
