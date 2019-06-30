package it.polimi.ProgettoIngSW2019.common.utilities;

import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;

/**
 * Class that contains the constants needed.
 * It only contains public static final attributes, with no methods.
 * @author Nicholas Magatti
 */
public abstract class GeneralInfo {

    public static final int NUMBER_OF_MAPS = 4;
    public static final int ROWS_MAP = 3;
    public static final int COLUMNS_MAP = 4;
    public static final int DAMAGE_TO_KILL = 11;
    public static final int DAMAGE_TO_OVERKILL = 12;
    public static final int MIN_SKULLS = 5;
    public static final int MAX_SKULLS = 8;
    public static final int MIN_NUM_PLAYERS = 3;
    public static final int MAX_NUM_PLAYERS = 5;
    public static final int ACTIONS_PER_TURN = 2;

    public static final int RED_ROOM_ID = 0;
    public static final int BLUE_ROOM_ID = 1;
    public static final int YELLOW_ROOM_ID = 2;
    public static final int GRAY_ROOM_ID = 3;
    public static final int PURPLE_ROOM_ID = 4;
    public static final int GREEN_ROOM_ID = 5;

    //names of powerups
    public static final String TAGBACK_GRENADE = "TAGBACK GRENADE";
    public static final String NEWTON = "NEWTON";
    public static final String TARGETING_SCOPE = "TARGETING SCOPE";
    public static final String TELEPORTER = "TELEPORTER";

    //for view
    public static final String YES_COMMAND = "yes";
    public static final String NO_COMMAND = "no";
    /**
     * @deprecated
     */
    public static final String EXIT_COMMAND = "exit";
    public static final String MOVE_COMMAND = "move";
    public static final String GRAB_COMMAND = "grab";
    public static final String SHOOT_COMMAND = "shoot";
    public static final String EXIT_EXPLANATION = "exit from game";
    public static final String PREFIX_COMMAND_DESCRIPTION = "description ";
    public static final char[] ILLEGAL_CHARACTERS_FOR_USERNAME = {'.', ':', ',', ';'};
    public static final String ASK_INPUT = "Type the chosen option: ";
}
