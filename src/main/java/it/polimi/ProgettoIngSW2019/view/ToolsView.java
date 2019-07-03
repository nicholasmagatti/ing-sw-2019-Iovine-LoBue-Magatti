package it.polimi.ProgettoIngSW2019.view;

import it.polimi.ProgettoIngSW2019.common.LightModel.*;
import it.polimi.ProgettoIngSW2019.common.Message.toController.PaymentChoiceInfo;
import it.polimi.ProgettoIngSW2019.common.Message.toView.EnemyInfo;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.enums.SquareType;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.common.utilities.InputScanner;
import it.polimi.ProgettoIngSW2019.model.AmmoPoint;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.util.ArrayList;
import java.util.List;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.Color.BLACK;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * Abstract static class that make different static methods of general user available for all the other classes of the view.
 * @author Nicholas Magatti
 * @author Luca Iovine
 */
public abstract class ToolsView {

    private static InputScanner inputScanner = new InputScanner();

    //to display map in vew
    private static final int ROWS_BODY_SQUARE = 4;
    private static final int SPACES_INSIDE_SQUARE = 10;
    private static final char EMPTY_SPACE_CAR = ' ';
    private static final String EMPTY_LINE_INSIDE_SQUARE = "          ";
    //we call 'black squares' the ones that are null
    private static final String BLACK_SQUARE_BORDER_NORTH_SOUTH = EMPTY_SPACE_CAR + EMPTY_LINE_INSIDE_SQUARE + EMPTY_SPACE_CAR;
    private static final String BLOCKED_AT_NORTH_SOUTH = "__________ ";
    private static final String DOOR_AT_NORTH_SOUTH = "___    ___ ";
    private static final String SAME_ROOM_AT_NORTH_SOUTH = ".......... ";
    private static final String BLACK_SQUARE_BORDER_EAST_WEST = "    "; //verical
    private static final String BLOCKED_AT_EAST_WEST = "||||"; //vertical
    private static final String DOOR_AT_EAST_WEST = "|  |"; //vertical
    private static final String SAME_ROOM_AT_EAST_WEST = "...."; //vertical
    //to display the interaction for buying/paying something
    private static final String red = "R";
    private static final String blue = "B";
    private static final String yellow = "Y";
    private static List<String> responeForAmmo;
    private static List<String> responseForPowerUp;
    private static int[] tmpAmmoInAmmoBox;
    private static int[] ammoChosen;
    private static int[] tmpCostToPay;
    private static List<PowerUpLM> tmpAmmoInPowerUp;
    private static List<Integer> powerUpIdChosenList;
    private static StringBuilder paymentSB;
    private static String msg;
    private static List<String> possibleChoice;
    private static int i;


    /**
     * Return the input scanner of this client.
     * @return the input scanner of this client
     * @author Nicholas Magatti
     */
    static InputScanner getInputScanner() {
        return inputScanner;
    }

    /**
     * Return a string with the information for the user (ready to be printed)
     * about the specified amount of ammo. It could be for a cost or for representing
     * the ammo that a player has in his/her ammo box.
     * @param cost - amount of ammo to represent
     * @return a string with the information for the user (ready to be printed) about the specified amount of ammo.
     * @author Nicholas Magatti
     */
    static String costToString(int[] cost){
        //example of output: 2 red, 1 blue, 0 yellow
        return cost[AmmoType.intFromAmmoType(AmmoType.RED)] + " red, " +
                cost[AmmoType.intFromAmmoType(AmmoType.BLUE)] + " blue, " +
                cost[AmmoType.intFromAmmoType(AmmoType.YELLOW)] + " yellow";
    }

    /**
     * From an ammo type get the string of the respective color in lowercase.
     * @param ammoType
     * @return string of the requested color in lowercase
     * @author Nicholas Magatti
     */
    static String ammoTypeToString(AmmoType ammoType){
        String color;
        switch (ammoType) {
            case BLUE:
                color = "blue";
                break;
            case YELLOW:
                color = "yellow";
                break;
            case RED:
                color = "red";
                break;
            default:
                throw new IllegalArgumentException("This ammo type is neither blue, yellow or red.");
        }
        return color;
    }


    /**
     * Print list of weapons with their names in a line.
     * @param weapons
     * @author Nicholas Magatti
     */
    static void printListOfWeapons(List<WeaponLM> weapons){
        for(WeaponLM weapon : weapons){
            System.out.print(weapon.getName());
            if(weapons.indexOf(weapon) != weapons.size()-1){
                System.out.print(", ");
            }
        }
    }

    /**
     * Print list of powerups with their names and their ammo color in a line.
     * @param powerUps
     * @author Nicholas Magatti
     */
    static void printListOfPowerups(List<PowerUpLM> powerUps) {
        for(PowerUpLM pu : powerUps){
            System.out.print(pu.getName() + "(" + ToolsView.ammoTypeToString(pu.getGainAmmoColor()) + ")");
            if(powerUps.indexOf(pu) != powerUps.size()-1){
                System.out.print(", ");
            }
        }
    }

    /**
     * Print list of players with their names in a line.
     * @param players
     * @author Nicholas Magatti
     */
    static void printListOfPlayersNames(List<PlayerDataLM> players){
        for(PlayerDataLM p : players){
            System.out.print(p.getNickname());
            if(players.indexOf(p) != players.size()-1){
                System.out.print(", ");
            }
        }
    }

    /**
     * Print the general options that are usually available for the player.
     * @author Nicholas Magatti
     */
    static void printGeneralOptions(){
        //System.out.println(GeneralInfo.EXIT_COMMAND + ": exit from game");
        System.out.println(GeneralInfo.PREFIX_COMMAND_DESCRIPTION + "NameWeapon/Powerup: read the description of that weapon/powerup");
    }


    /**
     * Keep reading input from user util he/she gives the requested result or
     * until the timer expires, in which case return null, and return the correct input otherwise.
     * Also allow the general options if they are enabled in the respective parameter.
     * @param allowedAnswers
     * @param generalOptionsEnabled - boolean that indicates if the general options are allowed here
     * @return return the correct input inserted by the user if the timer has not expired first, return null otherwise.
     * @author Nicholas Magatti
     */
    static String readUserChoice(List<String> allowedAnswers, boolean generalOptionsEnabled){
        /*
        if(generalOptionsEnabled) {
            allowedAnswers.add(GeneralInfo.EXIT_COMMAND);
        }*/
        boolean exit = false;
        String inputFromUser;
        do {
            inputScanner.read();
            inputFromUser = inputScanner.getInputValue();
            if(allowedAnswers.contains(inputFromUser)){
                exit = true;
            }
            else{
                if(generalOptionsEnabled && isDescriptionCommand(inputFromUser)){
                    printDescription(inputFromUser);
                    System.out.println("Answer the previous question or read another description.");
                    System.out.print(GeneralInfo.ASK_INPUT);
                }
                else {
                    System.out.println("Illegal input. Insert correct input.");
                }
            }
        }while(!exit && !inputScanner.isTimeExpired());

        inputScanner.close();

        if(inputScanner.isTimeExpired()){
            return null;
        }
        else {
            /*
            if(generalOptionsEnabled && inputFromUser.equals(GeneralInfo.EXIT_COMMAND)){
                method to disconnect
                return null;
            }
            else {*/
                return inputFromUser;
            /*}*/
        }
    }

    /**
     * Interaction with the user to make him/her pay one ammo of any color. Return the chosen ammo (or powerup) if
     * the timer has not expired; return null otherwise.
     * @param ammoInAmmoBox
     * @param ammoInPowerUp
     * @return the chosen ammo (or powerup) if the timer has not expired; return null otherwise.
     */
    static PaymentChoiceInfo payOneAmmo(int[] ammoInAmmoBox, List<PowerUpLM> ammoInPowerUp){
        System.out.println("You have " + costToString(ammoInAmmoBox) + "in your ammo box");
        System.out.println("Choose one ammo unit or powerup of the following to pay the cost of one ammo: ");
        List<String> options = new ArrayList<>();
        int nrOption = 1;

        for(PowerUpLM pw : ammoInPowerUp){
            System.out.println(nrOption + ": " + pw.getName() + "(" + ammoTypeToString(pw.getGainAmmoColor()) + ")");
            options.add(Integer.toString(nrOption));
            nrOption++;
        }
        AmmoType[] ammoTypes = {AmmoType.RED, AmmoType.BLUE, AmmoType.YELLOW};

        for(AmmoType ammoType : ammoTypes) {
            if (ammoInAmmoBox[AmmoType.intFromAmmoType(ammoType)] > 0) {
                System.out.println(nrOption + ": " + ammoTypeToString(ammoType).toUpperCase() + "ammo unit");
                options.add(Integer.toString(nrOption));
                nrOption++;
            }
        }
        System.out.print(GeneralInfo.ASK_INPUT);
        String userAnswer = readUserChoice(options, false);
        if(userAnswer == null){//time expired
            return null;
        }
        else{
            Integer idChosenPowerup;
            int[] ammoToDiscard = new int[3];
            List<Integer> idPowerUpsToDiscard = new ArrayList<>();

            int chosenOption = Integer.parseInt(userAnswer);
            if(chosenOption <= ammoInPowerUp.size()){//it is a powerup
                int posInList = chosenOption - 1;
                idChosenPowerup = ammoInPowerUp.get(posInList).getIdPowerUp();
                idPowerUpsToDiscard.add(idChosenPowerup);
            }
            else{//it is an ammo unit
                int posInAmmoTypes = chosenOption - ammoInPowerUp.size() - 1;
                AmmoType chosenAmmo = ammoTypes[posInAmmoTypes];
                ammoToDiscard[AmmoType.intFromAmmoType(chosenAmmo)] = 1;
            }

            return new PaymentChoiceInfo(ammoToDiscard, idPowerUpsToDiscard);
        }
    }

    /**
     * Return the id of the target chosen by the user if the time has not expired first,
     * or return the only possible target if there is no choice to make. Return null if the
     * timer expired.
     * @param enemyInfoList
     * @return the id of the target chosen by the user if the time has not expired first,
     * or return the only possible target if there is no choice to make. Return null if the
     * timer expired.
     */
    static Integer readTargetChoice(List<EnemyInfo> enemyInfoList){
        if(enemyInfoList.isEmpty()){
            throw new IllegalArgumentException("The list of possible targets should not be empty.");
        }
        if(enemyInfoList.size() == 1) { //only one possible target
            return enemyInfoList.get(0).getId();
        }
        else { //choose between the possible targets
            System.out.println("Choose one of these targets: ");
            List<String> options = new ArrayList<>();
            for (int i = 0; i < enemyInfoList.size(); i++) {
                System.out.println((i + 1) + ": " + enemyInfoList.get(i).getName());
                options.add(Integer.toString(i + 1));
            }
            System.out.print(GeneralInfo.ASK_INPUT);
            String userChoice = ToolsView.readUserChoice(options, false);
            if (userChoice != null) {//time not expired
                return enemyInfoList.get(Integer.parseInt(userChoice) - 1).getId();
            }
            else{
                return null;
            }
        }
    }

    /**
     *  Ask the user to choose between the possible destinations and return the chosen option if
     *  the timer has not expired, null otherwise.
     * @param possibleDestinations
     * @return the chosen option if the timer has not expired, null otherwise.
     */
    static int[] chooseDestination(List<int[]> possibleDestinations){
        System.out.println("Choose destination: ");
        List<String> options = new ArrayList<>();
        for(int i=0; i < possibleDestinations.size(); i++){
            int optionNr = i + 1;
            options.add(Integer.toString(optionNr));
            System.out.print(optionNr + ": ");

            char [] cordForUser = coordinatesForUser(possibleDestinations.get(i));

            for(char c : cordForUser){
                System.out.print(c);
            }
            System.out.print("\n");

        }
        System.out.print("Type the chosen option(the number at left, not the coordinates): ");
        String userChoice = readUserChoice(options, false);
        if(userChoice != null){//timer not expired
            int posInList = Integer.parseInt(userChoice) - 1;
            return possibleDestinations.get(posInList);
        }
        else{
            return null;
        }
    }

    /**
     * Translate coordinates for developers in coordinates for users (eg: (0,1) becomes (A,2) ).
     * @param coordinatesForDeveloper - numeric coordinates as row and column, beginning from 0 (eg: row=0, column=3).
     * @return coordinates for users (eg: [C, 3]).
     * @author Nicholas Magatti
     */
    static char[] coordinatesForUser(int[]coordinatesForDeveloper){
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
     * Print the specified map, line by line (with colors)
     * @param mapToPrint
     * @author Nicholas Magatti
     */
    static void printTheSpecifiedMap(MapLM mapToPrint){
        List<List<List<Ansi>>> mapAnsi = mapLMToAnsi(mapToPrint.getMap());
        for(int mapRow = 0; mapRow < GeneralInfo.ROWS_MAP; mapRow++){
            /*mapAnsi.get(mapRow).get(0) is the first square of the current mapRow,
                assuming the other squares on the same mapRow have the same
                number of rows (in other words, the same .size())
             */
            for(int squareRow=0; squareRow < mapAnsi.get(mapRow).get(0).size() ; squareRow++){
                //for each square on the specified mapRow(mapRow is a list of squares)
                for(List<Ansi> square: mapAnsi.get(mapRow)){
                    AnsiConsole.out.print(square.get(squareRow));
                    /*after the last character of the second line of the last square of
                      each row of the map, print the corresponding vertical coordinate
                     */
                    if((/*last square of the foreach*/ square.equals(mapAnsi.get(mapRow).get(mapAnsi.get(mapRow).size()-1)))
                            && squareRow == 1){
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
    }

    /**
     * Return true if the string is a request from the user to visualize the description of a card.
     * @param string
     * @return true if the string is a request from the user to visualize the description of a card
     * @author Nicholas Magatti
     */
    private static boolean isDescriptionCommand(String string){
        return stringStartsWith(string, GeneralInfo.PREFIX_COMMAND_DESCRIPTION);
    }

    /**
     * Return true if the string 'string' start with the string 'prefix', false otherwise.
     * @param string - string to parse
     * @param prefix - possible prefix of the string to parse
     * @return true if the string 'string' start with the string 'prefix', false otherwise
     * @author Nicholas Magatti
     */
    private static boolean stringStartsWith(String string, String prefix){
        for(int i=0; i < prefix.length(); i++){
            if(string.charAt(i) != prefix.charAt(i)){
                return false;
            }
        }
        return true;
    }


    /**
     * Given the input from a user that is asking for a detailed description of a card with a
     * certain name, print the detailed description if the name of the card is on the table or on
     * the hand of the player. If not, print a message that explains that the requested name has not
     * been found.
     * @param inputFromUser
     * @author Nicholas Magatti
     */
    private static void printDescription(String inputFromUser){
        String descrCommmand = GeneralInfo.PREFIX_COMMAND_DESCRIPTION;
        for(int index = 0; index < descrCommmand.length(); index++){
            if(inputFromUser.charAt(index) != descrCommmand.charAt(index)){
                throw new IllegalArgumentException("The input is not a description request. It " +
                        "this method should not have been called");
            }
        }
        String nameCard = descrCommmand.substring(GeneralInfo.PREFIX_COMMAND_DESCRIPTION.length());
        printDescriptionCard(nameCard);
    }

    /**
     * Given the name of a card from a user that is asking for a detailed description of a card with a
     * that name, print the detailed description if the name of the card is on the table or on
     * the hand of the player. If not, print a message that explains that the requested name has not
     * been found.
     * @param cardName
     * @author Nicholas Magatti
     */
    private static void printDescriptionCard(String cardName){
        String descWeaponFound = descriptionWeaponFoundOnView(cardName);
        String descPowerUpFound = descriptionPowerUpFoundOnView(cardName);
        String descToPrint;
        if(descWeaponFound == null && descPowerUpFound == null){
            System.out.println("The card you are searching for is not visible to you now, or does not exist.");
        }
        else{
            if(descWeaponFound != null){
                descToPrint = descWeaponFound;
            }
            else{
                descToPrint = descPowerUpFound;
            }
            System.out.println(descToPrint);
        }
    }

    /**
     * Return the string with the detailed description of the requested weapon card if
     * present on the client, return null otherwise.
     * @param cardName
     * @return the string with the detailed description of the requested weapon card if
     * present on the client, return null otherwise.
     * @author Nicholas Magatti
     */
    private static String descriptionWeaponFoundOnView(String cardName){
        String descToReturn = null;
        //search between your loaded weapons
        for(WeaponLM weapon : InfoOnView.getMyLoadedWeapons().getLoadedWeapons()){
            if(cardName.equalsIgnoreCase(weapon.getName())){
                descToReturn = detailedDescriptionWeapon(weapon);
            }
        }
        //search between the unloaded weapons of all the players
        for(PlayerDataLM player : InfoOnView.getPlayers()){
            for(WeaponLM weapon : player.getUnloadedWeapons()) {
                if (cardName.equalsIgnoreCase(weapon.getName())){
                    descToReturn = detailedDescriptionWeapon(weapon);
                }
            }
        }
        return descToReturn;
    }

    /**
     * Return the detailed description of a specific weapon.
     * @param weapon
     * @return the detailed description of a specific weapon.
     * @author Nicholas Magatti
     */
    private static String detailedDescriptionWeapon(WeaponLM weapon){
        return "Cost to reload: " + costToString(weapon.getAmmoCostReload()) +
                ",\tCost to buy: " + costToString(weapon.getAmmoCostBuy()) +
                "\nDescription: " + weapon.getDescription();
    }

    /**
     * Return the string with the detailed description of the requested powerup card if
     * present on the client, return null otherwise.
     * @param cardName
     * @return the string with the detailed description of the requested powerup card if
     * present on the client, return null otherwise.
     * @author Nicholas Magatti
     */
    private static String descriptionPowerUpFoundOnView(String cardName){
        String descToReturn = null;
        for(PowerUpLM card : InfoOnView.getMyPowerUps().getPowerUps()){
            if(cardName.equalsIgnoreCase(card.getName())){
                descToReturn = card.getDescription();
            }
        }
        return descToReturn;
    }

    /**
     * Convert a SquareLM[][] map in a list of lists of lists of Ansi (to be able to print it with colors).
     * @param mapLM
     * @return the map as lists of Ansi, ready to be printed with colors.
     * @author Nicholas Magatti
     */
    private static List<List<List<Ansi>>> mapLMToAnsi(SquareLM[][] mapLM){

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

    /**
     * Convert a square of the given map to a square as list of Ansi.
     * @param row
     * @param col
     * @param mapLM
     * @return a square formatted as a list of Ansi.
     * @author Nicholas Magatti
     */
    private static List<Ansi> squareLMToAnsi(int row, int col, SquareLM[][] mapLM){
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
            else{ //door or not? (check the square a right (col+1)
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
            else{ //check the square down (row+1)
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
     * Return the colored square as a list of Ansi
     * @param squareOfStrings
     * @param squareLM
     * @return
     * @author Nicholas Magatti
     */
    private static List<Ansi>colorSquare(List<String> squareOfStrings, SquareLM squareLM){
        List<Ansi> squareAnsi = new ArrayList<>();
        Ansi.Color background;
        if(squareLM == null){
            background = BLACK;
        }
        else{
            if(squareLM.getIdRoom() == GeneralInfo.RED_ROOM_ID){
                background = RED;
            }
            else if(squareLM.getIdRoom() == GeneralInfo.BLUE_ROOM_ID){
                background = BLUE;
            }
            else if(squareLM.getIdRoom() == GeneralInfo.YELLOW_ROOM_ID){
                background = YELLOW;
            }
            else if(squareLM.getIdRoom() == GeneralInfo.GRAY_ROOM_ID){
                background = WHITE;
            }
            else if(squareLM.getIdRoom() == GeneralInfo.PURPLE_ROOM_ID){
                background = MAGENTA;
            }
            else if(squareLM.getIdRoom() == GeneralInfo.GREEN_ROOM_ID){
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

    /**
     * Return the strings that compose the body of the square (the square excluding the borders).
     * @param squareLM
     * @return
     * @author Nicholas Magatti
     */
    private static String[] bodySquare(SquareLM squareLM){
        String[] bodyLines = new String[ROWS_BODY_SQUARE];
        if(squareLM == null){ //if it is empty
            for(int i=0; i < bodyLines.length; i++){
                bodyLines[i] = EMPTY_LINE_INSIDE_SQUARE;
            }
        }
        else{ //if it is not empty
            //first line: players on the square
            bodyLines[0] = playersToDrawOnSquare(squareLM);
            //second line: nothing (empty space)
            bodyLines[1] = EMPTY_LINE_INSIDE_SQUARE;
            //third and fourth lines:
            String [] otherInfoSquare;
            if(squareLM.getSquareType() == SquareType.AMMO_POINT){
                otherInfoSquare = specificInfoAmmoPointToDraw((AmmoPointLM) squareLM);
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

    /**
     * Return the line with the info about the players to draw on a specific square
     * @param squareLM
     * @return
     * @author Nicholas Magatti
     */
    private static String playersToDrawOnSquare(SquareLM squareLM){
        if(squareLM == null){
            throw new NullPointerException("The parameter should not be null");
        }
        String stringToReturn = "";
        for(int idPlayer : squareLM.getPlayers()){
            //'P' if the player is up, 'p' if the player is down
            char markerPlayer;
            if(InfoOnView.getPlayers()[idPlayer].getDown()){
                markerPlayer = 'p';
            }
            else{
                markerPlayer = 'P';
            }
            //eg: the player with id 0 is the player 1
            stringToReturn += markerPlayer + (idPlayer + 1);
        }
        //all the rest of the line is blank (' ')
        return lineInsideSquareWithBlankAtTheEnd(stringToReturn);
    }

    /**
     * Return the lines with the info about the indicated ammo point to draw in it.
     * @param ammoPointLM
     * @return the lines with the info about the indicated ammo point to draw in it.
     * @author Nicholas Magatti
     */
    private static String [] specificInfoAmmoPointToDraw(AmmoPointLM ammoPointLM){
        if(ammoPointLM == null){
            throw new NullPointerException("The parameter should not be null.");
        }
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

    /**
     * Return the lines with the info about the indicated spawn point to draw in it.
     * @param spawnPointLM
     * @return he lines with the info about the indicated spawn point to draw in it.
     * @author Nicholas Magatti
     */
    private static String[] specificInfoSpawnPointToDraw(SpawnPointLM spawnPointLM){
        if(spawnPointLM == null){
            throw new NullPointerException("The parameter should not be null.");
        }
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

    /**
     * Create the white space at the end of a line of a square and return the completed line.
     * @param line - line with the important content, without the white space at the end.
     * @return the completed line with white space at the end
     */
    private static String lineInsideSquareWithBlankAtTheEnd(String line) {
        while (line.length() < SPACES_INSIDE_SQUARE) {
            line+= EMPTY_SPACE_CAR;
        }
        return line;
    }

    /**
     * Translate vertical coordinates for developers in coordinates for users (eg: from 0 to 1, from 3 to 4).
     * @param verticalCoordinateForDeveloper
     * @return the coordinate for the user as a char, starting the count from 1 instead of 0
     * @author Nicholas Magatti
     */
    private static char verticalCoordinateForUser(int verticalCoordinateForDeveloper){
        //row (with letters: A,B,C instead of 0,1,2)
        return  (char)((int) 'A' + verticalCoordinateForDeveloper);
    }

    /**
     * Translate horizontal coordinates for developers in coordinates for users (eg: from 0 to A, from 3 to D).
     * @param horizontalCoordinateForDeveloper
     * @return the coordinate for the user as a capital letter
     * @author Nicholas Magatti
     */
    private static char horizontalCoordinateForUser(int horizontalCoordinateForDeveloper){
        //column (1,2,3 instead of 0,1,2)
        return (char)((int) '1' + horizontalCoordinateForDeveloper);
    }

    /**
     * This is used to ask an user how to pay for something.
     *
     * @param costToPay cost he needs to pay
     * @param ammoInAmmoBox ammo that the player has in his ammo box
     * @param ammoInPowerUp power up he can use to pay the cost
     * @return a PaymentChoiceInfo which contains the user's choice
     * @author: Luca Iovine
     */
    public static PaymentChoiceInfo askPayment(int[] costToPay, int[] ammoInAmmoBox, List<PowerUpLM> ammoInPowerUp){
        boolean checkResult = true;
        possibleChoice = new ArrayList<>();
        paymentSB = new StringBuilder();
        responeForAmmo = new ArrayList<>();
        responseForPowerUp = new ArrayList<>();
        tmpAmmoInAmmoBox = new int[ammoInAmmoBox.length];
        ammoChosen = new int[ammoInAmmoBox.length];
        powerUpIdChosenList = new ArrayList<>();
        tmpCostToPay = new int[costToPay.length];
        tmpAmmoInPowerUp = new ArrayList<>();

        //Creazione liste temporanee per tenere traccia quantità "utilizzate" e del costo rimanente
        for(i = 0; i < costToPay.length; i++){
            tmpCostToPay[i] = costToPay[i];
        }

        for(i = 0; i < ammoInAmmoBox.length; i++){
            tmpAmmoInAmmoBox[i] = ammoInAmmoBox[i];
        }

        tmpAmmoInPowerUp.addAll(ammoInPowerUp);

        //Costruzione interazione utente
        msg = "You need to pay with ammo to proceed with this action. \n" +
                "You can use ammo from you ammo box or your powerups.\n\n" +
                "This is the cost you need to pay: ";
        paymentSB.append(msg);

        msg = ToolsView.costToString(costToPay) + "\n\n";
        paymentSB.append(msg);

        while(checkResult) {
            messageConstructor();
            System.out.print(paymentSB);

            String userChoice = ToolsView.readUserChoice(possibleChoice, false);

            if (userChoice != null) {
                if (responeForAmmo.contains(userChoice)) {
                    switch (userChoice) {
                        case red:
                            tmpCostToPay[GeneralInfo.RED_ROOM_ID]--;
                            tmpAmmoInAmmoBox[GeneralInfo.RED_ROOM_ID]--;
                            ammoChosen[GeneralInfo.RED_ROOM_ID]++;
                            break;
                        case blue:
                            tmpCostToPay[GeneralInfo.BLUE_ROOM_ID]--;
                            tmpAmmoInAmmoBox[GeneralInfo.BLUE_ROOM_ID]--;
                            ammoChosen[GeneralInfo.BLUE_ROOM_ID]++;
                            break;
                        case yellow:
                            tmpCostToPay[GeneralInfo.YELLOW_ROOM_ID]--;
                            tmpAmmoInAmmoBox[GeneralInfo.YELLOW_ROOM_ID]--;
                            ammoChosen[GeneralInfo.YELLOW_ROOM_ID]++;
                            break;
                    }
                    checkResult = checkIfNeedMore(tmpCostToPay);
                } else if (responseForPowerUp.contains(userChoice)) {
                    powerUpIdChosenList.add(tmpAmmoInPowerUp.get(Integer.parseInt(userChoice) - 1).getIdPowerUp());
                    tmpAmmoInPowerUp.remove(Integer.parseInt(userChoice) - 1);
                }
            }
            else
                return null;
        }
        return new PaymentChoiceInfo(ammoChosen, powerUpIdChosenList);
    }

    /**
     * Check how many ammo are needed to accomplish the request and if a color is done, you won't be
     * able to choose that color anymore.
     * @param costToPayToCheck
     * @return
     * @authro: Luca Iovine
     */
    private static boolean checkIfNeedMore(int[] costToPayToCheck){
        boolean result = false;

        if(costToPayToCheck[GeneralInfo.RED_ROOM_ID] > 0){
            result = true;
        }
        else{
            tmpAmmoInAmmoBox[GeneralInfo.RED_ROOM_ID] = 0;
            for(PowerUpLM power: tmpAmmoInPowerUp){
                if(power.getGainAmmoColor().equals(AmmoType.RED))
                    tmpAmmoInPowerUp.remove(power);
            }
        }

        if(costToPayToCheck[GeneralInfo.BLUE_ROOM_ID] > 0){
            result = true;
        }
        else{
            tmpAmmoInAmmoBox[GeneralInfo.BLUE_ROOM_ID] = 0;
            for(PowerUpLM power: tmpAmmoInPowerUp){
                if(power.getGainAmmoColor().equals(AmmoType.BLUE))
                    tmpAmmoInPowerUp.remove(power);
            }
        }

        if(costToPayToCheck[GeneralInfo.YELLOW_ROOM_ID] > 0){
            result = true;
        }
        else{
            tmpAmmoInAmmoBox[GeneralInfo.YELLOW_ROOM_ID] = 0;
            for(PowerUpLM power: tmpAmmoInPowerUp){
                if(power.getGainAmmoColor().equals(AmmoType.YELLOW))
                    tmpAmmoInPowerUp.remove(power);
            }
        }

        return result;
    }

    /**
     * Used to construct the interaction with the user.
     * It ask how the user are gonna pay the amount cost.
     *
     * @author: Luca Iovine
     */
    private static void messageConstructor(){
        msg = "These are the ammo you can spend.\n" +
                "Every time you select the corresponding color, the quantity will decrease: \n";

        paymentSB.append(msg);
        if (tmpAmmoInAmmoBox[GeneralInfo.RED_ROOM_ID] > 0) {
            msg = red + ": Red ammo available" + tmpAmmoInAmmoBox[GeneralInfo.RED_ROOM_ID] + "\n";
            paymentSB.append(msg);
            responeForAmmo.add(red);
        }
        if (tmpAmmoInAmmoBox[GeneralInfo.BLUE_ROOM_ID] > 0) {
            msg = blue + ": Blue ammo available" + tmpAmmoInAmmoBox[GeneralInfo.BLUE_ROOM_ID] + "\n";
            paymentSB.append(msg);
            responeForAmmo.add(blue);
        }
        if (tmpAmmoInAmmoBox[GeneralInfo.YELLOW_ROOM_ID] > 0) {
            msg = yellow + ": Yellow ammo available" + tmpAmmoInAmmoBox[GeneralInfo.YELLOW_ROOM_ID] + "\n";
            paymentSB.append(msg);
            responeForAmmo.add(yellow);
        }

        for (i = 0; i < tmpAmmoInPowerUp.size(); i++) {
            msg = (i + 1) + ": " + tmpAmmoInPowerUp.get(i).getName() + " COLOR: " + tmpAmmoInPowerUp.get(i).getGainAmmoColor() + "\n";
            paymentSB.append(msg);
            responseForPowerUp.add(Integer.toString(i + 1));
        }

        if (!responeForAmmo.isEmpty()) {
            possibleChoice.addAll(responeForAmmo);
            msg = "Chose a letter R/B/Y or ";
            paymentSB.append(msg);
        }

        possibleChoice.addAll(responseForPowerUp);
        msg = "choose a number from 1 to " + i + ": ";
        paymentSB.append(msg);
    }


    static void waitServerResponse(){
        try {
            Thread.sleep(5000);
        }catch(InterruptedException e){
            System.out.println("The application close due to an error.");
            System.exit(-1);
        }
    }
}
