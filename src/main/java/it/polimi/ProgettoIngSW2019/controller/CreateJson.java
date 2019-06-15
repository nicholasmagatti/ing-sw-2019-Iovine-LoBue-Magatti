package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.LightModel.*;
import it.polimi.ProgettoIngSW2019.common.Message.toView.*;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
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
    public WeaponLM createWeaponLM(WeaponCard weaponCard) {
        if(weaponCard == null)
            throw new NullPointerException("weapon card cannot be null");

        int[] ammoCostReload = new int[AmmoType.contEnum()];
        int[] ammoCostBuy = new int[AmmoType.contEnum()];

        int blue = 0;
        int yellow = 0;
        int red = 0;

        if(!weaponCard.getreloadCost().isEmpty()) {
            for (AmmoType ammoType : weaponCard.getreloadCost()) {
                if (ammoType == AmmoType.RED)
                    ammoCostReload[AmmoType.intFromAmmoType(AmmoType.RED)] = red++;

                if (ammoType == AmmoType.BLUE)
                    ammoCostReload[AmmoType.intFromAmmoType(AmmoType.BLUE)] = blue;

                if (ammoType == AmmoType.YELLOW)
                    ammoCostReload[AmmoType.intFromAmmoType(AmmoType.YELLOW)] = yellow;
            }
        }

        if(!weaponCard.getBuyCost().isEmpty()) {
            for (AmmoType ammoType : weaponCard.getBuyCost()) {
                if (ammoType == AmmoType.RED)
                    ammoCostBuy[AmmoType.intFromAmmoType(AmmoType.RED)] = red++;

                if (ammoType == AmmoType.BLUE)
                    ammoCostBuy[AmmoType.intFromAmmoType(AmmoType.BLUE)] = blue;

                if (ammoType == AmmoType.YELLOW)
                    ammoCostBuy[AmmoType.intFromAmmoType(AmmoType.YELLOW)] = yellow;
            }
        }

        return new WeaponLM(weaponCard.getIdCard(), weaponCard.getName(), weaponCard.getDescription(), ammoCostReload, ammoCostBuy);
    }


    /**
     * Creates JsonFile of WeaponLM
     * @param weaponCard    weaponCard to convert in WeaponLM
     * @return              json of WeaponLM
     */
    public String createWeaponLMJson(WeaponCard weaponCard) {
        if(weaponCard == null)
            throw new NullPointerException("weapon card cannot be null");

        WeaponLM weaponLM = createWeaponLM(weaponCard);
        return new Gson().toJson(weaponLM);
    }



    /**
     * Creates a list of WeaponLM
     * @param weaponsList       list of WeaponCard to convert
     * @return                  list of WeaponLM converted
     */
    public List<WeaponLM> createWeaponsListLM(List<WeaponCard> weaponsList) {
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
     * Creates Json of List of WeaponLM
     * @param weaponsList   weapons list to convert
     * @return              json of WeaponsLM list
     */
    public String createWeaponsListLMJson(List<WeaponCard> weaponsList) {
        if(weaponsList == null)
            throw new NullPointerException("WeaponList cannot be null");

        List<WeaponLM> weaponsListLM = createWeaponsListLM(weaponsList);
        return new Gson().toJson(weaponsListLM);
    }



    /**
     * creates PlayerDataLM from constructor
     * needs to convert the unloadedWeapons in LM
     * @param player    player to convert
     * @return          player converted
     */
    public PlayerDataLM createPlayerLM(Player player) {
        if(player == null)
            throw new NullPointerException("Player cannot be null");

        List<WeaponLM> unloadedWeaponsListLM = createWeaponsListLM(player.getUnloadedWeapons());
        return new PlayerDataLM(player.getIdPlayer(), player.getCharaName(), unloadedWeaponsListLM, player.getRedAmmo(), player.getBlueAmmo(), player.getYellowAmmo(), player.getNumberOfSkulls(), player.isActive(), player.isPlayerDown(), player.getDamageLine(), player.getMarkLine());
    }


    /**
     * Creates Json of PlayerDataLM
     * @param player       player to convert
     * @return             json of PlayerDataLm
     */
    public String createPlayerLMJson(Player player) {
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
    public MyLoadedWeaponsLM createMyLoadedWeaponsListLM(Player player) {
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
    public String createMyLoadedWeaponsListLMJson(Player player) {
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
    public PowerUpLM createPowerUpLM(PowerUp powerUp) {
        if(powerUp == null)
            throw new NullPointerException("PowerUp card cannot be null");

        return new PowerUpLM(powerUp.getIdCard(), powerUp.getName(), powerUp.getDescription(), powerUp.getGainAmmoColor());
    }


    /**
     * Creates Json of PowerUpLM
     * @param powerUp       powerUp to convert
     * @return              powerUpLM json
     */
    public String createPowerUpLMJson(PowerUp powerUp) {
        if(powerUp == null)
            throw new NullPointerException("PowerUp card cannot be null");

        PowerUpLM powerUpLM = createPowerUpLM(powerUp);
        return new Gson().toJson(powerUpLM);
    }



    /**
     * Creates list of PowerUpLM
     * @param powerUps      list powerUps cards
     * @return              PowerUpLM list converted
     */
    public List<PowerUpLM> createPowerUpsListLM(List<PowerUp> powerUps) {
        if(powerUps == null)
            throw new NullPointerException("PowerUps cannot be null");

        List<PowerUpLM> powerUpsListLM = new ArrayList<>();

        for(PowerUp powerUp: powerUps) {
            PowerUpLM powerUpLM = createPowerUpLM(powerUp);
            powerUpsListLM.add(powerUpLM);
        }

        return powerUpsListLM;
    }


    public String createPowerUpsListLMJson(List<PowerUp> powerUps) {
        if(powerUps == null)
            throw new NullPointerException("PowerUps cannot be null");

        List<PowerUpLM> powerUpsListLM = createPowerUpsListLM(powerUps);
        return new Gson().toJson(powerUpsListLM);
    }


    /**
     * Creates MyPowerUpsLM
     * @param player        player
     * @return              myPowerUpsLM
     */
    public MyPowerUpLM createMyPowerUpsLM(Player player) {
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
    public String createMyPowerUpsLMJson(Player player) {
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
    public AmmoCardLM createAmmoCardLM(AmmoCard ammoCard) {
        if(ammoCard == null)
            throw new NullPointerException("AmmoCard card cannot be null");

        int nAmmo = ammoCard.getAmmo().size();
        if(nAmmo == 2)
            return new AmmoCardLM(ammoCard.getIdCard(), ammoCard.getAmmo().get(0), ammoCard.getAmmo().get(1));
        else
            return new AmmoCardLM(ammoCard.getIdCard(), ammoCard.getAmmo().get(0), ammoCard.getAmmo().get(1), ammoCard.getAmmo().get(2));
    }



    /**
     * create Ammo card LM json
     * @param ammoCard      ammo card
     * @return              json ammo card LM
     */
    public String createAmmoCardLMJson(AmmoCard ammoCard) {
        if(ammoCard == null)
            throw new NullPointerException("AmmoCard card cannot be null");

        AmmoCardLM ammoCardLM = createAmmoCardLM(ammoCard);
        return new Gson().toJson(ammoCardLM);
    }



    /**
     * Create the map LM
     * @return  mapLM
     */
    public MapLM createMapLM() {
        Square[][] map = turnManager.getGameTable().getMap();
        SquareLM[][] mapLM = new SquareLM[GeneralInfo.ROWS_MAP][GeneralInfo.COLUMNS_MAP];
        List<Integer> idPlayers = new ArrayList<>();

        int i = 0;
        int j = 0;

        for(Square[] squareLine: map) {
            for(Square square: squareLine) {

                if(square != null) {
                    for(Player player: square.getPlayerOnSquare()) {
                        idPlayers.add(player.getIdPlayer());
                    }

                    if(square instanceof SpawningPoint) {
                        List<WeaponCard> weaponCardsList = ((SpawningPoint) square).getWeaponCards();
                        mapLM[i][j] = new SpawnPointLM(idPlayers, createWeaponsListLM(weaponCardsList), square.getIsBlockedAtNorth(), square.getIsBlockedAtEast(), square.getIsBlockedAtSouth(), square.getIsBlockedAtWest(), square.getIdRoom());
                    }

                    if(square instanceof AmmoPoint) {
                        AmmoCard ammoCard = ((AmmoPoint) square).getAmmoCard();
                        mapLM[i][j] = new AmmoPointLM(idPlayers, createAmmoCardLM(ammoCard), square.getIsBlockedAtNorth(), square.getIsBlockedAtEast(), square.getIsBlockedAtSouth(), square.getIsBlockedAtWest(), square.getIdRoom());
                    }
                }
                else {
                    mapLM[i][j] = null;
                }
                j++;
            }
            i++;
        }
        return new MapLM(mapLM);
    }


    /**
     *
     * @return mapLM json
     */
    public String createMapLMLMJson() {
        return new Gson().toJson(createMapLM());
    }


    /**
     * create the killShotTrackLM
     * @return  killShotTrackLM
     */
    public KillshotTrackLM createKillShotTrackLM() {
        int nSkulls = turnManager.getGameTable().initialNumberOfSkulls();
        List<KillToken> killShotTrack = turnManager.getGameTable().getKillshotTrack();

        return new KillshotTrackLM(killShotTrack, nSkulls);
    }


    /**
     *
     * @return  json killShotTrackLM
     */
    public String createKillShotTrackLMJson() {
        return new Gson().toJson(createKillShotTrackLM());
    }


/*
    public SpawnPointLM createSpawnPointLM(SpawningPoint spawningPoint) {
        if(spawningPoint == null)
            throw new NullPointerException();

        List<Integer> idPlayers

        return new SpawnPointLM(spawningPoint.getPlayerOnSquare())
    }
*/







//CLASSES TO VIEW
    /**
     * create DrawCardsInfoResponse json
     * @param player        spawn player
     * @return              json
     */
    public String createDrawCardsInfoJson(Player player) {
        if(player == null)
            throw new NullPointerException("Player cannot be null");

        DrawCardsInfoResponse drawCardsInfoResponse = new DrawCardsInfoResponse(player.getIdPlayer(), createPlayerLM(player), createMyPowerUpsLM(player));
        return new Gson().toJson(drawCardsInfoResponse);
    }


    public String createWeaponsToPayJson(Player player, List<WeaponCard> weaponCards, List<PayAmmoList> payment) {
        if(player == null)
            throw new NullPointerException("Player cannot be null");

        if(weaponCards == null)
            throw new NullPointerException("Weapons cards cannot be null");

        if(payment == null)
            throw new NullPointerException("payment cannot be null");

        List<WeaponLM> weaponLMs = createWeaponsListLM(weaponCards);
        return new Gson().toJson(new WeaponsCanPayResponse(player.getIdPlayer(), weaponLMs, payment));
    }



//MESSAGES TO VIEW
    /**
     * Create MessageMyPowerUp json
     * message with my draw powerUps
     * @param player        player draw powerUp
     * @param powerUps      list of draw powerUps
     * @return              json message
     */
    public String createMessageDrawMyPowerUpJson(Player player, List<PowerUp> powerUps) {
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
    public String createMessageEnemyDrawPowerUpJson(Player player, int nCards) {
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
    public Message createMessage(Player player) {
        if(player == null)
            throw new NullPointerException("player cannot be null");

       return new Message(player.getIdPlayer(), player.getCharaName());
    }


    /**
     * common Message json to view
     * @param player    player
     * @return          message json
     */
    public String createMessageJson(Player player) {
        if(player == null)
            throw new NullPointerException("player cannot be null");

        return new Gson().toJson(createMessage(player));
    }



    public String createMessageEnemyWeaponPay(Player player, WeaponCard weaponPay) {
        if(player == null)
            throw new NullPointerException("player cannot be null");

        if(weaponPay == null)
            throw new NullPointerException("weapon cannot be null");

        MessageWeaponPay messageWeaponPay = new MessageWeaponPay(player.getIdPlayer(), player.getCharaName(), weaponPay.getIdCard(), weaponPay.getName(), createPlayerLM(player));
        return new Gson().toJson(messageWeaponPay);
    }



    public String createMessagePlayerSwapWeapons(Player player, WeaponCard weaponDiscarded, WeaponCard weaponBuy) {
        if(player == null)
            throw new NullPointerException("player cannot be null");

        if(weaponDiscarded == null)
            throw new NullPointerException("weapon cannot be null");

        if(weaponBuy == null)
            throw new NullPointerException("weapon cannot be null");

        MessagePlayerSwapWeapons messagePlayerSwapWeapons = new MessagePlayerSwapWeapons(player.getIdPlayer(), player.getCharaName(), weaponBuy.getIdCard(), weaponBuy.getName(), createPlayerLM(player), weaponDiscarded.getIdCard(), weaponDiscarded.getName());
        return new Gson().toJson(messagePlayerSwapWeapons);
    }
}
