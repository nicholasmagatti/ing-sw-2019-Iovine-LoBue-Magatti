package it.polimi.ProgettoIngSW2019.view;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;
import it.polimi.ProgettoIngSW2019.common.utilities.InputScanner;

import java.util.List;
import java.util.Scanner;

/**
 * @author Nicholas Magatti
 */
public abstract class ToolsView {

    private static InputScanner inputScanner = new InputScanner();


    /**
     * Return the input scanner of this client.
     * @return the input scanner of this client
     */
    static InputScanner getInputScanner() {
        return inputScanner;
    }

    static String costToString(int[] cost){
        //example of output: 2 red, 1 blue, 0 yellow
        return cost[AmmoType.intFromAmmoType(AmmoType.RED)] + " red, " +
                cost[AmmoType.intFromAmmoType(AmmoType.BLUE)] + " blue, " +
                cost[AmmoType.intFromAmmoType(AmmoType.YELLOW)] + " yellow";
    }

    /**
     * Return true if the string is a request from the user to visualize the description of a card.
     * @param string
     * @return true if the string is a request from the user to visualize the description of a card
     */
    static boolean isDescriptionCommand(String string){
        return stringStartsWith(string, GeneralInfo.PREFIX_COMMAND_DESCRIPTION);
    }

    /**
     * Return true if the string 'string' start with the string 'prefix', false otherwise.
     * @param string - string to parse
     * @param prefix - possible prefix of the string to parse
     * @return true if the string 'string' start with the string 'prefix', false otherwise
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
     * Keep reading input from user util he/she gives the requested result or
     * until the timer expire, in which case return null, return the correct input otherwise.
     * If the //TODO finish this javadoc
     */
    static String readUserChoice(List<String> allowedAnswers, boolean descriptionEnabled){
        boolean exit = false;
        String inputFromUser;
        do {
            inputScanner.read();
            inputFromUser = inputScanner.getInputValue();
            if(allowedAnswers.contains(inputFromUser)){
                exit = true;
            }
            else{
                if(descriptionEnabled && inputFromUser.contains(GeneralInfo.PREFIX_COMMAND_DESCRIPTION)){
                    //TODO
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
            return inputFromUser;
        }
    }

}
