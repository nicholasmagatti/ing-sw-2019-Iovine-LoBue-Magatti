package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.LightModel.*;
import it.polimi.ProgettoIngSW2019.common.Message.toView.*;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.custom_exception.IllegalAttributeException;
import it.polimi.ProgettoIngSW2019.model.*;

import java.util.ArrayList;
import java.util.List;

public class CreateJson {
    private TurnManager turnManager;


    /**
     * constructor
     * @param turnManager   turnManager
     */
    public CreateJson(TurnManager turnManager) {
        this.turnManager = turnManager;
    }



//CLASSES LM
    /**
     * Creates a WeaponLM
     * @param weaponCard    weaponCard to convert in weaponLM
     * @return              the converted WeaponLM card
     */
    //tested
    WeaponLM createWeaponLM(WeaponCard weaponCard) {
        if(weaponCard == null)
            throw new NullPointerException("weapon card cannot be null");

        int[] ammoCostReload = new int[AmmoType.contEnum()];
        int[] ammoCostBuy = new int[AmmoType.contEnum()];

        if(!weaponCard.getreloadCost().isEmpty()) {
            for (AmmoType ammoType : weaponCard.getreloadCost()) {
                if (ammoType == AmmoType.RED)
                    ammoCostReload[GeneralInfo.RED_ROOM_ID]++;

                if (ammoType == AmmoType.BLUE)
                    ammoCostReload[GeneralInfo.BLUE_ROOM_ID]++;

                if (ammoType == AmmoType.YELLOW)
                    ammoCostReload[GeneralInfo.YELLOW_ROOM_ID]++;
            }
        }

        if(!weaponCard.getBuyCost().isEmpty()) {
            for (AmmoType ammoType : weaponCard.getBuyCost()) {
                if (ammoType == AmmoType.RED)
                    ammoCostBuy[GeneralInfo.RED_ROOM_ID]++;

                if (ammoType == AmmoType.BLUE)
                    ammoCostBuy[GeneralInfo.BLUE_ROOM_ID]++;

                if (ammoType == AmmoType.YELLOW)
                    ammoCostBuy[GeneralInfo.YELLOW_ROOM_ID]++;
            }
        }

        return new WeaponLM(weaponCard.getIdCard(), weaponCard.getName(), weaponCard.getDescription(), ammoCostReload, ammoCostBuy);
    }


    /**
     * Creates a list of WeaponLM
     * @param weaponsList       list of WeaponCard to convert
     * @return                  list of WeaponLM converted
     */
    //tested
    List<WeaponLM> createWeaponsListLM(List<WeaponCard> weaponsList) {
        if(weaponsList == null)
            throw new NullPointerException("WeaponList cannot be null");

        List<WeaponLM> weaponsListLM = new ArrayList<>();

        for(WeaponCard weaponCard: weaponsList) {
            WeaponLM weaponLM = createWeaponLM(weaponCard);
            weaponsListLM.add(weaponLM);
        }

        return weaponsListLM;
    }


    /**
     * creates PlayerDataLM from constructor
     * needs to convert the unloadedWeapons in LM
     * @param player    player to convert
     * @return          player converted
     */
    //tested
    PlayerDataLM createPlayerLM(Player player) {
        if(player == null)
            throw new NullPointerException("Player cannot be null");

        List<WeaponLM> unloadedWeaponsListLM = createWeaponsListLM(player.getUnloadedWeapons());
        return new PlayerDataLM(player.getIdPlayer(), player.getCharaName(), unloadedWeaponsListLM, player.getRedAmmo(), player.getBlueAmmo(), player.getYellowAmmo(), player.getNumberOfSkulls(), player.isActive(), player.isPlayerDown(), player.getDamageLine(), player.getMarkLine(), player.getLoadedWeapons().size(), player.getNumberOfPoweUps());
    }


    /**
     * Creates Json of PlayerDataLM
     * @param player       player to convert
     * @return             json of PlayerDataLm
     */
    //tested
    String createPlayerLMJson(Player player) {
        if(player == null)
            throw new NullPointerException("Player cannot be null");

        PlayerDataLM playerDataLM = createPlayerLM(player);
        return new Gson().toJson(playerDataLM);
    }



    /**
     * Creates Json of MyLoadedWeaponsLM
     * @param player        player to convert
     * @return              list of loaded Weapons LM
     */
    //tested
    MyLoadedWeaponsLM createMyLoadedWeaponsListLM(Player player) {
        if(player == null)
            throw new NullPointerException("Player cannot be null");

        List<WeaponLM> loadedWeaponsListLM = createWeaponsListLM(player.getLoadedWeapons());
        return new MyLoadedWeaponsLM(loadedWeaponsListLM);
    }



    /**
     * Creates Json of MyLoadedWeaponsLM
     * @param player        player to convert
     * @return              json of list of loaded Weapons
     */
    //tested
    String createMyLoadedWeaponsListLMJson(Player player) {
        if(player == null)
            throw new NullPointerException("Player cannot be null");

        MyLoadedWeaponsLM myLoadedWeaponsLM = createMyLoadedWeaponsListLM(player);
        return new Gson().toJson(myLoadedWeaponsLM);
    }



    /**
     * Creates a PowerUpLM
     * @param powerUp   powerUp card to convert
     * @return          PowerUpLm converted
     */
    //tested
    PowerUpLM createPowerUpLM(PowerUp powerUp) {
        if(powerUp == null)
            throw new NullPointerException("PowerUp card cannot be null");

        return new PowerUpLM(powerUp.getIdCard(), powerUp.getName(), powerUp.getDescription(), powerUp.getGainAmmoColor());
    }


    /**
     * Creates list of PowerUpLM
     * @param powerUps      list powerUps cards
     * @return              PowerUpLM list converted
     */
    //tested
    List<PowerUpLM> createPowerUpsListLM(List<PowerUp> powerUps) {
        if(powerUps == null)
            throw new NullPointerException("PowerUps cannot be null");

        List<PowerUpLM> powerUpsListLM = new ArrayList<>();

        for(PowerUp powerUp: powerUps) {
            PowerUpLM powerUpLM = createPowerUpLM(powerUp);
            powerUpsListLM.add(powerUpLM);
        }

        return powerUpsListLM;
    }


    /**
     * Creates MyPowerUpsLM
     * @param player        player
     * @return              myPowerUpsLM
     */
    //tested
    MyPowerUpLM createMyPowerUpsLM(Player player) {
        if(player == null)
            throw new NullPointerException("Player cannot be null");

        List<PowerUpLM> powerUpsLM = createPowerUpsListLM(player.getPowerUps());
        return new MyPowerUpLM(powerUpsLM);
    }



    /**
     * Creates MyPowerUpsLM json
     * @param player        player
     * @return              myPowerUpsLM json
     */
    //tested
    String createMyPowerUpsLMJson(Player player) {
        if(player == null)
            throw new NullPointerException("Player cannot be null");

        MyPowerUpLM myPowerUpLM= createMyPowerUpsLM(player);
        return new Gson().toJson(myPowerUpLM);
    }


    /**
     * create Ammo card LM
     * @param ammoCard      ammo card
     * @return              ammo card LM
     */
    //tested
    AmmoCardLM createAmmoCardLM(AmmoCard ammoCard) {
        if(ammoCard == null)
            throw new NullPointerException("AmmoCard card cannot be null");

        int nAmmo = ammoCard.getAmmo().size();
        if(nAmmo == 2)
            return new AmmoCardLM(ammoCard.getIdCard(), ammoCard.getAmmo().get(0), ammoCard.getAmmo().get(1));
        else
            return new AmmoCardLM(ammoCard.getIdCard(), ammoCard.getAmmo().get(0), ammoCard.getAmmo().get(1), ammoCard.getAmmo().get(2));
    }



    /**
     * Create the map LM
     * @return  mapLM
     */
    MapLM createMapLM() {
        Square[][] map = turnManager.getGameTable().getMap();
        SquareLM[][] mapLM = new SquareLM[GeneralInfo.ROWS_MAP][GeneralInfo.COLUMNS_MAP];
        List<Integer> idPlayers = new ArrayList<>();


        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[i].length; j++) {

                if(map[i][j] != null) {
                    for(Player player: map[i][j].getPlayerOnSquare()) {
                        idPlayers.clear();
                        idPlayers.add(player.getIdPlayer());
                    }

                    if(map[i][j] instanceof SpawningPoint) {
                        List<WeaponCard> weaponCardsList = ((SpawningPoint) map[i][j]).getWeaponCards();

                        if(weaponCardsList == null) {
                            List<WeaponCard> weaponCardListEmpty = new ArrayList<>();
                            mapLM[i][j] = new SpawnPointLM(idPlayers, createWeaponsListLM(weaponCardListEmpty), map[i][j].getIsBlockedAtNorth(), map[i][j].getIsBlockedAtEast(), map[i][j].getIsBlockedAtSouth(), map[i][j].getIsBlockedAtWest(), map[i][j].getIdRoom());
                        }
                        else {
                            mapLM[i][j] = new SpawnPointLM(idPlayers, createWeaponsListLM(weaponCardsList), map[i][j].getIsBlockedAtNorth(), map[i][j].getIsBlockedAtEast(), map[i][j].getIsBlockedAtSouth(), map[i][j].getIsBlockedAtWest(), map[i][j].getIdRoom());
                        }
                    }

                    if(map[i][j] instanceof AmmoPoint) {
                        AmmoCard ammoCard = ((AmmoPoint) map[i][j]).getAmmoCard();
                        if(ammoCard!= null) {
                            mapLM[i][j] = new AmmoPointLM(idPlayers, createAmmoCardLM(ammoCard), map[i][j].getIsBlockedAtNorth(), map[i][j].getIsBlockedAtEast(), map[i][j].getIsBlockedAtSouth(), map[i][j].getIsBlockedAtWest(), map[i][j].getIdRoom());
                        }
                        else {
                            mapLM[i][j] = new AmmoPointLM(idPlayers, null, map[i][j].getIsBlockedAtNorth(), map[i][j].getIsBlockedAtEast(), map[i][j].getIsBlockedAtSouth(), map[i][j].getIsBlockedAtWest(), map[i][j].getIdRoom());
                        }
                    }
                }
                else {
                    mapLM[i][j] = null;
                }
            }
        }
        return new MapLM(mapLM);
    }


    /**
     *
     * @return mapLM json
     */
    String createMapLMJson() {
        return new Gson().toJson(createMapLM());
    }


    /**
     * create the killShotTrackLM
     * @return  killShotTrackLM
     */
    //tested
    KillshotTrackLM createKillShotTrackLM() {
        int nSkulls = turnManager.getGameTable().initialNumberOfSkulls();
        List<KillToken> killShotTrack = turnManager.getGameTable().getKillshotTrack();

        return new KillshotTrackLM(killShotTrack, nSkulls);
    }


    /**
     * @return  json killShotTrackLM
     */
    //tested
    String createKillShotTrackLMJson() {
        return new Gson().toJson(createKillShotTrackLM());
    }





//CLASSES TO VIEW
    /**
     * create DrawCardsInfoResponse json
     * @param player        spawn player
     * @return              json
     */
    //tested
    String createDrawCardsInfoJson(Player player, List<PowerUp> powerUps) {
        if(player == null)
            throw new NullPointerException("Player cannot be null");

        if(powerUps == null)
            throw new NullPointerException("powerUps cannot be null");

        DrawCardsInfoResponse drawCardsInfoResponse = new DrawCardsInfoResponse(player.getIdPlayer(), createPowerUpsListLM(powerUps));
        return new Gson().toJson(drawCardsInfoResponse);
    }

//todo
    String createWeaponsToPayJson(Player player, List<WeaponCard> weaponCards, List<PayAmmoList> payment) {
        if(player == null)
            throw new NullPointerException("Player cannot be null");

        if(weaponCards == null)
            throw new NullPointerException("Weapons cards cannot be null");

        if(payment == null)
            throw new NullPointerException("payment cannot be null");

        List<WeaponLM> weaponLMs = createWeaponsListLM(weaponCards);
        return new Gson().toJson(new WeaponsCanPayResponse(player.getIdPlayer(), weaponLMs, payment));
    }


    /**
     * Message to all player with the powerUps discarded by a player
     * @param player        player who discarded powerUps
     * @param powerUps      list of powerUps discarded
     * @return              message MessagePowerUpDiscarded in json format
     */
    //tested
    String createMessagePowerUpsDiscardedJson(Player player, List<PowerUp> powerUps) {
        if(player == null)
            throw new NullPointerException("Player cannot be null");

        if(powerUps == null)
            throw new NullPointerException("PowerUps cards cannot be null");

        List<PowerUpLM> powerUpLMS = createPowerUpsListLM(powerUps);
        return new Gson().toJson(new MessagePowerUpsDiscarded(player.getIdPlayer(), player.getCharaName(), powerUpLMS));
    }


    /**
     * Creates GrabWeaponSwap json
     * @param weaponsList   weapons list to convert
     * @param player        player
     * @return              json of GrabWeaponSwap
     */
    //tested
    String createGrabWeaponSwapJson(Player player, List<WeaponCard> weaponsList) {
        if(player == null)
            throw new NullPointerException("Player cannot be null");

        if(weaponsList == null)
            throw new NullPointerException("WeaponList cannot be null");

        List<WeaponLM> weaponsListLM = createWeaponsListLM(weaponsList);
        GrabWeaponSwap grabWeaponSwap = new GrabWeaponSwap(player.getIdPlayer(), weaponsListLM);
        return new Gson().toJson(grabWeaponSwap);
    }



//MESSAGES TO VIEW
    /**
     * Create MessageMyPowerUp json
     * message with my draw powerUps
     * @param player        player draw powerUp
     * @param powerUps      list of draw powerUps
     * @return              json message
     */
    //tested
    String createMessageDrawMyPowerUpJson(Player player, List<PowerUp> powerUps) {
        if(player == null)
            throw new NullPointerException("Player cannot be null");

        if(powerUps == null)
            throw new NullPointerException("PowerUps cannot be null");

        List<String> namePowerUps = new ArrayList<>();
        int[] idPowerUps = new int[2];

        int i = 0;

        //this control can be changed
        if(powerUps.size() > 2)
            throw new IllegalArgumentException("The player cannot draw more than 2 powerUps");

        for(PowerUp p: powerUps) {
            namePowerUps.add(p.getName());
            idPowerUps[i] = p.getIdCard();
            i++;
        }

        MessageDrawMyPowerUp messageDrawMyPowerUpInfo = new MessageDrawMyPowerUp(player.getIdPlayer(), player.getCharaName(), idPowerUps, namePowerUps);
        return new Gson().toJson(messageDrawMyPowerUpInfo);
    }


    /**
     * Create MessageDrawEnemyPowerUp json
     * @param player        player draw powerUp
     * @param nCards        n cards draw
     * @return              json message
     */
    //tested exception
    String createMessageEnemyDrawPowerUpJson(Player player, int nCards) {
        if(player == null)
            throw new NullPointerException("Player cannot be null");

        if(nCards < 1)
            throw new IllegalArgumentException("nCard must be positive");

        MessageEnemyDrawPowerUp messageEnemyDrawPowerUp = new MessageEnemyDrawPowerUp(player.getIdPlayer(), player.getCharaName(), nCards);
        return new Gson().toJson(messageEnemyDrawPowerUp);
    }


    /**
     * common Message to view
     * @param player    player
     * @return          Message
     */
    //tested exception
    Message createMessage(Player player) {
        if(player == null)
            throw new NullPointerException("player cannot be null");

       return new Message(player.getIdPlayer(), player.getCharaName());
    }


    /**
     * common Message json to view
     * @param player    player
     * @return          message json
     */
    //tested exception
    String createMessageJson(Player player) {
        if(player == null)
            throw new NullPointerException("player cannot be null");

        return new Gson().toJson(createMessage(player));
    }


    /**
     * @param player        player
     * @param weaponPay     weapon to pay
     * @return              message enemy weapon to pay
     */
    String createMessageEnemyWeaponPay(Player player, WeaponCard weaponPay) {
        if(player == null)
            throw new NullPointerException("player cannot be null");

        if(weaponPay == null)
            throw new NullPointerException("weapon cannot be null");

        MessageWeaponPay messageWeaponPay = new MessageWeaponPay(player.getIdPlayer(), player.getCharaName(), weaponPay.getIdCard(), weaponPay.getName());
        return new Gson().toJson(messageWeaponPay);
    }


    /**
     * @param player                player
     * @param weaponDiscarded       weapon to discard
     * @param weaponBuy             weapon to buy
     * @return                      message of the swap weapons in spawning point to all player
     */
    String createMessagePlayerSwapWeapons(Player player, WeaponCard weaponDiscarded, WeaponCard weaponBuy) {
        if(player == null)
            throw new NullPointerException("player cannot be null");

        if(weaponDiscarded == null)
            throw new NullPointerException("weapon cannot be null");

        if(weaponBuy == null)
            throw new NullPointerException("weapon cannot be null");

        MessagePlayerSwapWeapons messagePlayerSwapWeapons = new MessagePlayerSwapWeapons(player.getIdPlayer(), player.getCharaName(), weaponBuy.getIdCard(), weaponBuy.getName(), weaponDiscarded.getIdCard(), weaponDiscarded.getName());
        return new Gson().toJson(messagePlayerSwapWeapons);
    }


    /**
     * @param player        player
     * @return              Message with number of actions left
     */
    MessageActionLeft createMessageActionsLeft(Player player, List<PowerUp> powerUpsCanUse) {
        if(player == null)
            throw new NullPointerException("player cannot be null");

        if(player.getIdPlayer() != turnManager.getCurrentPlayer().getIdPlayer())
            throw new IllegalAttributeException("This player is not the current one");

        if(powerUpsCanUse == null)
            throw new NullPointerException("powerUpsCanUse cannot be null");

        List<PowerUpLM> powerUpsCanUseLM = createPowerUpsListLM(powerUpsCanUse);

        return new MessageActionLeft(player.getIdPlayer(), player.getCharaName(), turnManager.getActionsLeft(), powerUpsCanUseLM);
    }


    /**
     * @param player    player
     * @return          Message with number of actions left json
     */
    String createMessageActionsLeftJson(Player player, List<PowerUp> powerUpsCanUse) {
        if(player == null)
            throw new NullPointerException("player cannot be null");

        return new Gson().toJson(createMessageActionsLeft(player, powerUpsCanUse));
    }
}
