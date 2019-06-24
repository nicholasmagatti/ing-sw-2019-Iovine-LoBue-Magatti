package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.Message.toController.InfoRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toController.ReloadChoiceRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.MessageWeaponPay;
import it.polimi.ProgettoIngSW2019.common.Message.toView.PayAmmoList;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;


import java.util.ArrayList;
import java.util.List;


/**
 * Class ReloadController
 * check reload
 * reload method
 * @author Priscilla Lo Bue
 */
public class ReloadController extends Controller {
    private PayAmmoController payAmmoController;
    private WeaponCard weaponToReload;
    private Player ownerPlayer;
    private List<WeaponCard> weaponsCanReload = new ArrayList<>();



    /**
     * constructor
     * @param turnManager   turnManager
     * @param virtualView   virtualView
     */
    public ReloadController(TurnManager turnManager, VirtualView virtualView, IdConverter idConverter, CreateJson createJson, HostNameCreateList hostNameCreateList, PayAmmoController payAmmoController) {
        super(turnManager, virtualView, idConverter, createJson, hostNameCreateList);
        this.payAmmoController = payAmmoController;
    }



    /**
     * receive the message from the view
     * check type event
     * do actions based by type event
     * @param event     event sent
     */
    public void update(Event event) {
        if(event.getCommand().equals(EventType.REQUEST_WEAPONS_CAN_RELOAD)) {
            checkInfoFromView(event.getMessageInJsonFormat());
        }

        if(event.getCommand().equals(EventType.REQUEST_RELOAD)) {
            checkReloadFromView(event.getMessageInJsonFormat());
        }
    }


    /**
     * extract the info from the message to generate the player
     * @param messageJson   message from view
     */
    public void checkInfoFromView(String messageJson) {
        //extract the json message in class with his data
        InfoRequest infoRequest = new Gson().fromJson(messageJson, InfoRequest.class);

        ownerPlayer = convertPlayer(infoRequest.getIdPlayer(), infoRequest.getHostNamePlayer());

        if(ownerPlayer != null) {
            if(checkHostNameCorrect(ownerPlayer, infoRequest.getHostNamePlayer())) {
                if (checkCurrentPlayer(ownerPlayer) && (checkNoActionLeft(ownerPlayer))) {
                    reloadInfo();
                }
            }
        }
    }


    /**
     * search all the cards can be reloaded and return to the view a list with the weapons can be reloaded
     */
    private void reloadInfo() {
        //set weapons list can be reloaded based by the number of the ammo of the player
        setListWeaponsCanReload();

        if(!weaponsCanReload.isEmpty()) {
            List<PayAmmoList> payAmmoLists = new ArrayList<>();

            for(WeaponCard weaponCard: weaponsCanReload) {
                List<AmmoType> ammoCost = weaponCard.getreloadCost();
                payAmmoLists.add(payAmmoController.ammoToPay(ownerPlayer, weaponCard, ammoCost));
            }

            String reloadInfoString = getCreateJson().createWeaponsToPayJson(ownerPlayer, weaponsCanReload, payAmmoLists);
            sendInfo(EventType.RESPONSE_REQUEST_WEAPONS_CAN_RELOAD, reloadInfoString, getHostNameCreateList().addOneHostName(ownerPlayer));
        }
        else {
            List<WeaponCard> weaponsCanReloadEmpty = new ArrayList<>();
            List<PayAmmoList> payAmmoListsEmpty = new ArrayList<>();
            String reloadInfoString = getCreateJson().createWeaponsToPayJson(ownerPlayer, weaponsCanReloadEmpty, payAmmoListsEmpty);
            sendInfo(EventType.RESPONSE_REQUEST_WEAPONS_CAN_RELOAD, reloadInfoString, getHostNameCreateList().addOneHostName(ownerPlayer));
        }
    }



    /**
     * creates the list with the weapons can be reloaded
     */
    private void setListWeaponsCanReload() {
        //remove all the elements
        if(!weaponsCanReload.isEmpty())
            weaponsCanReload.clear();

        if(!ownerPlayer.getUnloadedWeapons().isEmpty()) {
            for (WeaponCard weaponCard : ownerPlayer.getUnloadedWeapons()) {
                if (ownerPlayer.hasEnoughAmmo(weaponCard.getreloadCost()))
                    weaponsCanReload.add(weaponCard);
            }
        }
    }


    /**
     * check if reload info from view are correct
     * @param messageJson       json message
     */
    public void checkReloadFromView(String messageJson) {
        ReloadChoiceRequest reloadChoice = new Gson().fromJson(messageJson, ReloadChoiceRequest.class);

        if(checkIdCorrect(ownerPlayer, reloadChoice.getIdPlayer(), reloadChoice.getHostNamePlayer()) && (checkHostNameCorrect(ownerPlayer, reloadChoice.getHostNamePlayer()))) {
            weaponToReload = convertWeapon(ownerPlayer, reloadChoice.getIdWeaponToReload(), true);

            if (weaponToReload != null) {
                if (checkContainsWeapon(ownerPlayer, weaponToReload, true)) {
                    if (checkHasEnoughAmmo(ownerPlayer, weaponToReload.getreloadCost())) {

                        int[] ammoToPay = reloadChoice.getAmmoToDiscard();
                        List<Integer> powerUpToDiscard = reloadChoice.getIdPowerUpToDiscard();
                        List<PowerUp> powerUps = new ArrayList<>();

                        if(powerUpToDiscard == null)
                            throw new NullPointerException("powerUpToDiscard cannot be null");

                        if(!powerUpToDiscard.isEmpty()) {
                            for (int i: powerUpToDiscard) {
                                PowerUp powerUp = convertPowerUp(ownerPlayer, i);

                                if(powerUp == null)
                                    return;
                                else
                                    powerUps.add(powerUp);
                            }
                        }

                        if(payAmmoController.checkAmmoToPayFromView(weaponToReload.getreloadCost(), powerUps, ammoToPay))
                            reloadWeapon(ammoToPay, powerUps);
                        else {
                            String messageError ="ERROR: hai sbagliato input di pagamento.";
                            sendInfo(EventType.ERROR, messageError, getHostNameCreateList().addOneHostName(ownerPlayer));
                        }
                    }
                }
            }
        }
    }


    /**
     * reload
     * discard powerUp
     * send powerUp discarded
     * send the new player with the attributes modified to the view
     */
    private void reloadWeapon(int[] ammoToPayInt, List<PowerUp> powerUpsToDiscard) {

        if(powerUpsToDiscard == null)
            throw new NullPointerException("powerUpToDiscard list cannot be null");

        List<AmmoType> ammoToPay = convertAmmoToPay(ammoToPayInt);

        ownerPlayer.pay(ammoToPay, powerUpsToDiscard);

        if(!powerUpsToDiscard.isEmpty()) {
            String messagePowerUpDiscarded = getCreateJson().createPowerUpsListLMJson(powerUpsToDiscard);
            sendInfo(EventType.MSG_POWERUPS_DISCARDED_AS_AMMO, messagePowerUpDiscarded, getHostNameCreateList().addAllHostName());
            String myPowerUpsJson = getCreateJson().createMyPowerUpsLMJson(ownerPlayer);
            sendInfo(EventType.UPDATE_MY_POWERUPS, myPowerUpsJson, getHostNameCreateList().addOneHostName(ownerPlayer));
        }

        //send message to all enemy players with reload info
        String messageEnemyWeaponReloadedJson = createMessageWeaponReloadedJson();
        sendInfo(EventType.MSG_ALL_RELOAD_WEAPON, messageEnemyWeaponReloadedJson, getHostNameCreateList().addAllHostName());

        String updatePlayerLMJson = getCreateJson().createPlayerLMJson(ownerPlayer);
        sendInfo(EventType.UPDATE_PLAYER_INFO, updatePlayerLMJson, getHostNameCreateList().addAllHostName());

        //send to the player the new loaded weapons
        String updateMyLoadedWeapons = getCreateJson().createMyLoadedWeaponsListLMJson(ownerPlayer);
        sendInfo(EventType.UPDATE_MY_LOADED_WEAPONS, updateMyLoadedWeapons, getHostNameCreateList().addOneHostName(ownerPlayer));

        reloadInfo();
    }


    /**
     * creates the message for the enemies with reload info
     * @return  message json for enemies
     */
    public String createMessageWeaponReloadedJson() {
        MessageWeaponPay message = new MessageWeaponPay(ownerPlayer.getIdPlayer(), ownerPlayer.getCharaName(), weaponToReload.getIdCard(), weaponToReload.getName());
        return new Gson().toJson(message);
    }



    /**
     * converts ammo to pay int to list ammoType
     * @param ammoToPayInt      list ammo to pay int format
     * @return                  list of ammoType
     */
    public List<AmmoType> convertAmmoToPay(int[] ammoToPayInt) {
        List<AmmoType> ammoToPay = new ArrayList<>();

        for(int i = 0; i < ammoToPayInt.length; i++) {

            if(ammoToPayInt[i] > 0) {
                if(AmmoType.intFromAmmoType(AmmoType.RED) == i) {
                    for (int nRed = 0; nRed < ammoToPayInt[i]; nRed++) {
                        ammoToPay.add(AmmoType.RED);
                    }
                }

                if(AmmoType.intFromAmmoType(AmmoType.BLUE) == i) {
                    for (int nBlue = 0; nBlue < ammoToPayInt[i]; nBlue++) {
                        ammoToPay.add(AmmoType.BLUE);
                    }
                }

                if(AmmoType.intFromAmmoType(AmmoType.YELLOW) == i) {
                    for (int nYellow = 0; nYellow < ammoToPayInt[i]; nYellow++) {
                        ammoToPay.add(AmmoType.YELLOW);
                    }
                }
            }
        }
        return ammoToPay;
    }
}