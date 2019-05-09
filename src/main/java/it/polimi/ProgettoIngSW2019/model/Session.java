package it.polimi.ProgettoIngSW2019.model;

import java.util.ArrayList;
import java.util.List;

public class Session {
    private List<String> client;
    private List<Player> playerConnected;
    private GameTable gt;
    private int idPlayer;

    public Session(GameTable gt){
        playerConnected = new ArrayList<>();
        client = new ArrayList<>();
        this.gt = gt;
        idPlayer = 0;
    }

    public void registerClient(String ip) {
        if(!client.contains(ip))
            client.add(ip);
        else {
            //TODO: vuol dire che l'utente è già connesso e bisogna rimetterlo in partita
        }
    }

    public void createPlayer(String username){
        Player p = new Player(idPlayer, username, gt);
        playerConnected.add(p);
        idPlayerGenerator();
    }

    public void idPlayerGenerator(){
        idPlayer++;
    }
}
