package it.polimi.ProgettoIngSW2019.view;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.LightModel.*;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.enums.SquareType;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.common.utilities.Observer;
import it.polimi.ProgettoIngSW2019.model.KillToken;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.util.ArrayList;
import java.util.List;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * Class that contains what the client can see
 * @author Nicholas Magatti
 */
public class InfoOnView implements Observer<Event> {

    private static int myId;
    private static String myNickname;
    private static String hostname;

    private static PlayerDataLM[] players;
    private static MyLoadedWeaponsLM myLoadedWeapons;
    private static MyPowerUpLM myPowerUps;
    private static MapLM map;
    private static KillshotTrackLM killshotTrack;



    static int getMyId() {
        return myId;
    }

    static String getMyNickname() {
        return myNickname;
    }

    static String getHostname() {
        return hostname;
    }

    static void createFirstLightModel(
                        int id, String username, String hostName, PlayerDataLM[] allPlayers,
                        MyLoadedWeaponsLM loadedWeapons, MyPowerUpLM powerUps,
                        MapLM mapUsed, KillshotTrackLM killshotTrackGame){
        myId = id;
        myNickname = username;
        hostname = hostName;
        players = allPlayers;
        myLoadedWeapons = loadedWeapons;
        myPowerUps = powerUps;
        map = mapUsed;
        killshotTrack = killshotTrackGame;
    }

    static void printEverything(){

    }

    /**
     * Print map and killshot track
     */
    void printMapAndKillshotTrack(){
        printMap();
        System.out.print("\t");
        printKillshotTrack();
        System.out.print("\n\n");
    }

    /**
     * Print the map, line by line (with colors)
     */
    void printMap(){
        ToolsView.printTheSpecifiedMap(map);
    }

    /**
     * Print the killshot track
     */
    void printKillshotTrack(){
        List<KillToken> track = killshotTrack.getTrack();
        System.out.print("Killshot track: ");
        for(KillToken killToken : track){
            System.out.print(killToken.getCharacterName() + " ");
            if(killToken.isOverkill()){
                System.out.print("x2");
            }
            //if not last element of the tokens
            if(track.indexOf(killToken) != killshotTrack.getInitialNumberOfSkulls() - 1) {
                System.out.print(", ");
            }
        }
        int skullsStillOnTheTrack = killshotTrack.getInitialNumberOfSkulls() - track.size();
        for(int skull = 0; skull < skullsStillOnTheTrack; skull++){
            System.out.print("[skull]");
            //if not last
            if(skull < skullsStillOnTheTrack - 1){
                System.out.print(", ");
            }
        }
        System.out.print("\n");
    }

    void printWeaponsOnSpawnPoints(){
        //array of spawn points in order of idRoom
        SpawnPointLM[] spawnPoints = spawnPoints();
        System.out.print("Weapons on Spawn Points:\t");


        System.out.print("\n");
    }

    void printListOfWeapons(List<WeaponLM> weapons){
        for(WeaponLM weapon : weapons){
            System.out.print(weapon.getName());
            if(weapons.indexOf(weapon) != weapons.size()-1){
                System.out.print(", ");
            }
        }
    }

    void printListOfPowerups(List<PowerUpLM> powerUps) {
        for(PowerUpLM pu : powerUps){
            System.out.print(pu.getName() + "(" + ToolsView.ammoTypeToString(pu.getGainAmmoColor()) + ")");
            if(powerUps.indexOf(pu) != powerUps.size()-1){
                System.out.print(", ");
            }
        }
    }



    /**
     * Return the spawn points in an array with the spawn point of the color corresponding to
     * the id room X at position X.
     * @return the spawn points in an array with the spawn point of the color corresponding to
     * the id room X at position X.
     */
    SpawnPointLM[] spawnPoints(){
        SpawnPointLM[] spawnPoints = new SpawnPointLM[3];
        SquareLM[][] mapLM = map.getMap();

        for(SquareLM[] rowMap : mapLM){
            for(SquareLM square : rowMap){
                if(square.getSquareType() == SquareType.SPAWNING_POINT){
                    spawnPoints[square.getIdRoom()] = (SpawnPointLM)square;
                }
            }
        }
        return spawnPoints;
    }

    /**
     * Update the objects that can be seen in the view when the controller sends the new versions (of players, cards, map, killshot track)
     * @param event
     */
    @Override
    public void update(Event event){

    }

}
