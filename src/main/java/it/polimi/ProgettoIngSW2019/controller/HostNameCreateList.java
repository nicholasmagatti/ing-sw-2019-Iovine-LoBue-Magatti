package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.TurnManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates list of HostName of each client to send info
 */
public class HostNameCreateList {
    private List<String> hostNamePlayers = new ArrayList<>();
    private TurnManager turnManager;


    /**
     * Constructor
     * @param turnManager   turnManager
     */
    public HostNameCreateList(TurnManager turnManager) {
        this.turnManager = turnManager;
    }


    /**
     * get list of host name
     * @return  list host name
     */
    public List<String> getHostNamePlayers() {
        return hostNamePlayers;
    }


    /**
     * Add in the list all the clients
     * @return      list of host name
     */
    public List<String> addAllHostName() {
        hostNamePlayers.clear();
        Player[] playersList = turnManager.getGameTable().getPlayers();

        for(Player player: playersList) {
            hostNamePlayers.add(player.getHostname());
        }
        return hostNamePlayers;
    }


    /**
     * add only one host name
     * @param player    player to send info
     * @return          list of host name
     */
    public List<String> addOneHostName(Player player) {
        hostNamePlayers.clear();
        hostNamePlayers.add(player.getHostname());
        return hostNamePlayers;
    }


    /**
     * add all the host name of the player except one
     * @param player        player cannot be added
     * @return              list of host name
     */
    public List<String> addAllExceptOneHostName(Player player) {
        hostNamePlayers.clear();
        Player[] playersList = turnManager.getGameTable().getPlayers();

        for(Player p: playersList) {
            if(p.getIdPlayer() != player.getIdPlayer())
                hostNamePlayers.add(p.getHostname());
        }
        return hostNamePlayers;
    }
}
