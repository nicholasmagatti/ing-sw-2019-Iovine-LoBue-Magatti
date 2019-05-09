package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.common.message.reloadmsg.*;
//import it.polimi.ProgettoIngSW2019.common.message.ReloadMessage;
import it.polimi.ProgettoIngSW2019.model.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.utilities.Observer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Class ReloadController
 * check reload
 * reload method
 * setUp end turn
 * @author Priscilla Lo Bue
 */
public class ReloadController implements Observer<ReloadMessage> {
    private WeaponCard weaponToReload;
    private Player ownerPlayer;
    private TurnManager turnManager;
    private GameTable gameTable;
    private Deck weaponDeck;
    private Deck ammoDeck;
    private int[] idRooms = new int[3];;


    public void update(ReloadMessage reloadMessage) {

    }


    public WeaponCard getWeaponUsedById(int idWeapon){
        WeaponCard weaponCard;
        weaponCard = ownerPlayer.getUnloadedWeapons().get(idWeapon);

        return weaponCard;
    }



    public Player getUserPlayerById(int idUserPlayer){
        Player userPlayer;
        userPlayer = gameTable.getPlayers()[idUserPlayer];

        return userPlayer;
    }



    public List<WeaponCard> setListWeaponsCanReload() {
        List<WeaponCard> weaponsCanReload = new ArrayList<>();

        //cerco tutte le armi scariche che posso ricaricare, in base agli ammo che ho
       for(WeaponCard weaponCard:ownerPlayer.getUnloadedWeapons()) {
           List<AmmoType> reloadCost = weaponCard.getreloadCost();
           if(ownerPlayer.hasEnoughAmmo(reloadCost))
               weaponsCanReload.add(weaponCard);
       }

       return weaponsCanReload;
    }



    public void reload() {
        List<AmmoType> reloadCost = weaponToReload.getreloadCost();

        if(ownerPlayer.hasEnoughAmmo(reloadCost)) {
            //faccio il reload
            ownerPlayer.reload(weaponToReload);
        }
        else
            throw new IllegalArgumentException("The player: " + ownerPlayer.getCharaName() + "with id: " + ownerPlayer.getIdPlayer() + "cannot pay the reload cost");
    }



    public void scoreDeadPlayers() {
        //current player
        Player currentPlayer = turnManager.getCurrentPlayer();

        //calcolo il punteggio dai danni dei players morti
        turnManager.scoreDamageLineOfAllDeadPlayers();

        //creo lista di players morti
        List<Player> deadPlayers = turnManager.checkDeadPlayers();

        //Sposto ogni player morto in spawnState
        for (Player player: deadPlayers) {

            //aggiungo i player nel killToken
            gameTable.addTokenOnKillshotTrack(player, currentPlayer);

            //aggiungo un teschio(skull) ad ogni player morto
            player.increaseNumberOfSkulls();

            //TODO: passo ogni player in spawnState
        }
    }


    public void scoreDamagedPlayers() {
        //calcolo il punteggio dai danni dei players non morti quando finisce il gioco
        turnManager.scoreDamageLineOfAllPlayersWithDamage();
    }



    public void setIdRooms() {
        //lista di colori delle room che hanno gli SpawningPoint
        List<AmmoType> colors = new ArrayList<>();
        colors = Arrays.asList(AmmoType.YELLOW, AmmoType.BLUE, AmmoType.RED);

        //genero array con tutti gli id delle room con gli spawningPoint
        int i = 0;
        for(AmmoType ammo: colors){
            idRooms[i] = AmmoType.intFromAmmoType(ammo);
            i++;
        }
    }



    public void resetSpawningPoint() {
        Square spawnPos;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                spawnPos = gameTable.getMap()[i][j];
                for(int elem:idRooms) {
                    if((spawnPos.getIdRoom() == elem) && (spawnPos instanceof SpawningPoint))
                        spawnPos.reset(weaponDeck);
                }

            }
        }
    }



    public void resetAmmoPoint() {
        Square ammoPos;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                ammoPos = gameTable.getMap()[i][j];
                if(ammoPos != null)
                    if(ammoPos instanceof  AmmoPoint)
                        ammoPos.reset(ammoDeck);
            }
        }
    }



    public void resetMap() {
        resetAmmoPoint();
        resetSpawningPoint();
    }

}
