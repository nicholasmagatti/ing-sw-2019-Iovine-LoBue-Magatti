package it.polimi.ProgettoIngSW2019.model;

import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;

/**
 * Class that manages all the possible maps that can be chosen for the game
 * @author Nicholas Magatti
 */
public class Maps {
    //[][][] indicate row and column respectively ([map][row][column])

    private Square[][][] maps = new Square[GeneralInfo.NUMBER_OF_MAPS][GeneralInfo.ROWS_MAP][GeneralInfo.COLUMNS_MAP];

    /**
     * Constructor that creates all the maps that will be shown to the first player,
     * so that he/she can choose a specific map to use for the game
     */
    public Maps(){

        //maps[0]
        maps[0][0][0] = new AmmoPoint(GeneralInfo.RED_ROOM_ID, true, false, false, true);
        maps[0][0][1] = new AmmoPoint(GeneralInfo.BLUE_ROOM_ID, true, false, false, false);
        maps[0][0][2] = new SpawningPoint(GeneralInfo.BLUE_ROOM_ID, true, true, false, false);
        maps[0][0][3] = null;

        maps[0][1][0] = new SpawningPoint(GeneralInfo.RED_ROOM_ID, false, true, false, true);
        maps[0][1][1] = new AmmoPoint(GeneralInfo.PURPLE_ROOM_ID, false, false, false, true);
        maps[0][1][2] = new AmmoPoint(GeneralInfo.PURPLE_ROOM_ID, false, false, true, false);
        maps[0][1][3] = new AmmoPoint(GeneralInfo.YELLOW_ROOM_ID, true, true, false, false);

        maps[0][2][0] = new AmmoPoint(GeneralInfo.GRAY_ROOM_ID, false, false, true, true);
        maps[0][2][1] = new AmmoPoint(GeneralInfo.GRAY_ROOM_ID, false, false, true, false);
        maps[0][2][2] = new AmmoPoint(GeneralInfo.GRAY_ROOM_ID, true, false, true, false);
        maps[0][2][3] = new SpawningPoint(GeneralInfo.YELLOW_ROOM_ID, false, true, true, false);


        //maps[1]
        maps[1][0][0] = new AmmoPoint(GeneralInfo.BLUE_ROOM_ID, true, false, false, true);
        maps[1][0][1] = new AmmoPoint(GeneralInfo.BLUE_ROOM_ID, true, false, true, false);
        maps[1][0][2] = new SpawningPoint(GeneralInfo.BLUE_ROOM_ID, true, true, false, false);
        maps[1][0][3] = null;

        maps[1][1][0] = new SpawningPoint(GeneralInfo.RED_ROOM_ID, false, false, true, true);
        maps[1][1][1] = new AmmoPoint(GeneralInfo.RED_ROOM_ID, true, false, false, false);
        maps[1][1][2] = new AmmoPoint(GeneralInfo.RED_ROOM_ID, false, false, true, false);
        maps[1][1][3] = new AmmoPoint(GeneralInfo.YELLOW_ROOM_ID, true, true, false, false);

        maps[1][2][0] = null;
        maps[1][2][1] = new AmmoPoint(GeneralInfo.GRAY_ROOM_ID, false, false, true, true);
        maps[1][2][2] = new AmmoPoint(GeneralInfo.GRAY_ROOM_ID, true, false, true, false);
        maps[1][2][3] = new SpawningPoint(GeneralInfo.YELLOW_ROOM_ID, false, true, true, false);


        //maps[2]
        maps[2][0][0] = new AmmoPoint(GeneralInfo.BLUE_ROOM_ID, true, false, false, true);
        maps[2][0][1] = new AmmoPoint(GeneralInfo.BLUE_ROOM_ID, true, false, true, false);
        maps[2][0][2] = new SpawningPoint(GeneralInfo.BLUE_ROOM_ID, true, false, false, false);
        maps[2][0][3] = new AmmoPoint(GeneralInfo.GREEN_ROOM_ID, true, true, false, false);

        maps[2][1][0] = new SpawningPoint(GeneralInfo.RED_ROOM_ID, false, false, true, true);
        maps[2][1][1] = new AmmoPoint(GeneralInfo.RED_ROOM_ID, true, true, false, false);
        maps[2][1][2] = new AmmoPoint(GeneralInfo.YELLOW_ROOM_ID, false, false, false, true);
        maps[2][1][3] = new AmmoPoint(GeneralInfo.YELLOW_ROOM_ID, false, true, false, false);

        maps[2][2][0] = null;
        maps[2][2][1] = new AmmoPoint(GeneralInfo.GRAY_ROOM_ID, false, false, true, true);
        maps[2][2][2] = new AmmoPoint(GeneralInfo.YELLOW_ROOM_ID, false, false, true, false);
        maps[2][2][3] = new SpawningPoint(GeneralInfo.YELLOW_ROOM_ID, false, true, true, false);


        //maps[3]
        maps[3][0][0] = new AmmoPoint(GeneralInfo.RED_ROOM_ID, true, false, false, true);
        maps[3][0][1] = new AmmoPoint(GeneralInfo.BLUE_ROOM_ID, true, false, false, false);
        maps[3][0][2] = new SpawningPoint(GeneralInfo.BLUE_ROOM_ID, true, false, false, false);
        maps[3][0][3] = new AmmoPoint(GeneralInfo.GREEN_ROOM_ID, true, true, false, false);

        maps[3][1][0] = new SpawningPoint(GeneralInfo.RED_ROOM_ID, false, true, false, true);
        maps[3][1][1] = new AmmoPoint(GeneralInfo.PURPLE_ROOM_ID, false, true, false, true);
        maps[3][1][2] = new AmmoPoint(GeneralInfo.YELLOW_ROOM_ID, false, false, false, true);
        maps[3][1][3] = new AmmoPoint(GeneralInfo.YELLOW_ROOM_ID, false, true, false, false);

        maps[3][2][0] = new AmmoPoint(GeneralInfo.GRAY_ROOM_ID, false, false, true, true);
        maps[3][2][1] = new AmmoPoint(GeneralInfo.GRAY_ROOM_ID, false, false, true, false);
        maps[3][2][2] = new AmmoPoint(GeneralInfo.YELLOW_ROOM_ID, false, false, true, false);
        maps[3][2][3] = new SpawningPoint(GeneralInfo.YELLOW_ROOM_ID, false, true, true, false);

    }

    /**
     * Get an array of all the possible maps of the game. To access to a specific map, use getMaps()[i], where
     * i is the index
     * @return array of all the maps
     */
    public Square[][][] getMaps() {
        return maps;
    }
}

