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

public class PowerUpController extends Controller {
    private PowerUp powerUpToUse;
    private Player ownerPlayer;
    private Player targetPlayer;
    private Square position;
    private AmmoType ammoToSpend;
    List<EnemyInfo> enemyInfos = new ArrayList<>();


    public PowerUpController(TurnManager turnManager, VirtualView virtualView, IdConverter idConverter, CreateJson createJson, HostNameCreateList hostNameCreateList) {
        super(turnManager, virtualView, idConverter, createJson, hostNameCreateList);
    }



    public void update(Event event) {
        EventType eventType = event.getCommand();

        switch(eventType) {
            case TELEPORTER:
                useTeleporter(event.getMessageInJsonFormat());
                break;

            case RESPONSE_NEWTON_INFO:
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


    private void useTeleporter(String messageJson) {
        TeleporterRequest teleporterRequest = new Gson().fromJson(messageJson, TeleporterRequest.class);

        ownerPlayer = convertPlayer(teleporterRequest.getIdPlayer(), teleporterRequest.getHostNamePlayer());

        if(ownerPlayer != null) {
            if (checkHostNameCorrect(ownerPlayer, teleporterRequest.getHostNamePlayer())) {
                if (checkCurrentPlayer(ownerPlayer)) {
                    powerUpToUse = convertPowerUp(ownerPlayer, teleporterRequest.getIdPowerUp());

                    if (powerUpToUse != null && powerUpToUse.getName().equals(GeneralInfo.TELEPORTER)) {
                        Square square = ownerPlayer.getPosition();
                        int[] playerCoordinates = square.getCoordinates(getTurnManager().getGameTable().getMap());

                        if (teleporterRequest.getCoordinates() != playerCoordinates) {
                            position = convertSquare(ownerPlayer, teleporterRequest.getCoordinates());
                            powerUpToUse.usePowerUpEffect(null, null,null, ownerPlayer, position);
                            ownerPlayer.discardPowerUps(Arrays.asList(powerUpToUse));

                            String updateMapLMJson = getCreateJson().createMapLMJson();
                            sendInfo(EventType.UPDATE_MAP, updateMapLMJson, getHostNameCreateList().addAllHostName());

                            String updateMyPowerUpsJson = getCreateJson().createMyPowerUpsLMJson(ownerPlayer);
                            sendInfo(EventType.UPDATE_MY_POWERUPS, updateMyPowerUpsJson, getHostNameCreateList().addOneHostName(ownerPlayer));

                            String mess = "";
                            sendInfo(EventType.MSG_BEFORE_ENEMY_ACTION_OR_RELOAD, mess, getHostNameCreateList().addAllExceptOneHostName(ownerPlayer));

                            msgActionLeft(ownerPlayer);
                        }
                    }
                }
            }
        }
    }


    //done
    private void useTagBackGrenade(String messageJson) {
        TagBackGrenadeRequest tagBackGrenadeRequest = new Gson().fromJson(messageJson, TagBackGrenadeRequest.class);

        ownerPlayer = convertPlayer(tagBackGrenadeRequest.getIdPlayer(), tagBackGrenadeRequest.getHostNamePlayer());

        if(ownerPlayer != null) {
            if (checkHostNameCorrect(ownerPlayer, tagBackGrenadeRequest.getHostNamePlayer())) {
                if(ownerPlayer.getIdPlayer() != getTurnManager().getCurrentPlayer().getIdPlayer()) {
                    powerUpToUse = convertPowerUp(ownerPlayer, tagBackGrenadeRequest.getIdPowerUp());

                    if(powerUpToUse != null) {
                        targetPlayer = convertPlayer(tagBackGrenadeRequest.getIdTarget(), ownerPlayer.getHostname());

                        if(targetPlayer != null){
                            int size = ownerPlayer.getDamageLine().size();
                            String nameLastHit = ownerPlayer.getDamageLine().get(size);
                            Player playerLastHit = getTurnManager().getPlayerFromCharaName(nameLastHit);

                            if(playerLastHit.getIdPlayer() == targetPlayer.getIdPlayer()) {

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

                                msgActionLeft(ownerPlayer);
                            }
                            else {
                                String messageError = "ERROR: ops, qualcosa è andato storto";
                                sendInfo(EventType.ERROR, messageError, getHostNameCreateList().addOneHostName(ownerPlayer));
                            }
                        }
                    }
                }
            }
        }
    }



    private void newtonInfoFromView(String messageJson) {
        PowerUpChoiceRequest powerUpChoiceRequest = new Gson().fromJson(messageJson, PowerUpChoiceRequest.class);
        ownerPlayer = convertPlayer(powerUpChoiceRequest.getIdPlayer(), powerUpChoiceRequest.getHostNamePlayer());

        if(ownerPlayer != null) {
            if(checkHostNameCorrect(ownerPlayer, powerUpChoiceRequest.getHostNamePlayer())) {
                if (checkCurrentPlayer(ownerPlayer)) {
                    powerUpToUse = convertPowerUp(ownerPlayer, powerUpChoiceRequest.getIdPowerUp());

                    if(powerUpToUse != null && powerUpToUse.getName().equals(GeneralInfo.NEWTON)) {
                        List<int[]> coordinatesMovements = new ArrayList<>();
                        List<Square> directions = new ArrayList<>();

                        for(int i = 0; i < getTurnManager().getGameTable().getPlayers().length; i++) {
                            if(i != ownerPlayer.getIdPlayer()) {
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
                    }
                }
            }
        }
    }



    private void useNewton(String messageJson) {
        NewtonRequest newtonRequest = new Gson().fromJson(messageJson, NewtonRequest.class);

        if(ownerPlayer.getIdPlayer() == newtonRequest.getIdPlayer() && ownerPlayer.getHostname().equals(newtonRequest.getHostNamePlayer())) {
            for(int i = 0; i < getTurnManager().getGameTable().getPlayers().length; i++) {
                if(i != ownerPlayer.getIdPlayer()) {
                    boolean found = false;

                    for(EnemyInfo enemyInfo:enemyInfos) {
                        if(newtonRequest.getIdTarget() == enemyInfo.getId() && enemyInfo.getMovement().contains(newtonRequest.getCoordinatesMovement())) {
                            found = true;
                        }
                    }

                    if(found) {
                        targetPlayer = convertPlayer(newtonRequest.getIdTarget(), ownerPlayer.getHostname());
                        position = convertSquare(ownerPlayer, newtonRequest.getCoordinatesMovement());
                        powerUpToUse.usePowerUpEffect(null, null, targetPlayer, null, position);

                        ownerPlayer.discardPowerUps(Arrays.asList(powerUpToUse));

                        String messageUsePowerUp = getCreateJson().createMessagePowerUpsDiscardedJson(ownerPlayer, Arrays.asList(powerUpToUse));
                        sendInfo(EventType.MSG_USE_POWERUP, messageUsePowerUp, getHostNameCreateList().addAllHostName());

                        String updateMap =getCreateJson().createMapLMJson();
                        sendInfo(EventType.UPDATE_MAP, updateMap, getHostNameCreateList().addAllHostName());

                        String updateMyPowerUpsJson = getCreateJson().createMyPowerUpsLMJson(ownerPlayer);
                        sendInfo(EventType.UPDATE_MY_POWERUPS, updateMyPowerUpsJson, getHostNameCreateList().addOneHostName(ownerPlayer));

                        String mess = "";
                        sendInfo(EventType.MSG_BEFORE_ENEMY_ACTION_OR_RELOAD, mess, getHostNameCreateList().addAllExceptOneHostName(ownerPlayer));

                        msgActionLeft(ownerPlayer);
                    }
                    else {
                        String messageError = "ERROR: qualcosa è andato storto.";
                        sendInfo(EventType.ERROR, messageError, getHostNameCreateList().addOneHostName(ownerPlayer));
                    }
                }
            }
        }
    }


    //done
    private void useTargetingScope(String messageJson) {
        TargetingScopeRequest targetingScopeRequest = new Gson().fromJson(messageJson, TargetingScopeRequest.class);
        ownerPlayer = convertPlayer(targetingScopeRequest.getIdPlayer(), targetingScopeRequest.getHostNamePlayer());
        targetPlayer = convertPlayer(targetingScopeRequest.getIdTarget(), targetingScopeRequest.getHostNamePlayer());

        if(ownerPlayer != null && targetPlayer != null) {
            if(checkCurrentPlayer(ownerPlayer)) {
                if(checkTargetDamaged()) {
                    if(targetingScopeRequest.getAmmoToSpend() != null) {
                        ammoToSpend = targetingScopeRequest.getAmmoToSpend();
                        powerUpToUse.usePowerUpEffect(targetingScopeRequest.getAmmoToSpend(), null, targetPlayer, ownerPlayer, null);
                    }
                    else if(targetingScopeRequest.getIdPowerUpToSpend() >= 0) {
                        PowerUp powerUpToDiscard = convertPowerUp(ownerPlayer, targetingScopeRequest.getIdPowerUp());

                        if(powerUpToDiscard != null) {
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

                    if(targetingScopeRequest.getAmmoToSpend() != null) {
                        String updatePlayerJson = getCreateJson().createPlayerLMJson(ownerPlayer);
                        sendInfo(EventType.UPDATE_PLAYER_INFO, updatePlayerJson, getHostNameCreateList().addAllHostName());
                    }

                    String updatePowerUpJson = getCreateJson().createMyPowerUpsLMJson(ownerPlayer);
                    sendInfo(EventType.UPDATE_MY_POWERUPS, updatePowerUpJson, getHostNameCreateList().addOneHostName(ownerPlayer));

                    String mess = "";
                    sendInfo(EventType.MSG_BEFORE_ENEMY_ACTION_OR_RELOAD, mess, getHostNameCreateList().addAllExceptOneHostName(ownerPlayer));

                    msgActionLeft(ownerPlayer);
                }
            }
        }
    }



    private boolean checkTargetDamaged() {
        List<Player> playersDamaged = new ArrayList<>();

        for(int i = 0; i < getTurnManager().getGameTable().getPlayers().length; i++) {
            playersDamaged.add(convertPlayerWithId(i));
            int size = playersDamaged.get(i).getDamageLine().size();
            if(playersDamaged.get(i).getDamageLine().get(size).equals(targetPlayer.getCharaName()))
                return true;
        }
        return false;
    }



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

    //Metodi ausiliari per la generazione
    private List<Square> getNorthDirection(Player fromPlayer){
        List<Square> northDirection = new ArrayList<>();
        Square northSquare;

        if(!fromPlayer.getPosition().getIsBlockedAtNorth()) {
            northSquare = fromPlayer.getPosition().getNorthSquare();
            northDirection.add(fromPlayer.getPosition().getNorthSquare());

            if(!northSquare.getIsBlockedAtNorth()) {
                northDirection.add(northSquare.getNorthSquare());
            }
        }
        return northDirection;
    }



    private List<Square> getSouthDirection(Player fromPlayer) {
        List<Square> southDirection = new ArrayList<>();
        Square southSquare;

        if(!fromPlayer.getPosition().getIsBlockedAtNorth()) {
            southSquare = fromPlayer.getPosition().getNorthSquare();
            southDirection.add(fromPlayer.getPosition().getNorthSquare());

            if(!southSquare.getIsBlockedAtNorth()) {
                southDirection.add(southSquare.getNorthSquare());
            }
        }
        return southDirection;
    }


    private List<Square> getEastDirection(Player fromPlayer) {
        List<Square> eastDirection = new ArrayList<>();
        Square eastSquare;

        if(!fromPlayer.getPosition().getIsBlockedAtNorth()) {
            eastSquare = fromPlayer.getPosition().getNorthSquare();
            eastDirection.add(fromPlayer.getPosition().getNorthSquare());

            if(!eastSquare.getIsBlockedAtNorth()) {
                eastDirection.add(eastSquare.getNorthSquare());
            }
        }
        return eastDirection;
    }


    private List<Square> getWestDirection(Player fromPlayer) {
        List<Square> westDirection = new ArrayList<>();
        Square westSquare;

        if(!fromPlayer.getPosition().getIsBlockedAtNorth()) {
            westSquare = fromPlayer.getPosition().getNorthSquare();
            westDirection.add(fromPlayer.getPosition().getNorthSquare());

            if(!westSquare.getIsBlockedAtNorth()) {
                westDirection.add(westSquare.getNorthSquare());
            }
        }
        return westDirection;
    }



    private String createMessageTargetingScope() {
        MessageTargetingScope messageTargetingScope = new MessageTargetingScope(ownerPlayer.getIdPlayer(), ownerPlayer.getCharaName(), ammoToSpend, targetPlayer.getCharaName());
        return new Gson().toJson(messageTargetingScope);
    }


    private String createMessageTagBackGrenade() {
        MessageTagBackGrenade messageTagBackGrenade = new MessageTagBackGrenade(ownerPlayer.getIdPlayer(), ownerPlayer.getCharaName(), targetPlayer.getCharaName());
        return new Gson().toJson(messageTagBackGrenade);
    }
}
