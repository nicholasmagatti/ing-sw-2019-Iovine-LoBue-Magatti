package it.polimi.ProgettoIngSW2019.view;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.LightModel.*;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.enums.SquareType;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.common.utilities.Observer;
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

    private PlayerDataLM[] players;
    private MyLoadedWeaponsLM myLoadedWeapons;
    private MyPowerUpLM myPowerUps;
    private MapLM map;
    private KillshotTrackLM killshotTrack;
    private static String hostname;

    //to display map in vew
    private final int ROWS_BODY_SQUARE = 4;
    private final int SPACES_INSIDE_SQUARE = 10;
    private final char EMPTY_SPACE_CAR = ' ';
    private final String EMPTY_LINE_INSIDE_SQUARE = "          ";
    //we call 'black squares' the ones that are null
    private final String BLACK_SQUARE_BORDER_EAST_WEST = "   ";
    private final String BLACK_SQUARE_BORDER_NORTH_SOUTH = EMPTY_SPACE_CAR + EMPTY_LINE_INSIDE_SQUARE + EMPTY_SPACE_CAR;
    private final String BLOCKED_AT_NORTH_SOUTH = "__________ ";
    private final String DOOR_AT_NORTH_SOUTH = "___    ___ ";
    private final String SAME_ROOM_AT_NORTH_SOUTH = ".......... ";
    private final String BLOCKED_AT_EAST_WEST = "||||"; //vertical
    private final String DOOR_AT_EAST_WEST = "|  |"; //vertical
    private final String SAME_ROOM_AT_EAST_WEST = "...."; //vertical



    public InfoOnView(int id, String nickname, int numberOfPlayers, String hostname){
        myId = id;
        myNickname = nickname;
        players = new PlayerDataLM[numberOfPlayers];
        this.hostname = hostname;
    }

    static int getMyId() {
        return myId;
    }

    public static String getMyNickname() {
        return myNickname;
    }

    static String getHostname() {
        return hostname;
    }

    /**
     * Print the map, line by line (with colors)
     */
    void printMapAndKillshotTrack(){
        List<List<List<Ansi>>> mapAnsi = mapLMToAnsi(map.getMap());
        for(int mapRow = 0; mapRow < GeneralInfo.ROWS_MAP; mapRow++){
            /*mapAnsi.get(mapRow).get(0) is the first square of the current mapRow,
                assuming the other squares on the same mapRow have the same
                number of rows (in other words, the same .size())
             */
            for(int squareRow=0; squareRow < mapAnsi.get(mapRow).get(0).size() ; squareRow++){
                for(List<Ansi> square: mapAnsi.get(mapRow)){
                    AnsiConsole.out.print(square.get(squareRow));
                    /*at the last of the square row, at the second row of
                        the square, print the vertical coordinate
                     */
                    if((mapRow == GeneralInfo.ROWS_MAP -1) && squareRow == 1){
                        //print vertical coordinate coordinate
                        System.out.print("  " + verticalCoordinateForUser(mapRow));
                    }
                }
                /*after printing the whole  specified line of the map (the specified line of all
                    the squares on the specified row of squares of the map), start a new line
                 */
                System.out.print("\n");
            }
        }
        //print horizontal coordinates
        System.out.print("\n");
        for(int i=0; i < GeneralInfo.COLUMNS_MAP; i++){
            System.out.print("     " + horizontalCoordinateForUser(i) + "     ");
        }
        System.out.print("\t");
        printKillshotTrack();
        System.out.print("\n\n");

    }

    void printKillshotTrack(){
        //TODO
    }

    List<List<List<Ansi>>> mapLMToAnsi(SquareLM[][] mapLM){

        List<List<List<Ansi>>> mapAnsi = new ArrayList<>();

        for(int row = 0; row < mapLM.length; row++) {
            List<List<Ansi>> rowMapAnsi = new ArrayList<>();
            for (int col = 0; col < mapLM[0].length; col++) {
                List<Ansi> squareAnsi = squareLMToAnsi(row, col, mapLM);
                rowMapAnsi.add(squareAnsi);
            }
            mapAnsi.add(rowMapAnsi);
        }
        return mapAnsi;
    }


    List<Ansi> squareLMToAnsi(int row, int col, SquareLM[][] mapLM){
        //squares have only two borders: the ones at east and south
        //fist I work with strings, then I add them here as ansi
        List<String> squareOfStrings = new ArrayList<>();

        //if square is null (empty square)
        SquareLM squareLM = mapLM[row][col];
        String[] center = bodySquare(squareLM);
        //set right border
        String rightBorder;
        String bottomBorder;
        if(squareLM == null){
                rightBorder = BLACK_SQUARE_BORDER_EAST_WEST;
                bottomBorder = BLACK_SQUARE_BORDER_NORTH_SOUTH;
        }
        else{ //normal square
            //set rightBorder
            if(squareLM.isBlockedAtEast()){
                rightBorder = BLOCKED_AT_EAST_WEST;
            }
            else{ //door or not?
                if(squareLM.getIdRoom() == mapLM[row][col+1].getIdRoom()){
                    rightBorder = SAME_ROOM_AT_EAST_WEST;
                }
                else{
                    rightBorder = DOOR_AT_EAST_WEST;
                }
            }
            //set bottomBorder
            if(squareLM.isBlockedAtSouth()){
                bottomBorder = BLOCKED_AT_NORTH_SOUTH;
            }
            else{
                if(squareLM.getIdRoom() == mapLM[row+1][col].getIdRoom()){
                    bottomBorder = SAME_ROOM_AT_NORTH_SOUTH;
                }
                else{
                    bottomBorder = DOOR_AT_NORTH_SOUTH;
                }
            }
        }
        //add right border to center
        for(int i=0; i < center.length; i++){
            center[i] += rightBorder.charAt(i);
        }
        //add everything (center and bottom) to list of strings
        //add center
        for(String s : center){
            squareOfStrings.add(s);
        }
        //add bottom
        squareOfStrings.add(bottomBorder);
        return colorSquare(squareOfStrings, squareLM);
    }

    /**
     * Return the colored sqaure as a list of Ansi
     * @param squareOfStrings
     * @param squareLM
     * @return
     */
    List<Ansi>colorSquare(List<String> squareOfStrings, SquareLM squareLM){
        List<Ansi> squareAnsi = new ArrayList<>();
        Ansi.Color background;
        if(squareLM == null){
            background = BLACK;
        }
        else{
            if(squareLM.getIdRoom() == GeneralInfo.RED_ROOM_ID){
                background = RED;
            }
            if(squareLM.getIdRoom() == GeneralInfo.BLUE_ROOM_ID){
                background = BLUE;
            }
            if(squareLM.getIdRoom() == GeneralInfo.YELLOW_ROOM_ID){
                background = YELLOW;
            }
            if(squareLM.getIdRoom() == GeneralInfo.GRAY_ROOM_ID){
                background = WHITE;
            }
            if(squareLM.getIdRoom() == GeneralInfo.PURPLE_ROOM_ID){
                background = MAGENTA;
            }
            if(squareLM.getIdRoom() == GeneralInfo.GREEN_ROOM_ID){
                background = GREEN;
            }
            else {
                throw new RuntimeException("The idRoom does not correspond to the ones considered available for the map.");
            }
        }
        for(String string : squareOfStrings){
            squareAnsi.add(ansi().fg(BLACK).bg(background).a(string).reset());
        }
        return squareAnsi;
    }

    String[] bodySquare(SquareLM squareLM){
        String[] bodyLines = new String[ROWS_BODY_SQUARE];
        if(squareLM == null){ //if it is empty
            for(int i=0; i < bodyLines.length; i++){
                bodyLines[i] = EMPTY_LINE_INSIDE_SQUARE;
            }
        }
        else{ //if it is not empty
            //first line: players on the square
            bodyLines[0] = playersToDrawOnSquare(squareLM);
            bodyLines[1] = EMPTY_LINE_INSIDE_SQUARE;
            //third and fourth lines:
            String [] otherInfoSquare;
            if(squareLM.getSquareType() == SquareType.AMMO_POINT){
                otherInfoSquare = specificInfoAmmoPointToDraw((AmmoPointLM)squareLM);
                bodyLines[2] = otherInfoSquare[0];
                bodyLines[3] = otherInfoSquare[1];
            }
            //SPAWN POINT
            if(squareLM.getSquareType() == SquareType.SPAWNING_POINT){
                otherInfoSquare = specificInfoSpawnPointToDraw((SpawnPointLM)squareLM);
                bodyLines[2] = otherInfoSquare[0];
                bodyLines[3] = otherInfoSquare[1];
            }
        }
        return bodyLines;
    }

    String playersToDrawOnSquare(SquareLM squareLM){
        String stringToReturn = "";
        for(int idPlayer : squareLM.getPlayers()){
            //eg: the player with id 0 is the player 1
            stringToReturn += "P" + (idPlayer + 1);
        }
        //all the rest of the line is blank (' ')
        return lineInsideSquareWithBlankAtTheEnd(stringToReturn);
    }

    String [] specificInfoAmmoPointToDraw(AmmoPointLM ammoPointLM){
        String[] stringsToReturn = new String[2];
        if(ammoPointLM.getAmmoCard() == null){
            for(int i=0; i < 2; i++) {
                stringsToReturn[i] = EMPTY_LINE_INSIDE_SQUARE;
            }
        }
        else{
            stringsToReturn[0] = lineInsideSquareWithBlankAtTheEnd("AmmoCard:");
            stringsToReturn[1]= "";
            List<AmmoType> ammoInAmmoCard = ammoPointLM.getAmmoCard().getAmmo();
            for(int i=0; i < ammoInAmmoCard.size(); i++){
                AmmoType ammo = ammoInAmmoCard.get(i);
                if(ammo == AmmoType.RED){
                    stringsToReturn[1] += "r";
                }
                if(ammo == AmmoType.BLUE){
                    stringsToReturn[1] += "b";
                }
                if(ammo == AmmoType.YELLOW){
                    stringsToReturn[1] += "y";
                }
                //if this ammo is not the last of the list
                if(i < ammoInAmmoCard.size()-1) {
                    stringsToReturn[1] += ",";
                }
            }
            if(ammoPointLM.getAmmoCard().hasPowerup()){
                stringsToReturn[1] += ",PwUp";
            }
            stringsToReturn[1] = lineInsideSquareWithBlankAtTheEnd(stringsToReturn[1]);
        }
        return stringsToReturn;
    }

    String[] specificInfoSpawnPointToDraw(SpawnPointLM spawnPointLM){
        String[] stringsToReturn = new String[2];
        if(spawnPointLM.getWeapons().isEmpty()){
            stringsToReturn[0] = EMPTY_LINE_INSIDE_SQUARE;
        }
        else{
            stringsToReturn[0] = "weapons:";
            stringsToReturn[0] += spawnPointLM.getWeapons().size();
            //complete the rest of the line with empty space
            stringsToReturn[0] = lineInsideSquareWithBlankAtTheEnd(stringsToReturn[0]);
        }
        stringsToReturn[1] = "SpawnPoint";
        stringsToReturn[1] = lineInsideSquareWithBlankAtTheEnd(stringsToReturn[1]);
        return stringsToReturn;
    }

    String lineInsideSquareWithBlankAtTheEnd(String line) {
        while (line.length() < SPACES_INSIDE_SQUARE) {
            line+= EMPTY_SPACE_CAR;
        }
        return line;
    }

    /**
     * Translate coordinates for developers in coordinates for users (eg: (0,1) becomes (A,2) ).
     * @param coordinatesForDeveloper - numeric coordinates as row and column, beginning from 0 (eg: row=0, column=3).
     * @return coordinates for users (eg: [C, 3]).
     */
    public static char[] coordinatesForUser(int[]coordinatesForDeveloper){
        if(coordinatesForDeveloper.length != 2){
            throw new RuntimeException("The coordinates should be 2 (row and column), not " + coordinatesForDeveloper.length +".");
        }
        char[]coordinatesForUser = new char[2];
        //row (with letters: A,B,C instead of 0,1,2)
        coordinatesForUser[0] = verticalCoordinateForUser(coordinatesForDeveloper[0]);
        //column (1,2,3 instead of 0,1,2)
        coordinatesForUser[1] = horizontalCoordinateForUser(coordinatesForDeveloper[1]);
        return coordinatesForUser;
    }

    /**
     * Translate vertical coordinates for developers in coordinates for users (eg: from 0 to 1, from 3 to 4).
     * @param verticalCoordinateForDeveloper
     * @return the coordinate for the user as a char, starting the count from 1 instead of 0
     */
    private static char verticalCoordinateForUser(int verticalCoordinateForDeveloper){
        //row (with letters: A,B,C instead of 0,1,2)
        return  (char)((int) 'A' + verticalCoordinateForDeveloper);
    }

    /**
     * Translate horizontal coordinates for developers in coordinates for users (eg: from 0 to A, from 3 to D).
     * @param horizontalCoordinateForDeveloper
     * @return the coordinate for the user as a capital letter
     */
    private static char horizontalCoordinateForUser(int horizontalCoordinateForDeveloper){
        //column (1,2,3 instead of 0,1,2)
        return (char)((int) '1' + horizontalCoordinateForDeveloper);
    }

    /**
     * Update the objects that can be seen in the view when the controller sends the new versions (of players, cards, map, killshot track)
     * @param event
     */
    @Override
    public void update(Event event){

    }

}
