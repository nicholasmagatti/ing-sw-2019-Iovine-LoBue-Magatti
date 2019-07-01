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
import it.polimi.ProgettoIngSW2019.common.enums.AreaOfEffect;
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

                                   } else if (!weaponChosen.checkBaseEffectMovementPositionValidity(positionToMove,  weaponUser, enemyChosenList)) {
                                       wrongChoiceErr = "Lo spostamento scelto non è valido.\n" +
                                               "Rifare la selezione.\n";
                                       sendInfo(EventType.ERROR, wrongChoiceErr, getHostNameCreateList().addOneHostName(weaponUser));
                                   } else {
                                       activateEffect();
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
        WeaponEffectType effectType;
        int weaponId;
        String weaponName;
        int nrOfTargetHittable;

        for(WeaponCard card: weaponUser.getLoadedWeapons()){
            List<EnemyInfo> enemyVisible = generateEnemyInfoList(card);
            if(!enemyVisible.isEmpty()) {
                effectType = card.getBaseEffectType();
                weaponId = card.getIdCard();
                weaponName = card.getName();
                nrOfTargetHittable = card.getNrOfPlayerHittable();

                weaponInfoList.add(new WeaponInfo(enemyVisible, effectType, weaponId, weaponName, nrOfTargetHittable));
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
                powerUpForBuy.add(getCreateJson().createPowerUpLM(power));
        }

        for(Player p: enemyChosenList){
            if(!p.isPlayerDown())
                enemyChosenLM.add(new EnemyInfo(p.getIdPlayer(), p.getCharaName(), p.getPosition().getCoordinates(getTurnManager().getGameTable().getMap()), null));
        }

        ammoForBuy[GeneralInfo.RED_ROOM_ID] = weaponUser.getRedAmmo();
        ammoForBuy[GeneralInfo.BLUE_ROOM_ID] = weaponUser.getBlueAmmo();
        ammoForBuy[GeneralInfo.YELLOW_ROOM_ID] = weaponUser.getYellowAmmo();

        if(checkIfCanUseTargetingScope(targetingScopeList, enemyChosenLM, powerUpForBuy, ammoForBuy)){
            ShootPowerUpInfo shootPowerUpInfo = new ShootPowerUpInfo(targetingScopeList, enemyChosenLM, powerUpForBuy, ammoForBuy);
            sendInfo(EventType.CAN_USE_TARGETING_SCOPE, serialize(shootPowerUpInfo), Arrays.asList(weaponUser.getHostname()));
        }
    }

    /**
     * To check if the weapon user can actually use the trargetting scopet that has in hand (if it has
     * any)
     *
     * @param targetingScopeList list of targeting scope that user has got in hand
     * @param enemyChosenLM list of the enemy he can hit with the targeting scope
     * @param powerUpForBuy list of power up he can use to afford the one ammo cost
     * @param ammoForBuy list of ammo in his ammo box he can use to afford the one ammo cost
     * @return true if he can use the power up in his hand, false otherwise
     */
    private boolean checkIfCanUseTargetingScope(List<PowerUpLM> targetingScopeList, List<EnemyInfo> enemyChosenLM, List<PowerUpLM> powerUpForBuy, int[] ammoForBuy){
        boolean result = false;
        if(!targetingScopeList.isEmpty() && !enemyChosenLM.isEmpty()){
            result = true;
        }

        if(result) {
            if(powerUpForBuy.isEmpty())
                result = false;

            if(!result) {
                for (int i = 0; i < ammoForBuy.length; i++) {
                    if (ammoForBuy[i] > 0) {
                        result = true;
                        break;
                    }
                }
            }
        }

        return result;
    }
    /**
     * Notify the enemy hitted if they have got the tagback grenade
     *
     * @author: Luca Iovine
     */
    private void sendTagbackGranedeInfo(List<Player> enemyChosen){
        List<ShootPowerUpInfo> messageToSendList = new ArrayList<>();
        List<PowerUpLM> tagbackList;
        List<String> hostnameEnemyThatHasGotTagback = new ArrayList<>();

        for(Player enemy: enemyChosen) {
            if(canSeeTheWeaponUser(enemy, weaponUser)) {
                tagbackList = new ArrayList<>();
                for (PowerUp power : enemy.getPowerUps()) {
                    if (power.getName().equals("TAGBACK_GRENADE")) {
                        tagbackList.add(getCreateJson().createPowerUpLM(power));
                        if(!hostnameEnemyThatHasGotTagback.contains(enemy.getHostname()))
                            hostnameEnemyThatHasGotTagback.add(enemy.getHostname());
                    }
                }

                if(!tagbackList.isEmpty()){
                    messageToSendList.add(new ShootPowerUpInfo(tagbackList, null, null, null));
                }
            }
        }

        for(int i = 0; i < hostnameEnemyThatHasGotTagback.size(); i++){
            sendInfo(EventType.CAN_USE_TAGBACK, serialize(messageToSendList.get(i)), Arrays.asList(hostnameEnemyThatHasGotTagback.get(i)));
        }
    }

    /**
     * To check if the enemy hitted can see the weapon user in order to use the tagback grenade
     *
     * @param enemy who would use the tagback
     * @param user who would be hitted by the tagback
     * @return true if the enemy can see the user, false otherwise
     */
    private boolean canSeeTheWeaponUser(Player enemy, Player user){
        boolean result = false;
        List<Square> squareListEnemyCanSee = distance.getTargetPosition(AreaOfEffect.CAN_SEE, enemy.getPosition());
        for(Square positionCanSee: squareListEnemyCanSee){
            if(positionCanSee.getPlayerOnSquare().contains(user)) {
                result = true;
                break;
            }
        }
        return result;
    }
    /**
     * Convert the enemy list id into Player of the game
     *
     * @param player the weapon user, in order to communicate him whenever something went wrong
     * @param idEnemyList id of the enmy to convert
     * @return the list of enemy as "Player"
     * @author: Luca Iovine
     */
    private List<Player> convertEnemyListFromView(Player player, List<Integer> idEnemyList){
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
    private Square convertMovementFromView(Player player, int[] position){
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
