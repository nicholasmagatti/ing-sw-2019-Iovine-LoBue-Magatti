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

    public static final int RED_ROOM_ID = 0;
    public static final int BLUE_ROOM_ID = 1;
    public static final int YELLOW_ROOM_ID = 2;
    public static final int GRAY_ROOM_ID = 3;
    public static final int PURPLE_ROOM_ID = 4;
    public static final int GREEN_ROOM_ID = 5;

    public static final String NO = "no";
    public static final String EXIT = "exit";
    public static final String DESCRIPTION_EXIT = "exit from game";
    public static final String PREFIX_COMMAND_DESCRIPTION = "description ";

}
