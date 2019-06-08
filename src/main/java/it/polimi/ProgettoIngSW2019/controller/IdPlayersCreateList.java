package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.TurnManager;

import java.util.ArrayList;
import java.util.List;

public class IdPlayersCreateList {
    private static List<Integer> idPlayers = new ArrayList<>();
    private TurnManager turnManager;


    public IdPlayersCreateList(TurnManager turnManager) {
        this.turnManager = turnManager;
    }


    public List<Integer> addAllIdPlayers() {
        idPlayers.clear();
        Player[] playersList = turnManager.getGameTable().getPlayers();

        for(Player player: playersList) {
            idPlayers.add(player.getIdPlayer());
        }
        return idPlayers;
    }



    public List<Integer> addOneIdPlayers(Player player) {
        idPlayers.clear();
        idPlayers.add(player.getIdPlayer());
        return idPlayers;
    }


    public List<Integer> addAllExceptPlayer(Player player) {
        idPlayers.clear();
        Player[] playersList = turnManager.getGameTable().getPlayers();

        for(Player p: playersList) {
            if(p.getIdPlayer() != player.getIdPlayer())
                idPlayers.add(p.getIdPlayer());
        }
        return idPlayers;
    }
}
