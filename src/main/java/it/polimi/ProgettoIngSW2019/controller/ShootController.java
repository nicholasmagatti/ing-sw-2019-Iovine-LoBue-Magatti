package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.Message.toController.InfoRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toController.ShootChoiceRequest;
import it.polimi.ProgettoIngSW2019.common.Message.toView.EnemyInfo;
import it.polimi.ProgettoIngSW2019.common.Message.toView.WeaponInfo;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.enums.WeaponEffectType;
import it.polimi.ProgettoIngSW2019.custom_exception.IllegalAttributeException;
import it.polimi.ProgettoIngSW2019.custom_exception.IllegalIdException;
import it.polimi.ProgettoIngSW2019.custom_exception.NotPartOfBoardException;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.model.dictionary.DistanceDictionary;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;

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

    public ShootController(TurnManager turnManager, IdConverter idConverter, VirtualView virtualView, CreateJson createJson, IdPlayersCreateList idPlayersCreateList, DistanceDictionary distance) {
        super(turnManager, virtualView, idConverter, createJson, idPlayersCreateList);
        this.distance = distance;
    }

    @Override
    public void update(Event event) {
       switch(event.getCommand()){
           case REQUEST_WEAPON_INFO:
               InfoRequest infoRequest = gson.fromJson(event.getMessageInJsonFormat(), InfoRequest.class);
               weaponUser = convertPlayer(infoRequest.getIdPlayer());

               if(weaponUser != null){
                   if(checkCurrentPlayer(weaponUser))
                       sendWeaponInfo();
               }
               break;
           case REQUEST_SHOOT:
               ShootChoiceRequest shootChoice = gson.fromJson(event.getMessageInJsonFormat(), ShootChoiceRequest.class);
               weaponUser = convertPlayer(shootChoice.getIdPlayer());

               if(weaponUser != null){
                   if(checkCurrentPlayer(weaponUser)){
                       weaponChosen = convertWeaponCardFromView(weaponUser, shootChoice.getIdWeaponUsed());
                       enemyChosenList = convertEnemyListFromView(weaponUser, shootChoice.getEnemyChosenListId());

                       if(enemyChosenList.size() != 0){
                           positionToMove = convertkMovementFromView(weaponUser, shootChoice.getPositionChosen());

                           if (!weaponChosen.checkBaseEffectParameterValidity(weaponUser, enemyChosenList)) {
                               wrongChoiceErr = "I nemici scelti non sono validi.\n" +
                                       "Rifare la selezione.\n";
                               sendInfo(EventType.ERROR, wrongChoiceErr, getIdPlayersCreateList().addOneIdPlayers(weaponUser));

                           } else if (!weaponChosen.checkBaseEffectMovementPositionValidity(positionToMove)) {
                               wrongChoiceErr = "Lo spostamento scelto non è valido.\n" +
                                       "Rifare la selezione.\n";
                               sendInfo(EventType.ERROR, wrongChoiceErr, getIdPlayersCreateList().addOneIdPlayers(weaponUser));
                           } else {
                               activateEffect();
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
            isEnemyToMove = card.isEnemyMoveInBaseEffect();
            hasToMove = card.hasToMoveInBaseEffect();
            effectType = card.getBaseEffectType();

            weaponInfoList.add(new WeaponInfo(enemyVisible, isEnemyToMove, hasToMove, effectType));
        }

        String jsonObj = gson.toJson(weaponInfoList);
        sendInfo(EventType.RESPONSE_REQUEST_WEAPON_INFO, jsonObj, getIdPlayersCreateList().addOneIdPlayers(weaponUser));
    }

    private void activateEffect(){
        weaponChosen.activateBaseEff(weaponUser, enemyChosenList);
    }

    private List<EnemyInfo> generateEnemyInfoList(WeaponCard card){
        List<EnemyInfo> enemyInfoList = new ArrayList<>();
        List<Square> area = distance.getTargetPosition(card.getBaseEffectAoe(), weaponUser.getPosition());

        for(Square s: area){
            for(Player enemy: s.getPlayerOnSquare()){
                List<int[]> movementList = new ArrayList<>();
                for(Square movementPos: card.getMovementList(weaponUser)){
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

    private WeaponCard convertWeaponCardFromView(Player player, int idWeapon){
        WeaponCard weapon = null;

        if(idWeapon < -1)
            throw new IllegalAttributeException("id weapon cannot be negative");

        try{
            weapon = getIdConverter().getLoadedWeaponById(player.getIdPlayer(), idWeapon);
        }catch(IllegalIdException e){
            sendInfo(EventType.ERROR, somethingWentWrong, getIdPlayersCreateList().addOneIdPlayers(player));
        }

        return weapon;
    }

    private List<Player> convertEnemyListFromView(Player player, List<Integer> idEnemyList){
        List<Player> enemylist = new ArrayList<>();

        for(int idEnemy: idEnemyList) {
            if(idEnemy < -1)
                throw new IllegalAttributeException("id player (enemy) cannot be negative");

            try {
                enemylist.add(getIdConverter().getPlayerById(idEnemy));
            }catch(IllegalIdException e){
                sendInfo(EventType.ERROR, somethingWentWrong, getIdPlayersCreateList().addOneIdPlayers(player));
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
            sendInfo(EventType.ERROR, somethingWentWrong, getIdPlayersCreateList().addOneIdPlayers(player));
        }

        return newPostion;
    }
}
