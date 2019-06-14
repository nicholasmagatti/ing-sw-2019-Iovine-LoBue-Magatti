package it.polimi.ProgettoIngSW2019.view;

import it.polimi.ProgettoIngSW2019.common.LightModel.*;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.enums.SquareType;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
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
public class InfoOnView {

    private int myId;
    private String myNickname;

    private PlayerDataLM[] players;
    private MyLoadedWeaponsLM myLoadedWeapons;
    private MyPowerUpLM myPowerUps;
    private MapLM map;
    private KillshotTrackLM killshotTrack;

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



    public InfoOnView(int id, String nickname, int numberOfPlayers){
        myId = id;
        myNickname = nickname;
        players = new PlayerDataLM[numberOfPlayers];
    }

    public int getMyId() {
        return myId;
    }

    /**
     * Print the map, line by line (with colors)
     */
    //TODO: add coordinates (A B ... columns, 1 2 ... rows)
    void printMap(){
        List<List<List<Ansi>>> mapAnsi = mapLMToAnsi(map.getMap());
        for(List<List<Ansi>> mapRow : mapAnsi){
            /*mapRow.get(0) is the first square of the current mapRow,
                assuming the other squares on the same mapRow have the same
                number of rows (in other words, the same .size())
             */
            for(int squareRow=0; squareRow < mapRow.get(0).size() ; squareRow++){
                    for(List<Ansi> square: mapRow){
                    AnsiConsole.out.print(square.get(squareRow));
                }
                /*after printing the whole  specified line of the map (the specified line of all
                    the squares on the specified row of squares of the map), start a new line
                 */
                System.out.print("\n");
            }
        }
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
            stringsToReturn[0] = EMPTY_LINE_INSIDE_SQUARE;
            stringsToReturn[1] = EMPTY_LINE_INSIDE_SQUARE;
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

    public static char[] coordinatesForUser(int[]coordinatesForDeveloper){
        if(coordinatesForDeveloper.length != 2){
            throw new RuntimeException("The coordinates should be 2 (row and column), not " + coordinatesForDeveloper.length +".");
        }
        char[]coordinetesForUser = new char[2];
        //row (with letters: A,B,C instead of 0,1,2)
        coordinetesForUser[0] = (char)((int) 'A' + coordinatesForDeveloper[0]);
        //column (1,2,3 instead of 0,1,2)
        coordinetesForUser[1] = (char)((int) '1' + coordinatesForDeveloper[1]);
        return coordinetesForUser;
    }

}
