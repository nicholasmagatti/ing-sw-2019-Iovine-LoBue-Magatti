package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.LightModel.WeaponLM;
import it.polimi.ProgettoIngSW2019.common.Message.toController.InfoRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toController.ReloadChoiceRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.MessageEnemyWeaponReloaded;
import it.polimi.ProgettoIngSW2019.common.Message.toView.MessageMyWeaponReloaded;
import it.polimi.ProgettoIngSW2019.common.Message.toView.PayAmmoList;
import it.polimi.ProgettoIngSW2019.common.Message.toView.WeaponsCanReloadResponse;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.Observer;
import it.polimi.ProgettoIngSW2019.custom_exception.IllegalAttributeException;
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
public class ReloadController extends Controller implements Observer<Event> {
    private WeaponCard weaponToReload;
    private Player ownerPlayer;
    private List<WeaponCard> weaponsCanReload = new ArrayList<>();
    private List<AmmoType> reloadCost = new ArrayList<>();



    /**
     * constructor
     * @param turnManager   turnManager
     * @param idConverter   idConverter
     * @param virtualView   virtualView
     */
    public ReloadController(TurnManager turnManager, IdConverter idConverter, VirtualView virtualView, CreateJson createJson, IdPlayersCreateList idPlayersCreateList) {
        super(turnManager, idConverter, virtualView, createJson, idPlayersCreateList);
    }



    /**
     * receive the message from the view
     * check type event
     * do actions based by type event
     * @param event     event sent
     */
    public void update(Event event) {
        if(event.getCommand().equals(EventType.REQUEST_WEAPONS_CAN_RELOAD)) {
            fromView(event.getMessageInJsonFormat());
        }

        if(event.getCommand().equals(EventType.REQUEST_RELOAD)) {
            reloadWeapon(event.getMessageInJsonFormat());
        }
    }


    /**
     * extract the info from the message to generate the player
     * @param messageJson   message from view
     */
    public void fromView(String messageJson) {
        //extract the json message in class with his data
        InfoRequest infoRequest = new Gson().fromJson(messageJson, InfoRequest.class);

        //is the turn of the player who wants to reload?
        if(infoRequest.getIdPlayer() != getTurnManager().getCurrentPlayer().getIdPlayer())
            throw new  IllegalAttributeException("It is not Player: " + infoRequest.getIdPlayer() + " turn");

        if(getTurnManager().getActionsLeft() != 0)
            throw new IllegalAttributeException("It is not the time for the player to reload");

        //extract player from the model with the idPlayer sent from the view
        ownerPlayer = getIdConverter().getPlayerById(infoRequest.getIdPlayer());

        reloadInfo();
    }


    /**
     * search all the cards can be reloaded and return to the view a list with the weapons can be reloaded
     */
    private void reloadInfo() {
        //set weapons list can be reloaded based by the number of the ammo of the player
        setListWeaponsCanReload();

        if(!weaponsCanReload.isEmpty()) {
            List<WeaponLM> weaponsCanReloadLM = getCreateJson().createWeaponsListLM(weaponsCanReload);
            //TODO: USO PAYAMMOCONTROLLER
            /*
            String reloadInfoString = new Gson().toJson(new WeaponsCanReloadResponse(ownerPlayer.getIdPlayer(), weaponsCanReloadLM));
            sendInfo(EventType.RESPONSE_REQUEST_WEAPONS_CAN_RELOAD, reloadInfoString, getIdPlayersCreateList().addOneIdPlayers(ownerPlayer));
             */
        }
        else {
            List<WeaponLM> weaponsCanReloadLM = new ArrayList<>();
            List<PayAmmoList> payAmmoLists = new ArrayList<>();
            String reloadInfoString = new Gson().toJson(new WeaponsCanReloadResponse(ownerPlayer.getIdPlayer(), weaponsCanReloadLM, payAmmoLists));
            sendInfo(EventType.RESPONSE_REQUEST_WEAPONS_CAN_RELOAD, reloadInfoString, getIdPlayersCreateList().addOneIdPlayers(ownerPlayer));
        }
    }



    /**
     * creates the list with the weapons can be reloaded
     */
    private void setListWeaponsCanReload() {
        if(!ownerPlayer.getUnloadedWeapons().isEmpty()) {

            for (WeaponCard weaponCard : ownerPlayer.getUnloadedWeapons()) {
                reloadCost = weaponCard.getreloadCost();

                if (ownerPlayer.hasEnoughAmmo(reloadCost))
                    weaponsCanReload.add(weaponCard);
            }
        }
    }



    /**
     * open the message and reload the weapon received
     * send the new player with the attributes modified to the view
     * @param messageJson   message json from the event generated by view
     */
    private void reloadWeapon(String messageJson) {
        ReloadChoiceRequest reloadChoice = new Gson().fromJson(messageJson, ReloadChoiceRequest.class);

        if(reloadChoice.getIdPlayer() != getTurnManager().getCurrentPlayer().getIdPlayer())
            throw new  IllegalAttributeException("It is not Player: " + reloadChoice.getIdPlayer() + " turn");

        if(getTurnManager().getActionsLeft() != 0)
            throw new IllegalAttributeException("It is not the time for the player to reload");

        ownerPlayer = getIdConverter().getPlayerById(reloadChoice.getIdPlayer());
        weaponToReload = getIdConverter().getUnloadedWeaponById(ownerPlayer.getIdPlayer(), reloadChoice.getIdWeaponToReload());

        if(weaponToReload == null)
            throw new IllegalAttributeException("WeaponToReload cannot be null");

        if(!ownerPlayer.getUnloadedWeapons().contains(weaponToReload))
            throw new IllegalAttributeException("The player: " + ownerPlayer.getCharaName() + " with id: " + ownerPlayer.getIdPlayer() + " doesn't have that powerUp");


        reloadCost = weaponToReload.getreloadCost();

        //TODO:crea problemi
        //another check if the player can pay the reload cost
        if(ownerPlayer.hasEnoughAmmo(reloadCost))
            ownerPlayer.reload(weaponToReload);
        else
            throw new IllegalAttributeException("The player: " + ownerPlayer.getCharaName() + "with id: " + ownerPlayer.getIdPlayer() + "cannot pay the reload cost");


        //send message to all enemy players with reload info
        String messageEnemyWeaponReloadedJson = createMessageEnemyWeaponReloadedJson();
        sendInfo(EventType.MSG_ENEMY_RELOAD_WEAPON, messageEnemyWeaponReloadedJson, getIdPlayersCreateList().addAllExceptPlayer(ownerPlayer));

        //send to the player the new loaded weapons
        String messageMyWeaponReloadedJson = createMessageMyWeaponReloaded();
        sendInfo(EventType.MSG_MY_RELOAD_WEAPON, messageMyWeaponReloadedJson, getIdPlayersCreateList().addOneIdPlayers(ownerPlayer));

        reloadInfo();
    }


    /**
     * creates the message for the enemies with reload info
     * @return  message json for enemies
     */
    public String createMessageEnemyWeaponReloadedJson() {
        MessageEnemyWeaponReloaded message = new MessageEnemyWeaponReloaded(ownerPlayer.getIdPlayer(), ownerPlayer.getCharaName(), weaponToReload.getIdCard(), weaponToReload.getName(), getCreateJson().createPlayerLM(ownerPlayer));
        return new Gson().toJson(message);
    }


    /**
     * creates the message for the player with the reload info
     * @return  message json for the player
     */
    public String createMessageMyWeaponReloaded() {
        MessageMyWeaponReloaded message = new MessageMyWeaponReloaded(ownerPlayer.getIdPlayer(), ownerPlayer.getCharaName(), weaponToReload.getIdCard(), weaponToReload.getName(), getCreateJson().createPlayerLM(ownerPlayer), getCreateJson().createMyLoadedWeaponsListLM(ownerPlayer));
        return new Gson().toJson(message);
    }
}