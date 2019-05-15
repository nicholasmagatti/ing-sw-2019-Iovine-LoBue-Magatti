package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.model.GameTable;
import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.model.WeaponCard;
import it.polimi.ProgettoIngSW2019.model.Player;

import java.util.ArrayList;
import java.util.List;

public class ShootController {
    private GameTable gameTable;
    private List<Player> targetPlayer;
    private Player userPlayer;
    private List<Square> targetMove;
    private Square userMove;
    private WeaponCard weaponUsed;

    public ShootController(GameTable gameTable){
        this.gameTable = gameTable;
    }

    public void shoot(List<Player> targetPlayer, Player userPlayer, List<Square> targetMove, Square userMove, WeaponCard weaponUsed) {
        weaponUsed.useBaseEff(targetPlayer, userPlayer, targetMove, userMove);
    }
}
