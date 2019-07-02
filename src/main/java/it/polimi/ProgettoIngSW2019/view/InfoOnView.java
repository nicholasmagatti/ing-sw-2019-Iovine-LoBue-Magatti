package it.polimi.ProgettoIngSW2019.view;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.LightModel.*;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
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


    /**
     * Get id player of this client
     * @return id player of this client
     */
    static int getMyId() {
        return myId;
    }

    /**
     * Get name of the player belonging to this client
     * @return name of the player belonging to this client
     */
    static String getMyNickname() {
        return myNickname;
    }

    /**
     * Get the hostname of this client
     * @return hostname of this client
     */
    static String getHostname() {
        return hostname;
    }

    /**
     * Get the public information about the players
     * @return array with public information about the players
     */
    static PlayerDataLM[] getPlayers() {
        return players;
    }

    /**
     * Get the information about the weapons on the hand of the user
     * @return weapons on the hand of the user
     */
    static MyLoadedWeaponsLM getMyLoadedWeapons() {
        return myLoadedWeapons;
    }

    /**
     * Get the information about the powerups on the hand of the user
     * @return powerups on the hand of the user
     */
    static MyPowerUpLM getMyPowerUps() {
        return myPowerUps;
    }

    /**
     * Create the first version of the 'light' model, with all the information
     * that this user is allowed to see.
     * @param id
     * @param username
     * @param hostName
     * @param allPlayers
     * @param loadedWeapons
     * @param powerUps
     * @param mapUsed
     * @param killshotTrackGame
     */
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

    /**
     * Return a list with all the positions of all the non-null squares on the map
     * @return a list with all the positions of all the non-null squares on the map
     */
    static List<int[]> allNonNullSquarePositions(){
        List<int[]> allNonNullSquares = new ArrayList<>();
        for(int row = 0; row < GeneralInfo.ROWS_MAP; row++){
            for(int col = 0; col < GeneralInfo.COLUMNS_MAP; col++){
                SquareLM square = map.getMap()[row][col];
                if(square != null){
                    int[] positionSquare = {row, col};
                    allNonNullSquares.add(positionSquare);
                }
            }
        }
        return allNonNullSquares;
    }

    /**
     * Get the position of the player if present, return null otherwise.
     * @param player
     * @return the position of the player if present, return null otherwise.
     */
    static int[] positionPlayer(PlayerDataLM player){
        int[] position = new int[2];
        SquareLM[][] mapLM = map.getMap();
        for(int row=0; row < GeneralInfo.ROWS_MAP; row++){
            for(int col=0; col < GeneralInfo.COLUMNS_MAP; col++){
                if(mapLM[row][col] != null && mapLM[row][col].getPlayers().contains(player.getIdPlayer())){
                    position[0] = row;
                    position[1] = col;
                    return position;
                }
            }
        }
        return null;
    }

    /**
     * Print everything the player can see: the map and what is on it, the killshot track,
     * all the info about the other players, the cards on his/her own hand.
     */
    static void printEverythingVisible(){
        System.out.print("\n");
        printMapAndKillshotTrack();
        System.out.print("\n");
        printWeaponsOnSpawnPoints();
        System.out.print("\n");
        for(PlayerDataLM player : players){
            if(player.getIdPlayer() != myId){
                printPublicInfoOnPlayer(player);
                System.out.print("\n");
            }
        }
        System.out.println("YOU:");
        printPublicInfoOnPlayer(players[myId]);
        System.out.println("Your hand:");
        printMyPowerupsAndLoadedWeapons();
        System.out.print("\n\n");
    }

    /**
     * Print map and killshot track
     */
    private static void printMapAndKillshotTrack(){
        printMap();
        System.out.print("\t");
        printKillshotTrack();
        System.out.print("\n");
    }

    /**
     * Print the map, line by line (with colors)
     */
    private static void printMap(){
        ToolsView.printTheSpecifiedMap(map);
    }

    /**
     * Print the killshot track
     */
    private static void printKillshotTrack(){
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

    /**
     * Print weapons present in the spawn points.
     */
    private static void printWeaponsOnSpawnPoints(){
        //array of spawn points at the position X where X is the id room that represents a specific color
        SpawnPointLM[] spawnPoints = spawnPoints();
        System.out.print("Weapons on Spawn Points:\t");
        //every ammo color is linked to the respective color of the spawn point
        AmmoType[] listAmmoType = {AmmoType.RED, AmmoType.BLUE, AmmoType.YELLOW};
        for(AmmoType ammoType : listAmmoType){
            System.out.print("\t");
            //print the color relative to that ammo in uppercase
            System.out.print(ToolsView.ammoTypeToString(ammoType).toUpperCase() + ": ");
            //print list of weapons of the spawn point of the color relative to that ammo
            ToolsView.printListOfWeapons(spawnPoints[AmmoType.intFromAmmoType(ammoType)].getWeapons());
            //if not last ammoType of the array
            if(ammoType != listAmmoType[listAmmoType.length-1]){
                System.out.print(",");
            }
        }
        System.out.print("\n");
    }

    /**
     * Print the public information relative to the specified player
     * @param player
     */
    private static void printPublicInfoOnPlayer(PlayerDataLM player){
        char markerPlayer;
        String activeOrNot;
        if(player.getDown()){
            markerPlayer = 'p';
        }
        else{
            markerPlayer = 'P';
        }
        if(player.getActive()){
            activeOrNot = "active";
        }
        else{
            activeOrNot = "inactive";
        }
        System.out.print(markerPlayer + (player.getIdPlayer()+1) + ": " + player.getNickname() + " (" + activeOrNot + "),\t");
        System.out.print("Skulls: " + player.getnSkulls() + ",\t");
        System.out.println("Ammo: " + player.getnRedAmmo() + " red, " +
                                    player.getnBlueAmmo() + " blue, " +
                                    player.getnYellowAmmo() + " yellow");
        System.out.print("Damage Line: ");

        for(int i=0; i < GeneralInfo.DAMAGE_TO_OVERKILL; i++){
            if(i < player.getDamageLine().size()) {
                System.out.print(player.getDamageLine().get(i));
            }
            else{ //empty spaces
                System.out.print("[-]");
            }
            if(i == GeneralInfo.DAMAGE_TO_KILL - 1){
                System.out.print("(kill)");
            }
            if(i == GeneralInfo.DAMAGE_TO_OVERKILL - 1){
                System.out.print("(overkill)");
            }
            else{
                System.out.print(", ");
            }
        }
        System.out.print(";\t");

        System.out.print("Mark Line: ");
        for(int i=0; i < player.getMarkLine().size(); i++){
            System.out.print(player.getMarkLine().get(i));
            if(i < player.getMarkLine().size()-1){
                System.out.print(", ");
            }
        }
        System.out.print("\n");
        System.out.print("Powerups: " + player.getnPowerUps() + "\t");
        System.out.print("Loaded weapons: " + player.getnMyLoadedWeapons() + "\t");
        System.out.print("Unloaded weapons: ");
        ToolsView.printListOfWeapons(player.getUnloadedWeapons());
        System.out.print("\n");
    }

    /**
     * Print the cards on the user's hand (loaded weapons and powerups)
     */
    private static void printMyPowerupsAndLoadedWeapons(){
        System.out.print("Powerups: ");
        ToolsView.printListOfPowerups(myPowerUps.getPowerUps());
        System.out.print("\t");
        System.out.print("Loaded weapons: ");
        ToolsView.printListOfWeapons(myLoadedWeapons.getLoadedWeapons());
        System.out.print("\n");
    }

    /**
     * Return the spawn points in an array with the spawn point of the color corresponding to
     * the id room X at position X.
     * @return the spawn points in an array with the spawn point of the color corresponding to
     * the id room X at position X.
     */
    private static SpawnPointLM[] spawnPoints(){
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
    public synchronized void update(Event event){
        EventType command = event.getCommand();
        String jsonMessage = event.getMessageInJsonFormat();

        if(command == EventType.UPDATE_MAP){
            map = new Gson().fromJson(jsonMessage, MapLM.class);
        }

        if(command == EventType.UPDATE_KILLSHOTTRACK){
            killshotTrack = new Gson().fromJson(jsonMessage, KillshotTrackLM.class);
        }

        if(command == EventType.UPDATE_PLAYER_INFO){
            PlayerDataLM playerToUpdate = new Gson().fromJson(jsonMessage, PlayerDataLM.class);
            players[playerToUpdate.getIdPlayer()] = playerToUpdate;
        }

        if(command == EventType.UPDATE_MY_POWERUPS){
            myPowerUps = new Gson().fromJson(jsonMessage, MyPowerUpLM.class);
        }

        if(command == EventType.UPDATE_MY_LOADED_WEAPONS){
            myLoadedWeapons = new Gson().fromJson(jsonMessage, MyLoadedWeaponsLM.class);
        }

        if(command == EventType.MSG_BEFORE_ENEMY_ACTION_OR_RELOAD){
            printEverythingVisible();
        }
    }

}
