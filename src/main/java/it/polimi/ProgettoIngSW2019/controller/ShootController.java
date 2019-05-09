package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.common.message.shootmsg.RequestTargetMessage;
import it.polimi.ProgettoIngSW2019.model.GameTable;
import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.model.WeaponCard;
import it.polimi.ProgettoIngSW2019.utilities.Observer;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.common.message.shootmsg.*;
import it.polimi.ProgettoIngSW2019.virtual_view.ShootVirtualView;

import java.util.ArrayList;
import java.util.List;

public class ShootController {
    private GameTable gameTable;
    private List<Player> targetPlayer;
    private Player userPlayer;
    private List<Square> targetMove;
    private Square userMove;
    private WeaponCard weaponUsed;
    private ShootVirtualView shootVirtualView;

    public ShootController(ShootVirtualView shootVirtualView, GameTable gameTable){
        shootVirtualView.addObserver(new RequestTargetListener());
        shootVirtualView.addObserver(new UseEffectListener());
        this.shootVirtualView = shootVirtualView;
        this.gameTable = gameTable;
    }

    private class RequestTargetListener implements Observer<ShootMessage> {
        public void update(ShootMessage shootMessage) {
            if(shootMessage instanceof RequestTargetMessage){
                //TODO: nella RequestTargetMessage vanno passati i parametri da dare al client
                shootVirtualView.setMessage(new RequestTargetMessage());
            }
        }
    }

    private class UseEffectListener implements Observer<ShootMessage> {
        public void update(ShootMessage shootMessage) {
            if(shootMessage instanceof UseEffectMessage){
                targetPlayer = getTargetPlayerById(((UseEffectMessage) shootMessage).getIdTargetPlayer());
                userPlayer = getUserPlayerById(((UseEffectMessage) shootMessage).getIdUserPlayer());
                targetMove = getTargetSquareToMoveByCoordinates(((UseEffectMessage) shootMessage).getTargetMoveCoordinates());
                userMove = getUserSquareToMoveByCoordinates(((UseEffectMessage) shootMessage).getUserMoveCoordinates());
                weaponUsed = getWeaponUsedById(((UseEffectMessage) shootMessage).getIdWeaponUsed());
                shoot(targetPlayer, userPlayer, targetMove, userMove, weaponUsed);
            }
        }
    }


    public void shoot(List<Player> targetPlayer, Player userPlayer, List<Square> targetMove, Square userMove, WeaponCard weaponUsed) {
        weaponUsed.useBaseEff(targetPlayer, userPlayer, targetMove, userMove);
    }

    public List<Player> getTargetPlayerById(List<Integer> idTargetPlayer){
        List<Player> targetPlayers = new ArrayList<>();
        for(Integer id: idTargetPlayer)
            targetPlayers.add(gameTable.getPlayers()[id]);

        return targetPlayers;
    }

    public Player getUserPlayerById(int idUserPlayer){
        Player userPlayer;
        userPlayer = gameTable.getPlayers()[idUserPlayer];

        return userPlayer;
    }

    public List<Square> getTargetSquareToMoveByCoordinates(List<Integer[]> coordinates){
        List<Square> targetSquareToMove = new ArrayList<>();
        int row;
        int column;

        for(Integer[] c: coordinates){
            row = c[0];
            column = c[1];
            if(row != -1 && column != -1)
                targetSquareToMove.add(gameTable.getMap()[row][column]);
        }

        return targetSquareToMove;
    }

    public Square getUserSquareToMoveByCoordinates(int[] coordintes){
        Square userSquareToMove = null;
        int row = coordintes[0];
        int column = coordintes[1];

        if(row != -1 && column != -1)
            userSquareToMove = gameTable.getMap()[row][column];

        return  userSquareToMove;
    }

    public WeaponCard getWeaponUsedById(int idWeapon){
        WeaponCard weaponCard;
        weaponCard = userPlayer.getLoadedWeapons().get(idWeapon);

        return weaponCard;
    }
}
