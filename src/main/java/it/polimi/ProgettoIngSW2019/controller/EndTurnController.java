package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.enums.SquareType;
import it.polimi.ProgettoIngSW2019.custom_exception.IllegalAttributeException;
import it.polimi.ProgettoIngSW2019.model.*;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.utilities.Observer;


import java.util.ArrayList;
import java.util.List;


/**
 * Class ReloadController
 * check reload
 * reload method
 * setUp end turn
 * @author Priscilla Lo Bue
 */
public class EndTurnController implements Observer<Event> {
    private WeaponCard weaponToReload;
    private Player ownerPlayer;
    private TurnManager turnManager;
    private List<WeaponCard> weaponsCanReload = new ArrayList<>();
    private List<AmmoType> reloadCost = new ArrayList<>();
    private Deck weaponDeck ;
    private Deck ammoDeck;


    public EndTurnController(TurnManager turnManager) {
        this.turnManager = turnManager;
    }



    public void update(Event event) {
        /*
        if(event.getCommand().equals(EventType.REQUEST_TARGET)){
            PlayerDataLM playerData = new Gson().fromJson(event.getDataInJsonFormat(), PlayerDataLM .class);
            String nikcnameList = createEnemyListJson(model.possibleTarget(playerData.getIdWeapon()));
            virtualView.sendMessage(new Event(EventType.RESPONSE_REQUEST_TARGET, nikcnameList));
        }
        */
    }



    //TODO: da togliere
    public WeaponCard getWeaponUsedById(int idWeapon){
        WeaponCard weaponCard;
        weaponCard = ownerPlayer.getUnloadedWeapons().get(idWeapon);

        return weaponCard;
    }



    //TODO: da togliere
    public Player getUserPlayerById(int idUserPlayer){
        Player userPlayer;
        userPlayer = turnManager.getGameTable().getPlayers()[idUserPlayer];

        return userPlayer;
    }


    public void endTurn() {
        if(ownerPlayer.getUnloadedWeapons() != null) {
            //se ha armi scariche
            //setto la lista di armi che il Player può ricaricare
            setListWeaponsCanReload();

            if(weaponsCanReload != null) {
                //TODO: restituisce l'array all'utente e gli fa scegliere l'arma da usare
                //TODO: disabilitare le armi che non posso ricaricare (?)
            }
        }

        //TODO: fare resto in endTurn
    }



    public void setListWeaponsCanReload() {
        //cerco tutte le armi scariche che posso ricaricare, in base agli ammo che ho
       for(WeaponCard weaponCard:ownerPlayer.getUnloadedWeapons()) {
           //lista ammo di ricarica dell'arma
           reloadCost = weaponCard.getreloadCost();

           //se Player ha abbastanza Ammo aggiunge l'arma nella lista delle armi che il Player può ricaricare
           if(ownerPlayer.hasEnoughAmmo(reloadCost))
               weaponsCanReload.add(weaponCard);
       }
    }



    public void reloadWeapon() {
        reloadCost = weaponToReload.getreloadCost();

        if(ownerPlayer.hasEnoughAmmo(reloadCost)) {
            //faccio il reload
            ownerPlayer.reload(weaponToReload);
        }
        else
            throw new IllegalAttributeException("The player: " + ownerPlayer.getCharaName() + "with id: " + ownerPlayer.getIdPlayer() + "cannot pay the reload cost");
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
            turnManager.getGameTable().addTokenOnKillshotTrack(player, currentPlayer);

            //aggiungo un teschio(skull) ad ogni player morto
            player.increaseNumberOfSkulls();

            //TODO: passo ogni player in spawnState
        }
    }



    public void scoreDamagedPlayers() {
        //calcolo il punteggio dai danni dei players non morti quando finisce il gioco
        turnManager.scoreDamageLineOfAllPlayersWithDamage();
    }



    public void resetMap() {
        for(Square[] squareLine: turnManager.getGameTable().getMap())
            for(Square square: squareLine) {
                if(square != null)
                    if(square.getSquareType() == SquareType.SPAWNING_POINT)
                        square.reset(weaponDeck);
                    if(square.getSquareType() == SquareType.AMMO_POINT)
                        square.reset(ammoDeck);
            }
    }

}
