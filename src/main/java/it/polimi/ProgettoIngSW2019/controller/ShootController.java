package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.Message.toController.InfoRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toController.ShootChoiceRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.EnemyInfo;
import it.polimi.ProgettoIngSW2019.common.Message.toView.WeaponInfo;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.enums.WeaponEffectType;
import it.polimi.ProgettoIngSW2019.custom_exception.EnemySizeLimitExceededException;
import it.polimi.ProgettoIngSW2019.custom_exception.IllegalAttributeException;
import it.polimi.ProgettoIngSW2019.custom_exception.IllegalIdException;
import it.polimi.ProgettoIngSW2019.custom_exception.NotPartOfBoardException;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;

import javax.naming.SizeLimitExceededException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class ShootController
 *
 * @author: Luca Iovine
 */
public class ShootController extends Controller{
    private Gson gson = new Gson();
    private Player weaponUser;
    private WeaponCard weaponChosen;
    private List<Player> enemyChosenList = new ArrayList<>();
    private Square positionToMove;
    private DistanceDictionary distance;
    String somethingWentWrong = "Ops, qualcosa è andato storto!";
    String wrongChoiceErr;

    public ShootController(TurnManager turnManager, IdConverter idConverter, VirtualView virtualView, CreateJson createJson, HostNameCreateList hostNameCreateList, DistanceDictionary distance) {
        super(turnManager, virtualView, idConverter, createJson, hostNameCreateList);
        this.distance = distance;
    }

    @Override
    public void update(Event event) {
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

                           if (enemyChosenList.size() != 0) {
                               positionToMove = convertkMovementFromView(weaponUser, shootChoice.getPositionChosen());

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
                                   }
                               } catch (EnemySizeLimitExceededException e) {
                                   //TODO: to handle
                               }
                           }
                       }
                   }
               }
               break;
       }
    }

    private void sendWeaponInfo(){
        List<WeaponInfo> weaponInfoList = new ArrayList<>();
        boolean isEnemyToMove;
        boolean hasToMove;
        WeaponEffectType effectType;

        for(WeaponCard card: weaponUser.getLoadedWeapons()){
            List<EnemyInfo> enemyVisible = generateEnemyInfoList(card);
            hasToMove = card.hasToMoveInBaseEffect();
            effectType = card.getBaseEffectType();

            weaponInfoList.add(new WeaponInfo(enemyVisible, hasToMove, effectType));
        }

        String jsonObj = gson.toJson(weaponInfoList);
        sendInfo(EventType.RESPONSE_REQUEST_WEAPON_INFO, jsonObj, getHostNameCreateList().addOneHostName(weaponUser));
    }

    private void activateEffect(){
        try {
            weaponChosen.activateBaseEff(weaponUser, enemyChosenList);
        } catch (SizeLimitExceededException e) {
            //TODO: to handle
        }
    }

    private List<EnemyInfo> generateEnemyInfoList(WeaponCard card){
        List<EnemyInfo> enemyInfoList = new ArrayList<>();
        List<Square> area = distance.getTargetPosition(card.getBaseEffectAoe(), weaponUser.getPosition());

        for(Square s: area){
            for(Player enemy: s.getPlayerOnSquare()){
                List<int[]> movementList = new ArrayList<>();
                for(Square movementPos: card.getMovementList(weaponUser, enemy)){
                    movementList.add(movementPos.getCoordinates(getTurnManager().getGameTable().getMap()));
                }
                int[] enemyPosition = enemy.getPosition().getCoordinates(getTurnManager().getGameTable().getMap());

                enemyInfoList.add(new EnemyInfo(enemy.getIdPlayer(), enemy.getCharaName(), enemyPosition, movementList));
            }
        }

        return enemyInfoList;
    }

    private void sendTargetingScopeInfo(){
        boolean hasTargetingScope = false;

        for(PowerUp power: weaponUser.getPowerUps()){
            if(power.getName().equals("TARGETING_SCOPE")){
                hasTargetingScope = true;
                break;
            }
        }

        if(hasTargetingScope){
            //TODO: aspettare di vedere se utilizzare il controller dei power up e come
        }
    }

    private void sendTagbackGranedeInfo(){
        boolean hasTagbackGranede = false;

        for(PowerUp power: weaponUser.getPowerUps()){
            if(power.getName().equals("TAGBACK_GRENADE")){
                hasTagbackGranede = true;
                break;
            }
        }

        if(hasTagbackGranede){
            //TODO: aspettare di vedere se utilizzare il controller dei power up e come
        }
    }

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

    private Square convertkMovementFromView(Player player, int[] position){
        Square newPostion = null;

        try{
            newPostion = getIdConverter().getSquareByCoordinates(position);
        }catch(NotPartOfBoardException e){
            sendInfo(EventType.ERROR, somethingWentWrong, getHostNameCreateList().addOneHostName(player));
        }

        return newPostion;
    }
}
