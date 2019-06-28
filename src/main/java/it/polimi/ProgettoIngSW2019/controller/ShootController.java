package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import com.sun.javafx.scene.control.skin.IntegerFieldSkin;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.LightModel.PowerUpLM;
import it.polimi.ProgettoIngSW2019.common.Message.toController.InfoRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toController.ShootChoiceRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.EnemyInfo;
import it.polimi.ProgettoIngSW2019.common.Message.toView.ShootPowerUpInfo;
import it.polimi.ProgettoIngSW2019.common.Message.toView.WeaponInfo;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.enums.WeaponEffectType;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.custom_exception.EnemySizeLimitExceededException;
import it.polimi.ProgettoIngSW2019.custom_exception.IllegalAttributeException;
import it.polimi.ProgettoIngSW2019.custom_exception.IllegalIdException;
import it.polimi.ProgettoIngSW2019.custom_exception.NotPartOfBoardException;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;

import javax.naming.SizeLimitExceededException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class ShootController
 *
 * @author: Luca Iovine
 */
public class ShootController extends Controller{
    private Gson gson = new Gson();
    private DistanceDictionary distance;
    private Player weaponUser;
    private WeaponCard weaponChosen;
    private List<Player> enemyChosenList;
    private Square positionToMove;
    private String somethingWentWrong = "Ops, qualcosa è andato storto!";
    private String wrongChoiceErr;

    public ShootController(TurnManager turnManager, IdConverter idConverter, VirtualView virtualView, CreateJson createJson, HostNameCreateList hostNameCreateList, DistanceDictionary distance) {
        super(turnManager, virtualView, idConverter, createJson, hostNameCreateList);
        this.distance = distance;
        enemyChosenList = new ArrayList<>();
    }

    @Override
    public void update(Event event) {
        resetParam();
        switch(event.getCommand()){
           case REQUEST_WEAPON_INFO:
               InfoRequest infoRequest = gson.fromJson(event.getMessageInJsonFormat(), InfoRequest.class);
               weaponUser = convertPlayer(infoRequest.getIdPlayer(), infoRequest.getHostNamePlayer());

               if(weaponUser != null){
                   if(checkCurrentPlayer(weaponUser))
                       sendWeaponInfo();
               }
               break;
           case REQUEST_SHOOT:
               ShootChoiceRequest shootChoice = gson.fromJson(event.getMessageInJsonFormat(), ShootChoiceRequest.class);
               weaponUser = convertPlayer(shootChoice.getIdPlayer(), shootChoice.getHostNamePlayer());

               if(weaponUser != null){
                   if(checkCurrentPlayer(weaponUser)){
                       weaponChosen = convertWeapon(weaponUser, shootChoice.getIdWeaponUsed(), false);
                       if(checkContainsWeapon(weaponUser, weaponChosen, false)) {
                           enemyChosenList = convertEnemyListFromView(weaponUser, shootChoice.getEnemyChosenListId());

                           if (!enemyChosenList.isEmpty()) {
                               if(shootChoice.getPositionChosen() != null)
                                   positionToMove = convertMovementFromView(weaponUser, shootChoice.getPositionChosen());

                               try {
                                   if (!weaponChosen.checkBaseEffectParameterValidity(weaponUser, enemyChosenList)) {
                                       wrongChoiceErr = "I nemici scelti non sono validi.\n" +
                                               "Rifare la selezione.\n";
                                       sendInfo(EventType.ERROR, wrongChoiceErr, getHostNameCreateList().addOneHostName(weaponUser));

                                   } else if (!weaponChosen.checkBaseEffectMovementPositionValidity(positionToMove, enemyChosenList)) {
                                       wrongChoiceErr = "Lo spostamento scelto non è valido.\n" +
                                               "Rifare la selezione.\n";
                                       sendInfo(EventType.ERROR, wrongChoiceErr, getHostNameCreateList().addOneHostName(weaponUser));
                                   } else {
                                       activateEffect();
                                       //TODO: move due to effect
                                       sendTargetingScopeInfo();
                                       sendTagbackGranedeInfo(enemyChosenList);

                                       sendInfo(EventType.UPDATE_MY_LOADED_WEAPONS, getCreateJson().createMyLoadedWeaponsListLMJson(weaponUser), getHostNameCreateList().addOneHostName(weaponUser));
                                       sendInfo(EventType.UPDATE_MAP, getCreateJson().createMapLMJson(), getHostNameCreateList().addAllHostName());
                                       sendInfo(EventType.UPDATE_PLAYER_INFO, getCreateJson().createPlayerLMJson(weaponUser), getHostNameCreateList().addAllHostName());
                                       for(Player enemy: enemyChosenList){
                                           sendInfo(EventType.UPDATE_PLAYER_INFO, getCreateJson().createPlayerLMJson(enemy), getHostNameCreateList().addAllHostName());
                                       }

                                       sendInfo(EventType.MSG_BEFORE_ENEMY_ACTION_OR_RELOAD, "", getHostNameCreateList().addAllExceptOneHostName(weaponUser));
                                       msgActionLeft(weaponUser);
                                   }
                               } catch (EnemySizeLimitExceededException e) {
                                   wrongChoiceErr = "Troppi nemici scelti\n" +
                                           "Rifare la selezione.\n";
                                   sendInfo(EventType.ERROR, wrongChoiceErr, getHostNameCreateList().addOneHostName(weaponUser));
                               }
                           }
                       }
                   }
               }
               break;
       }
    }

    /**
     * Send the weapon info for each weapon loaded of the player.
     * In the list of EnemyInfo it has all the enemy information:
     * - enemy id
     * - enemy username
     * - enemy position (coordinates (row, column))
     * - where to move the enemy if the option to move it is aviable
     *
     * The boolean value represent if the weapon card allow to move your target
     * The WeaponEffectType allow to know the different kind of weapon in the game
     *
     * @author: Luca Iovine
     */
    private void sendWeaponInfo(){
        List<WeaponInfo> weaponInfoList = new ArrayList<>();
        boolean hasToMove;
        WeaponEffectType effectType;
        int weaponId;
        String weaponName;
        int nrOfTargetHittable;

        for(WeaponCard card: weaponUser.getLoadedWeapons()){
            List<EnemyInfo> enemyVisible = generateEnemyInfoList(card);
            if(!enemyVisible.isEmpty()) {
                hasToMove = card.hasToMoveInBaseEffect();
                effectType = card.getBaseEffectType();
                weaponId = card.getIdCard();
                weaponName = card.getName();
                nrOfTargetHittable = card.getNrOfPlayerHittable();

                weaponInfoList.add(new WeaponInfo(enemyVisible, hasToMove, effectType, weaponId, weaponName, nrOfTargetHittable));
            }
        }

        String jsonObj = gson.toJson(weaponInfoList);
        sendInfo(EventType.RESPONSE_REQUEST_WEAPON_INFO, jsonObj, getHostNameCreateList().addOneHostName(weaponUser));
    }

    /**
     * Activate the base effect of the weapon chosen by the player
     *
     * @throws EnemySizeLimitExceededException
     */
    private void activateEffect() throws EnemySizeLimitExceededException{
        weaponChosen.activateBaseEff(weaponUser, enemyChosenList);
    }

    /**
     * Generates a list of enemy to send back to client.
     * Every EnemyInfo in the list contains all the information needed to hit the enemy correctly.
     *
     * @param card is the weapon card from where the enemy list is calculated
     * @return list of enemy info
     */
    private List<EnemyInfo> generateEnemyInfoList(WeaponCard card){
        List<EnemyInfo> enemyInfoList = new ArrayList<>();
        List<Player> enemyList = card.getEnemyList(weaponUser);

        for(Player enemy: enemyList){
            List<int[]> movementList = new ArrayList<>();
            for(Square movementPos: card.getMovementList(weaponUser, enemy)){
                movementList.add(movementPos.getCoordinates(getTurnManager().getGameTable().getMap()));
            }
            int[] enemyPosition = enemy.getPosition().getCoordinates(getTurnManager().getGameTable().getMap());

            enemyInfoList.add(new EnemyInfo(enemy.getIdPlayer(), enemy.getCharaName(), enemyPosition, movementList));
        }

        return enemyInfoList;
    }

    /**
     * Notify the weapon user if it has got the targeting scope
     *
     * @author: Luca Iovine
     */
    private void sendTargetingScopeInfo(){
        List<PowerUpLM> targetingScopeList = new ArrayList<>();
        List<EnemyInfo> enemyChosenLM = new ArrayList<>();
        List<PowerUpLM> powerUpForBuy = new ArrayList<>();
        int[] ammoForBuy = new int[3];

        for(PowerUp power: weaponUser.getPowerUps()){
            if(power.getName().equals("TARGETING_SCOPE"))
                targetingScopeList.add(getCreateJson().createPowerUpLM(power));
            else
                //TODO: in realtà anche gli "altri" targeting scope possono essere utilizzati per pagare
                powerUpForBuy.add(getCreateJson().createPowerUpLM(power));
        }

        for(Player p: enemyChosenList){
            if(!p.isPlayerDown())
                enemyChosenLM.add(new EnemyInfo(p.getIdPlayer(), p.getCharaName(), p.getPosition().getCoordinates(getTurnManager().getGameTable().getMap()), null));
        }

        ammoForBuy[GeneralInfo.RED_ROOM_ID] = weaponUser.getRedAmmo();
        ammoForBuy[GeneralInfo.BLUE_ROOM_ID] = weaponUser.getBlueAmmo();
        ammoForBuy[GeneralInfo.YELLOW_ROOM_ID] = weaponUser.getYellowAmmo();

        if(!targetingScopeList.isEmpty() && !enemyChosenLM.isEmpty()){
            ShootPowerUpInfo shootPowerUpInfo = new ShootPowerUpInfo(targetingScopeList, enemyChosenLM, powerUpForBuy, ammoForBuy);
            sendInfo(EventType.CAN_USE_TARGETING_SCOPE, serialize(shootPowerUpInfo), Arrays.asList(weaponUser.getHostname()));
        }
    }

    /**
     * Notify the enemy hitted if they have got the tagback grenade
     *
     * @author: Luca Iovine
     */
    private void sendTagbackGranedeInfo(List<Player> enemyChosen){
        //TODO: sistemare il metodo perché deve inviare le tagback solo ai giocatori che vedono chi li
        // ha colpiti e deve inviare la lista di tutte le tagback che ha in mano
        List<String> hostnameEnemyThatHasGotTagback = new ArrayList<>();

        for(Player enemy: enemyChosen) {
            for (PowerUp power : enemy.getPowerUps()) {
                if (power.getName().equals("TAGBACK_GRENADE")) {
                    hostnameEnemyThatHasGotTagback.add(enemy.getHostname());
                }
            }
        }

        if(!hostnameEnemyThatHasGotTagback.isEmpty()){
            sendInfo(EventType.CAN_USE_TAGBACK, "", hostnameEnemyThatHasGotTagback);
        }
    }

    /**
     * Convert the enemy list id into Player of the game
     *
     * @param player the weapon user, in order to communicate him whenever something went wrong
     * @param idEnemyList id of the enmy to convert
     * @return the list of enemy as "Player"
     * @author: Luca Iovine
     */
    public List<Player> convertEnemyListFromView(Player player, List<Integer> idEnemyList){
        List<Player> enemylist = new ArrayList<>();

        for(int idEnemy: idEnemyList) {
            if(idEnemy < -1)
                throw new IllegalAttributeException("id player (enemy) cannot be negative");

            try {
                enemylist.add(getIdConverter().getPlayerById(idEnemy));
            }catch(IllegalIdException e){
                sendInfo(EventType.ERROR, somethingWentWrong, getHostNameCreateList().addOneHostName(player));
                enemylist.clear();
                break;
            }
        }

        return enemylist;
    }

    /**
     * Convert coordinates in a Square position
     *
     * @param player the weapon user, in order to communicate him whenever something went wrong
     * @param position coordinates of the square
     * @return the position as "Square"
     * @author: Luca Iovine
     */
    public Square convertMovementFromView(Player player, int[] position){
        Square newPostion = null;

        try{
            newPostion = getIdConverter().getSquareByCoordinates(position);
        }catch(NotPartOfBoardException e){
            sendInfo(EventType.ERROR, somethingWentWrong, getHostNameCreateList().addOneHostName(player));
        }

        return newPostion;
    }

    /**
     * Reset the param before resolving new request
     *
     * @author: Luca Iovine
     */
    private void resetParam(){
        weaponUser = null;
        weaponChosen = null;
        enemyChosenList = new ArrayList<>();
        positionToMove = null;
    }

    /**
     * To serialize information of the event
     *
     * @param objToSerialize object that needs to be serialized
     * @return object serialized
     * @author: Luca Iovine
     */
    //NOT TO BE TESTED
    private String serialize(Object objToSerialize){
        Gson gsonReader = new Gson();
        String serializedObj = gsonReader.toJson(objToSerialize, objToSerialize.getClass());

        return serializedObj;
    }
}
