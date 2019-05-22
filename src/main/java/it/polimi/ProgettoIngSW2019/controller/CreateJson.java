package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.LightModel.*;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.PowerUp;
import it.polimi.ProgettoIngSW2019.model.TurnManager;
import it.polimi.ProgettoIngSW2019.model.WeaponCard;

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



    /**
     * Creates a WeaponLM
     * @param weaponCard    weaponCard to convert in weaponLM
     * @return              the converted WeaponLM card
     */
    public WeaponLM createWeaponLM(WeaponCard weaponCard) {
        return new WeaponLM(weaponCard.getIdCard(), weaponCard.getName(), weaponCard.getDescription());
    }


    /**
     * Creates JsonFile of WeaponLM
     * @param weaponCard    weaponCard to convert in WeaponLM
     * @return              json of WeaponLM
     */
    public String createWeaponLMJson(WeaponCard weaponCard) {
        WeaponLM weaponLM = new WeaponLM(weaponCard.getIdCard(), weaponCard.getName(), weaponCard.getDescription());
        return new Gson().toJson(weaponLM);
    }



    /**
     * Creates a list of WeaponLM
     * @param weaponsList       list of WeaponCard to convert
     * @return                  list of WeaponLM converted
     */
    public List<WeaponLM> createWeaponsListLM(List<WeaponCard> weaponsList) {
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
        List<WeaponLM> weaponsListLM = new ArrayList<>();

        for(WeaponCard weaponCard: weaponsList) {
            WeaponLM weaponLM = createWeaponLM(weaponCard);
            weaponsListLM.add(weaponLM);
        }

        return new Gson().toJson(weaponsListLM);
    }



    /**
     * creates PlayerDataLM from constructor
     * needs to convert the unloadedWeapons in LM
     * @param player    player to convert
     * @return          player converted
     */
    public PlayerDataLM createPlayerLM(Player player) {
        List<WeaponLM> unloadedWeaponsListLM = createWeaponsListLM(player.getUnloadedWeapons());
        return new PlayerDataLM(player.getIdPlayer(), player.getCharaName(), unloadedWeaponsListLM, player.getRedAmmo(), player.getBlueAmmo(), player.getYellowAmmo(), player.getNumberOfSkulls(), player.isActive(), player.isPlayerDown(), player.getDamageLine(), player.getMarkLine());
    }


    /**
     * Creates Json of PlayerDataLM
     * @param player       player to convert
     * @return             json of PlayerDataLm
     */
    public String createPlayerLMJson(Player player) {
        List<WeaponLM> unloadedWeaponsListLM = createWeaponsListLM(player.getUnloadedWeapons());
        PlayerDataLM playerDataLM = new PlayerDataLM(player.getIdPlayer(), player.getCharaName(), unloadedWeaponsListLM, player.getRedAmmo(), player.getBlueAmmo(), player.getYellowAmmo(), player.getNumberOfSkulls(), player.isActive(), player.isPlayerDown(), player.getDamageLine(), player.getMarkLine());
        return new Gson().toJson(player);
    }



    /**
     * Creates MyLoadedWeaponsLM from the list of cards
     * @param player    player
     * @return          loadedWeapons converted in MyLoadedWeaponsLM
     */
    public MyLoadedWeaponsLM createMyLoadedWeaponsListLM(Player player) {
        List<WeaponLM> loadedWeaponsListLM = createWeaponsListLM(player.getLoadedWeapons());
        return new MyLoadedWeaponsLM(loadedWeaponsListLM);
    }


    /**
     * Creates Json of MyLoadedWeaponsLM
     * @param player        player to convert
     * @return              json of list of loaded Weapons
     */
    public String createMyLoadedWeaponsListLMJson(Player player) {
        List<WeaponLM> loadedWeaponsListLM = createWeaponsListLM(player.getLoadedWeapons());
        MyLoadedWeaponsLM myLoadedWeaponsLM = new MyLoadedWeaponsLM(loadedWeaponsListLM);
        return new Gson().toJson(myLoadedWeaponsLM);
    }



    /**
     * Creates a PowerUpLM
     * @param powerUp   powerUp card to convert
     * @return          PowerUpLm converted
     */
    public PowerUpLM createPowerUpLM(PowerUp powerUp) {
        return new PowerUpLM(powerUp.getIdCard(), powerUp.getName(), powerUp.getDescription(), powerUp.getGainAmmoColor());
    }


    /**
     * Creates Json of PowerUpLM
     * @param powerUp       powerUp to convert
     * @return              powerUpLM json
     */
    public String createPowerUpLMJson(PowerUp powerUp) {
        PowerUpLM powerUpLM = new PowerUpLM(powerUp.getIdCard(), powerUp.getName(), powerUp.getDescription(), powerUp.getGainAmmoColor());
        return new Gson().toJson(powerUpLM);
    }



    /**
     * Creates list of PowerUpLM
     * @param powerUps      list powerUps cards
     * @return              PowerUpLM list converted
     */
    public List<PowerUpLM> createPowerUpsListLM(List<PowerUp> powerUps) {
        List<PowerUpLM> powerUpsListLM = new ArrayList<>();

        for(PowerUp powerUp: powerUps) {
            PowerUpLM powerUpLM = createPowerUpLM(powerUp);
            powerUpsListLM.add(powerUpLM);
        }

        return powerUpsListLM;
    }


    public String createPowerUpsListLMJson(List<PowerUp> powerUps) {
        List<PowerUpLM> powerUpsListLM = new ArrayList<>();

        for(PowerUp powerUp: powerUps) {
            PowerUpLM powerUpLM = createPowerUpLM(powerUp);
            powerUpsListLM.add(powerUpLM);
        }

        return new Gson().toJson(powerUpsListLM);
    }



    /**
     * Creates MyPowerUpsLM
     * @param player        player
     * @return              myPowerUpsLM json
     */
    public String createMyPowerUpsListLMJson(Player player) {
        List<PowerUpLM> powerUpsLM = createPowerUpsListLM(player.getPowerUps());
        MyPowerUpLM myPowerUpLM = new MyPowerUpLM(powerUpsLM);
        return new Gson().toJson(myPowerUpLM);
    }

}
