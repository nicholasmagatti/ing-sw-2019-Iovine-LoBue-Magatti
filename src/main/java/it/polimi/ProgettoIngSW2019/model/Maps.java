package it.polimi.ProgettoIngSW2019.model;

import it.polimi.ProgettoIngSW2019.common.enums.AmmoType;
import it.polimi.ProgettoIngSW2019.common.utilities.GeneralInfo;

/**
 * Class that manages all the possible maps that can be chosen for the game
 * @author Nicholas Magatti
 */
public class Maps {
    //[][] indicate row and column respectively ([row][column])

    private Square[][][] maps = new Square[GeneralInfo.NUMBER_OF_MAPS][GeneralInfo.ROWS_MAP][GeneralInfo.COLUMNS_MAP];

    /**
     * Constructor that creates all the maps that will be shown to the first player,
     * so that he/she can choose a specific map to use for the game
     */
    public Maps(){

        //id rooms with spawn point
        final int RED_ROOM = AmmoType.intFromAmmoType(AmmoType.RED);
        final int BLUE_ROOM = AmmoType.intFromAmmoType(AmmoType.BLUE);
        final int YELLOW_ROOM = AmmoType.intFromAmmoType(AmmoType.YELLOW);
        //assign id (different from the previous ones) to the other rooms (the ones without spawn point)
        final int[] OTHER_ROOMS = new int[3];
        for(int id = 0, index = 0; index < OTHER_ROOMS.length; id++){
            if(id != RED_ROOM && id != BLUE_ROOM && id != YELLOW_ROOM){
                OTHER_ROOMS[index] = id;
                index++;
            }
        }
        final int GRAY_ROOM = OTHER_ROOMS[0];
        final int PURPLE_ROOM = OTHER_ROOMS[1];
        final int GREEN_ROOM = OTHER_ROOMS[2];

        //maps[0]
        maps[0][0][0] = new AmmoPoint(RED_ROOM, true, false, false, true);
        maps[0][0][1] = new AmmoPoint(BLUE_ROOM, true, false, false, false);
        maps[0][0][2] = new SpawningPoint(BLUE_ROOM, true, true, false, false);
        maps[0][0][3] = null;

        maps[0][1][0] = new SpawningPoint(RED_ROOM, false, true, false, true);
        maps[0][1][1] = new AmmoPoint(PURPLE_ROOM, false, false, false, true);
        maps[0][1][2] = new AmmoPoint(PURPLE_ROOM, false, false, true, false);
        maps[0][1][3] = new AmmoPoint(YELLOW_ROOM, true, true, false, false);

        maps[0][2][0] = new AmmoPoint(GRAY_ROOM, false, false, true, true);
        maps[0][2][1] = new AmmoPoint(GRAY_ROOM, false, false, true, false);
        maps[0][2][2] = new AmmoPoint(GRAY_ROOM, true, false, true, false);
        maps[0][2][3] = new SpawningPoint(YELLOW_ROOM, false, true, true, false);


        //maps[1]
        maps[1][0][0] = new AmmoPoint(BLUE_ROOM, true, false, false, true);
        maps[1][0][1] = new AmmoPoint(BLUE_ROOM, true, false, true, false);
        maps[1][0][2] = new SpawningPoint(BLUE_ROOM, true, true, false, false);
        maps[1][0][3] = null;

        maps[1][1][0] = new SpawningPoint(RED_ROOM, false, false, true, true);
        maps[1][1][1] = new AmmoPoint(RED_ROOM, true, false, false, false);
        maps[1][1][2] = new AmmoPoint(RED_ROOM, false, false, true, false);
        maps[1][1][3] = new AmmoPoint(YELLOW_ROOM, true, true, false, false);

        maps[1][2][0] = null;
        maps[1][2][1] = new AmmoPoint(GRAY_ROOM, false, false, true, true);
        maps[1][2][2] = new AmmoPoint(GRAY_ROOM, true, false, true, false);
        maps[1][2][3] = new SpawningPoint(YELLOW_ROOM, false, true, true, false);


        //maps[2]
        maps[2][0][0] = new AmmoPoint(BLUE_ROOM, true, false, false, true);
        maps[2][0][1] = new AmmoPoint(BLUE_ROOM, true, false, true, false);
        maps[2][0][2] = new SpawningPoint(BLUE_ROOM, true, false, false, false);
        maps[2][0][3] = new AmmoPoint(GREEN_ROOM, true, true, false, false);

        maps[2][1][0] = new SpawningPoint(RED_ROOM, false, false, true, true);
        maps[2][1][1] = new AmmoPoint(RED_ROOM, true, true, false, false);
        maps[2][1][2] = new AmmoPoint(YELLOW_ROOM, false, false, false, true);
        maps[2][1][3] = new AmmoPoint(YELLOW_ROOM, false, true, false, false);

        maps[2][2][0] = null;
        maps[2][2][1] = new AmmoPoint(GRAY_ROOM, false, false, true, true);
        maps[2][2][2] = new AmmoPoint(YELLOW_ROOM, false, false, true, false);
        maps[2][2][3] = new SpawningPoint(YELLOW_ROOM, false, true, true, false);


        //maps[3]
        maps[3][0][0] = new AmmoPoint(RED_ROOM, true, false, false, true);
        maps[3][0][1] = new AmmoPoint(BLUE_ROOM, true, false, false, false);
        maps[3][0][2] = new SpawningPoint(BLUE_ROOM, true, false, false, false);
        maps[3][0][3] = new AmmoPoint(GREEN_ROOM, true, true, false, false);

        maps[3][1][0] = new SpawningPoint(RED_ROOM, false, true, false, true);
        maps[3][1][1] = new AmmoPoint(PURPLE_ROOM, false, true, false, true);
        maps[3][1][2] = new AmmoPoint(YELLOW_ROOM, false, false, false, true);
        maps[3][1][3] = new AmmoPoint(YELLOW_ROOM, false, true, false, false);

        maps[3][2][0] = new AmmoPoint(GRAY_ROOM, false, false, true, true);
        maps[3][2][1] = new AmmoPoint(GRAY_ROOM, false, false, true, false);
        maps[3][2][2] = new AmmoPoint(YELLOW_ROOM, false, false, true, false);
        maps[3][2][3] = new SpawningPoint(YELLOW_ROOM, false, true, true, false);

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

