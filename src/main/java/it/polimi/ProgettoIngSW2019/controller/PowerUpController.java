package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.Message.toController.*;
import it.polimi.ProgettoIngSW2019.common.Message.toView.*;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class that manages the use of the powerups
 * @author Priscilla Lo Bue
 */
public class PowerUpController extends Controller {
    private PowerUp powerUpToUse;
    private Player ownerPlayer;
    private Player targetPlayer;
    private Square position;
    private AmmoType ammoToSpend;
    List<EnemyInfo> enemyInfos = new ArrayList<>();


    /**
     * Constructor
     * @param turnManager           TurnManager
     * @param virtualView           VirtualView
     * @param idConverter           IdConverter
     * @param createJson            CreateJson
     * @param hostNameCreateList    hostNameCreateList
     */
    public PowerUpController(TurnManager turnManager, VirtualView virtualView, IdConverter idConverter, CreateJson createJson, HostNameCreateList hostNameCreateList) {
        super(turnManager, virtualView, idConverter, createJson, hostNameCreateList);
    }


    /**
     * update to receive the events
     * @param event     event message from view
     */
    public void update(Event event) {
        EventType eventType = event.getCommand();

        switch(eventType) {
            case TELEPORTER:
                useTeleporter(event.getMessageInJsonFormat());
                break;

            case REQUEST_NEWTON_INFO:
                newtonInfoFromView(event.getMessageInJsonFormat());
                break;

            case NEWTON:
                useNewton(event.getMessageInJsonFormat());
                break;

            case TAGBACK_GRENADE:
                useTagBackGrenade(event.getMessageInJsonFormat());
                break;

            case TARGETING_SCOPE:
                useTargetingScope(event.getMessageInJsonFormat());
                break;
        }

    }


    /**
     * Check info from view to use teleporter
     * use teleporter
     * @param messageJson       message from view in json format
     */
    //tested
    private void useTeleporter(String messageJson) {
        TeleporterRequest teleporterRequest = new Gson().fromJson(messageJson, TeleporterRequest.class);

        ownerPlayer = convertPlayer(teleporterRequest.getIdPlayer(), teleporterRequest.getHostNamePlayer());

        if(ownerPlayer != null) {
            if (checkHostNameCorrect(ownerPlayer, teleporterRequest.getHostNamePlayer())) {
                if (checkCurrentPlayer(ownerPlayer)) {
                    powerUpToUse = convertPowerUp(ownerPlayer, teleporterRequest.getIdPowerUp());

                    if (powerUpToUse != null) {
                        if (powerUpToUse.getName().equals(GeneralInfo.TELEPORTER)) {
                            Square square = ownerPlayer.getPosition();
                            int[] playerCoordinates = square.getCoordinates(getTurnManager().getGameTable().getMap());

                            if (teleporterRequest.getCoordinates() != playerCoordinates) {
                                position = convertSquare(ownerPlayer, teleporterRequest.getCoordinates());
                                powerUpToUse.usePowerUpEffect(null, null, null, ownerPlayer, position);
                                ownerPlayer.discardPowerUps(Arrays.asList(powerUpToUse));

                                String messageUsePowerUp = getCreateJson().createMessagePowerUpsDiscardedJson(ownerPlayer, Arrays.asList(powerUpToUse));
                                sendInfo(EventType.MSG_USE_POWERUP, messageUsePowerUp, getHostNameCreateList().addAllHostName());

                                String updateMapLMJson = getCreateJson().createMapLMJson();
                                sendInfo(EventType.UPDATE_MAP, updateMapLMJson, getHostNameCreateList().addAllHostName());

                                String updateMyPowerUpsJson = getCreateJson().createMyPowerUpsLMJson(ownerPlayer);
                                sendInfo(EventType.UPDATE_MY_POWERUPS, updateMyPowerUpsJson, getHostNameCreateList().addOneHostName(ownerPlayer));

                                String mess = "";
                                sendInfo(EventType.MSG_BEFORE_ENEMY_ACTION_OR_RELOAD, mess, getHostNameCreateList().addAllExceptOneHostName(ownerPlayer));

                                msgActionLeft(ownerPlayer);
                            }
                        }
                        else {
                            String messageError = GeneralInfo.MSG_ERROR;
                            sendInfo(EventType.ERROR, messageError, getHostNameCreateList().addOneHostName(ownerPlayer));
                        }
                    }
                }
            }
        }
    }


    /**
     * Check info from view to use TagBackGrenade
     * use TagBackGrenade
     * @param messageJson       message from view in json format
     */
    //tested
    private void useTagBackGrenade(String messageJson) {
        TagBackGrenadeRequest tagBackGrenadeRequest = new Gson().fromJson(messageJson, TagBackGrenadeRequest.class);

        ownerPlayer = convertPlayer(tagBackGrenadeRequest.getIdPlayer(), tagBackGrenadeRequest.getHostNamePlayer());

        if(ownerPlayer != null) {
            if (checkHostNameCorrect(ownerPlayer, tagBackGrenadeRequest.getHostNamePlayer())) {
                if(ownerPlayer.getIdPlayer() != getTurnManager().getCurrentPlayer().getIdPlayer() &&
                    tagBackGrenadeRequest.getIdTarget() == getTurnManager().getCurrentPlayer().getIdPlayer()) {
                    powerUpToUse = convertPowerUp(ownerPlayer, tagBackGrenadeRequest.getIdPowerUp());

                    if(powerUpToUse != null) {
                        if (powerUpToUse.getName().equals(GeneralInfo.TAGBACK_GRENADE)) {
                            targetPlayer = convertPlayer(tagBackGrenadeRequest.getIdTarget(), ownerPlayer.getHostname());

                            if (targetPlayer != null) {
                                int size = ownerPlayer.getDamageLine().size();
                                String nameLastHit = ownerPlayer.getDamageLine().get(size - 1);
                                Player playerLastHit = getTurnManager().getPlayerFromCharaName(nameLastHit);

                                if (playerLastHit.getIdPlayer() == targetPlayer.getIdPlayer()) {

                                    powerUpToUse.usePowerUpEffect(null, null, targetPlayer, ownerPlayer, null);
                                    ownerPlayer.discardPowerUps(Arrays.asList(powerUpToUse));

                                    String messageUsePowerUp = getCreateJson().createMessagePowerUpsDiscardedJson(ownerPlayer, Arrays.asList(powerUpToUse));
                                    sendInfo(EventType.MSG_USE_POWERUP, messageUsePowerUp, getHostNameCreateList().addAllHostName());

                                    String messageTagBackGrenade = createMessageTagBackGrenade();
                                    sendInfo(EventType.MSG_TAGBACK_GRENADE, messageTagBackGrenade, getHostNameCreateList().addAllHostName());

                                    String updateEnemyPlayer = getCreateJson().createPlayerLMJson(targetPlayer);
                                    sendInfo(EventType.UPDATE_PLAYER_INFO, updateEnemyPlayer, getHostNameCreateList().addAllHostName());

                                    String updateMyPowerUpsJson = getCreateJson().createMyPowerUpsLMJson(ownerPlayer);
                                    sendInfo(EventType.UPDATE_MY_POWERUPS, updateMyPowerUpsJson, getHostNameCreateList().addOneHostName(ownerPlayer));

                                    String mess = "";
                                    sendInfo(EventType.MSG_BEFORE_ENEMY_ACTION_OR_RELOAD, mess, getHostNameCreateList().addAllExceptOneHostName(ownerPlayer));

                                } else {
                                    String messageError = GeneralInfo.MSG_ERROR;
                                    sendInfo(EventType.ERROR, messageError, getHostNameCreateList().addOneHostName(ownerPlayer));
                                }
                            }
                        }
                        else {
                            String messageError = GeneralInfo.MSG_ERROR;
                            sendInfo(EventType.ERROR, messageError, getHostNameCreateList().addOneHostName(ownerPlayer));
                        }
                    }
                }
                else {
                    String messageError = GeneralInfo.MSG_ERROR;
                    sendInfo(EventType.ERROR, messageError, getHostNameCreateList().addOneHostName(ownerPlayer));
                }
            }
        }
    }


    /**
     * Check info from view to use Newton
     * @param messageJson       message from view in json format
     */
    //tested
    private void newtonInfoFromView(String messageJson) {
        PowerUpChoiceRequest powerUpChoiceRequest = new Gson().fromJson(messageJson, PowerUpChoiceRequest.class);
        ownerPlayer = convertPlayer(powerUpChoiceRequest.getIdPlayer(), powerUpChoiceRequest.getHostNamePlayer());

        if(ownerPlayer != null) {
            if(checkHostNameCorrect(ownerPlayer, powerUpChoiceRequest.getHostNamePlayer())) {
                if (checkCurrentPlayer(ownerPlayer)) {
                    powerUpToUse = convertPowerUp(ownerPlayer, powerUpChoiceRequest.getIdPowerUp());

                    if(powerUpToUse != null) {
                        if (powerUpToUse.getName().equals(GeneralInfo.NEWTON)) {
                            List<int[]> coordinatesMovements = new ArrayList<>();
                            List<Square> directions = new ArrayList<>();
                            enemyInfos.clear();

                            for (int i = 0; i < getTurnManager().getGameTable().getPlayers().length; i++) {
                                if (i != ownerPlayer.getIdPlayer() && getTurnManager().getGameTable().getPlayers()[i].getPosition() != null) {
                                    Player player = convertPlayerWithId(i);
                                    directions.clear();
                                    directions = getDirectionsMovement(player);
                                    coordinatesMovements.clear();

                                    for (Square s : directions) {
                                        coordinatesMovements.add(s.getCoordinates(getTurnManager().getGameTable().getMap()));
                                    }

                                    EnemyInfo enemyInfo = new EnemyInfo(i, player.getCharaName(), player.getPosition().getCoordinates(getTurnManager().getGameTable().getMap()), coordinatesMovements);
                                    enemyInfos.add(enemyInfo);
                                }
                            }

                            NewtonInfoResponse newtonInfoResponse = new NewtonInfoResponse(ownerPlayer.getIdPlayer(), enemyInfos);
                            String newtonInfoResponseJson = new Gson().toJson(newtonInfoResponse);
                            sendInfo(EventType.RESPONSE_NEWTON_INFO, newtonInfoResponseJson, getHostNameCreateList().addOneHostName(ownerPlayer));

                            if(enemyInfos.isEmpty()) {
                                String updateMyPowerUpsJson = getCreateJson().createMyPowerUpsLMJson(ownerPlayer);
                                sendInfo(EventType.UPDATE_MY_POWERUPS, updateMyPowerUpsJson, getHostNameCreateList().addOneHostName(ownerPlayer));

                                String mess = "";
                                sendInfo(EventType.MSG_BEFORE_ENEMY_ACTION_OR_RELOAD, mess, getHostNameCreateList().addAllExceptOneHostName(ownerPlayer));
                            }
                        }
                        else {
                            String messageError = GeneralInfo.MSG_ERROR;
                            sendInfo(EventType.ERROR, messageError, getHostNameCreateList().addOneHostName(ownerPlayer));
                        }
                    }
                }
            }
        }
    }


    /**
     * Check info from view to use Newton
     * use Newton
     * @param messageJson       message from view in json format
     */
    //tested
    private void useNewton(String messageJson) {
        NewtonRequest newtonRequest = new Gson().fromJson(messageJson, NewtonRequest.class);

        if(ownerPlayer.getIdPlayer() == newtonRequest.getIdPlayer() && ownerPlayer.getHostname().equals(newtonRequest.getHostNamePlayer())) {
            if (powerUpToUse.getIdCard() == newtonRequest.getIdPowerUp()) {
                for (int i = 0; i < getTurnManager().getGameTable().getPlayers().length; i++) {
                    if (i != ownerPlayer.getIdPlayer()) {
                        boolean found = false;

                        for (EnemyInfo enemyInfo : enemyInfos) {
                            if (newtonRequest.getIdTarget() == enemyInfo.getId()) {
                                boolean result = false;

                                for(int k = 0; k < enemyInfo.getMovement().size(); k++) {
                                    if(enemyInfo.getMovement().get(k)[0] == newtonRequest.getCoordinatesMovement()[0] &&
                                            enemyInfo.getMovement().get(k)[1] == newtonRequest.getCoordinatesMovement()[1]) {
                                        result = true;
                                    }
                                }

                                if (result) {
                                    found = true;
                                }
                            }
                        }

                        if (found) {
                            targetPlayer = convertPlayer(newtonRequest.getIdTarget(), ownerPlayer.getHostname());
                            position = convertSquare(ownerPlayer, newtonRequest.getCoordinatesMovement());
                            powerUpToUse.usePowerUpEffect(null, null, targetPlayer, null, position);

                            ownerPlayer.discardPowerUps(Arrays.asList(powerUpToUse));

                            String messageUsePowerUp = getCreateJson().createMessagePowerUpsDiscardedJson(ownerPlayer, Arrays.asList(powerUpToUse));
                            sendInfo(EventType.MSG_USE_POWERUP, messageUsePowerUp, getHostNameCreateList().addAllHostName());

                            String updateMap = getCreateJson().createMapLMJson();
                            sendInfo(EventType.UPDATE_MAP, updateMap, getHostNameCreateList().addAllHostName());

                            String updateMyPowerUpsJson = getCreateJson().createMyPowerUpsLMJson(ownerPlayer);
                            sendInfo(EventType.UPDATE_MY_POWERUPS, updateMyPowerUpsJson, getHostNameCreateList().addOneHostName(ownerPlayer));

                            String mess = "";
                            sendInfo(EventType.MSG_BEFORE_ENEMY_ACTION_OR_RELOAD, mess, getHostNameCreateList().addAllExceptOneHostName(ownerPlayer));

                            msgActionLeft(ownerPlayer);
                        } else {
                            String messageError = GeneralInfo.MSG_ERROR;
                            sendInfo(EventType.ERROR, messageError, getHostNameCreateList().addOneHostName(ownerPlayer));
                        }
                    }
                }
            }
        }
    }


    /**
     * Check info from view to use TargetingScope
     * use TargetingScope
     * @param messageJson       message from view in json format
     */
    //tested
    private void useTargetingScope(String messageJson) {
        TargetingScopeRequest targetingScopeRequest = new Gson().fromJson(messageJson, TargetingScopeRequest.class);
        ownerPlayer = convertPlayer(targetingScopeRequest.getIdPlayer(), targetingScopeRequest.getHostNamePlayer());
        targetPlayer = convertPlayer(targetingScopeRequest.getIdTarget(), targetingScopeRequest.getHostNamePlayer());

        if(ownerPlayer != null && targetPlayer != null) {
            if(checkCurrentPlayer(ownerPlayer)) {
                powerUpToUse = convertPowerUp(ownerPlayer, targetingScopeRequest.getIdPowerUp());
                if (powerUpToUse != null) {
                    if (powerUpToUse.getName().equals(GeneralInfo.TARGETING_SCOPE)) {
                        if (checkTargetDamaged()) {
                            if (targetingScopeRequest.getAmmoToSpend() != null) {
                                ammoToSpend = targetingScopeRequest.getAmmoToSpend();
                                powerUpToUse.usePowerUpEffect(targetingScopeRequest.getAmmoToSpend(), null, targetPlayer, ownerPlayer, null);
                            } else if (targetingScopeRequest.getIdPowerUpToSpend() >= 0) {
                                PowerUp powerUpToDiscard = convertPowerUp(ownerPlayer, targetingScopeRequest.getIdPowerUp());

                                if (powerUpToDiscard != null) {
                                    ammoToSpend = powerUpToDiscard.getGainAmmoColor();
                                    powerUpToUse.usePowerUpEffect(null, powerUpToDiscard, targetPlayer, ownerPlayer, null);

                                    String messagePowerUpsDiscarded = getCreateJson().createMessagePowerUpsDiscardedJson(ownerPlayer, Arrays.asList(powerUpToDiscard));
                                    sendInfo(EventType.MSG_POWERUPS_DISCARDED_AS_AMMO, messagePowerUpsDiscarded, getHostNameCreateList().addAllHostName());
                                }
                            }
                            ownerPlayer.discardPowerUps(Arrays.asList(powerUpToUse));

                            String messageUsePowerUp = getCreateJson().createMessagePowerUpsDiscardedJson(ownerPlayer, Arrays.asList(powerUpToUse));
                            sendInfo(EventType.MSG_USE_POWERUP, messageUsePowerUp, getHostNameCreateList().addAllHostName());

                            String messageTargetingScopeJson = createMessageTargetingScope();
                            sendInfo(EventType.MSG_TARGETING_SCOPE, messageTargetingScopeJson, getHostNameCreateList().addAllHostName());

                            if (targetingScopeRequest.getAmmoToSpend() != null) {
                                String updatePlayerJson = getCreateJson().createPlayerLMJson(ownerPlayer);
                                sendInfo(EventType.UPDATE_PLAYER_INFO, updatePlayerJson, getHostNameCreateList().addAllHostName());
                            }

                            String updatePowerUpJson = getCreateJson().createMyPowerUpsLMJson(ownerPlayer);
                            sendInfo(EventType.UPDATE_MY_POWERUPS, updatePowerUpJson, getHostNameCreateList().addOneHostName(ownerPlayer));

                            String mess = "";
                            sendInfo(EventType.MSG_BEFORE_ENEMY_ACTION_OR_RELOAD, mess, getHostNameCreateList().addAllExceptOneHostName(ownerPlayer));
                        }
                    }
                }
                else {
                    String messageError = GeneralInfo.MSG_ERROR;
                    sendInfo(EventType.ERROR, messageError, getHostNameCreateList().addOneHostName(ownerPlayer));
                }
            }
        }
    }


    /**
     * check if the target is one of the enemies who hit with the shoot
     * @return      true if is correct, false otherwise
     */
    //tested
    private boolean checkTargetDamaged() {
        List<Player> playersDamagedByShooter = new ArrayList<>();

        for(int i = 0; i < getTurnManager().getGameTable().getPlayers().length; i++) {
            if(i != ownerPlayer.getIdPlayer()) {
                Player playerDamaged = convertPlayerWithId(i);
                int size = playerDamaged.getDamageLine().size();
                String lastHitName = playerDamaged.getDamageLine().get(size-1);

                if (lastHitName.equals(ownerPlayer.getCharaName()))
                    playersDamagedByShooter.add(playerDamaged);
            }
        }

        for(Player player:playersDamagedByShooter) {
            if(player.getIdPlayer() == targetPlayer.getIdPlayer())
                return true;
        }

        String messageError = GeneralInfo.MSG_ERROR;
        sendInfo(EventType.ERROR, messageError, getHostNameCreateList().addOneHostName(ownerPlayer));
        return false;
    }


    /**
     * get a list of directions the player can move
     * @param fromPlayer        player target to move
     * @return                  list of squares to move
     */
    public List<Square> getDirectionsMovement(Player fromPlayer){
        List<Square> directions = new ArrayList<>();

        if(!getNorthDirection(fromPlayer).isEmpty())
            directions.addAll(getNorthDirection(fromPlayer));

        if(!getSouthDirection(fromPlayer).isEmpty())
            directions.addAll(getSouthDirection(fromPlayer));

        if(!getEastDirection(fromPlayer).isEmpty())
            directions.addAll(getEastDirection(fromPlayer));

        if(!getWestDirection(fromPlayer).isEmpty())
            directions.addAll(getWestDirection(fromPlayer));

        return directions;
    }


    /**
     * get north squares
     * @param fromPlayer        player to move
     * @return                  list of max 2 squares to the north from player square
     */
    private List<Square> getNorthDirection(Player fromPlayer){
        List<Square> northDirection = new ArrayList<>();
        Square northSquare;
        Square square = fromPlayer.getPosition();

        if(!square.getIsBlockedAtNorth()) {
            northSquare = fromPlayer.getPosition().getNorthSquare();
            northDirection.add(fromPlayer.getPosition().getNorthSquare());

            if(!northSquare.getIsBlockedAtNorth()) {
                northDirection.add(northSquare.getNorthSquare());
            }
        }
        return northDirection;
    }


    /**
     * get south squares
     * @param fromPlayer        player to move
     * @return                  list of max 2 squares to the south from player square
     */
    private List<Square> getSouthDirection(Player fromPlayer) {
        List<Square> southDirection = new ArrayList<>();
        Square southSquare;
        Square square = fromPlayer.getPosition();

        if(!square.getIsBlockedAtSouth()) {
            southSquare = square.getSouthSquare();
            southDirection.add(southSquare);

            if(!southSquare.getIsBlockedAtSouth()) {
                southDirection.add(southSquare.getSouthSquare());
            }
        }
        return southDirection;
    }


    /**
     * get east squares
     * @param fromPlayer        player to move
     * @return                  list of max 2 squares to the east from player square
     */
    private List<Square> getEastDirection(Player fromPlayer) {
        List<Square> eastDirection = new ArrayList<>();
        Square eastSquare;
        Square square = fromPlayer.getPosition();

        if(!fromPlayer.getPosition().getIsBlockedAtEast()) {
            eastSquare = square.getEastSquare();
            eastDirection.add(eastSquare);

            if(!eastSquare.getIsBlockedAtEast()) {
                eastDirection.add(eastSquare.getEastSquare());
            }
        }
        return eastDirection;
    }


    /**
     * get west squares
     * @param fromPlayer        player to move
     * @return                  list of max 2 squares to the west from player square
     */
    private List<Square> getWestDirection(Player fromPlayer) {
        List<Square> westDirection = new ArrayList<>();
        Square westSquare;
        Square square = fromPlayer.getPosition();

        if(!fromPlayer.getPosition().getIsBlockedAtWest()) {
            westSquare = square.getWestSquare();
            westDirection.add(square.getWestSquare());

            if(!westSquare.getIsBlockedAtWest()) {
                westDirection.add(westSquare.getWestSquare());
            }
        }
        return westDirection;
    }


    /**
     * creates message TargetingScope to view
     * @return      message into json format
     */
    private String createMessageTargetingScope() {
        MessageTargetingScope messageTargetingScope = new MessageTargetingScope(ownerPlayer.getIdPlayer(), ownerPlayer.getCharaName(), ammoToSpend, targetPlayer.getCharaName());
        return new Gson().toJson(messageTargetingScope);
    }


    /**
     * creates TagBack message to view
     * @return      message into json format
     */
    private String createMessageTagBackGrenade() {
        MessageTagBackGrenade messageTagBackGrenade = new MessageTagBackGrenade(ownerPlayer.getIdPlayer(), ownerPlayer.getCharaName(), targetPlayer.getCharaName());
        return new Gson().toJson(messageTagBackGrenade);
    }
}
