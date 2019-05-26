package it.polimi.ProgettoIngSW2019.model;

/**
 * Class that manages all the possible maps that can be chosen for the game
 * @author Nicholas Magatti
 */
public class Maps {
    //[][] indicate row and column respectively ([row][column])
    private final int ROWS = 3;
    private final int COLUMNS = 4;
    private final int NUMBER_OF_MAPS = 4;

    private Square[][][] maps = new Square[NUMBER_OF_MAPS][ROWS][COLUMNS];

    /**
     * Constructor that creates all the maps that will be shown to the first player,
     * so that he/she can choose a specific map to use for the game
     */
    public Maps(){

        //maps[0]
        maps[0][0][0] = new AmmoPoint(1, true, false, false, true);
        maps[0][0][1] = new AmmoPoint(3, true, false, false, false);
        maps[0][0][2] = new SpawningPoint(3, true, true, false, false);
        maps[0][0][3] = null;

        maps[0][1][0] = new SpawningPoint(1, false, true, false, true);
        maps[0][1][1] = new AmmoPoint(2, false, false, false, true);
        maps[0][1][2] = new AmmoPoint(2, false, false, true, false);
        maps[0][1][3] = new AmmoPoint(4, true, true, false, false);

        maps[0][2][0] = new AmmoPoint(0, false, false, true, true);
        maps[0][2][1] = new AmmoPoint(0, false, false, true, false);
        maps[0][2][2] = new AmmoPoint(0, true, false, true, false);
        maps[0][2][3] = new SpawningPoint(4, false, true, true, false);


        //maps[1]
        maps[1][0][0] = new AmmoPoint(3, true, false, false, true);
        maps[1][0][1] = new AmmoPoint(3, true, false, true, false);
        maps[1][0][2] = new SpawningPoint(3, true, true, false, false);
        maps[1][0][3] = null;

        maps[1][1][0] = new SpawningPoint(1, false, false, true, true);
        maps[1][1][1] = new AmmoPoint(1, true, false, false, false);
        maps[1][1][2] = new AmmoPoint(1, false, false, true, false);
        maps[1][1][3] = new AmmoPoint(4, true, true, false, false);

        maps[1][2][0] = null;
        maps[1][2][1] = new AmmoPoint(0, false, false, true, true);
        maps[1][2][2] = new AmmoPoint(0, true, false, true, false);
        maps[1][2][3] = new SpawningPoint(4, false, true, true, false);


        //maps[2]
        maps[2][0][0] = new AmmoPoint(3, true, false, false, true);
        maps[2][0][1] = new AmmoPoint(3, true, false, true, false);
        maps[2][0][2] = new SpawningPoint(3, true, false, false, false);
        maps[2][0][3] = new AmmoPoint(5, true, true, false, false);

        maps[2][1][0] = new SpawningPoint(1, false, false, true, true);
        maps[2][1][1] = new AmmoPoint(1, true, true, false, false);
        maps[2][1][2] = new AmmoPoint(4, false, false, false, true);
        maps[2][1][3] = new AmmoPoint(4, false, true, false, false);

        maps[2][2][0] = null;
        maps[2][2][1] = new AmmoPoint(0, false, false, true, true);
        maps[2][2][2] = new AmmoPoint(4, false, false, true, false);
        maps[2][2][3] = new SpawningPoint(4, false, true, true, false);


        //maps[3]
        maps[3][0][0] = new AmmoPoint(1, true, false, false, true);
        maps[3][0][1] = new AmmoPoint(3, true, false, false, false);
        maps[3][0][2] = new SpawningPoint(3, true, false, false, false);
        maps[3][0][3] = new AmmoPoint(5, true, true, false, false);

        maps[3][1][0] = new SpawningPoint(1, false, true, false, true);
        maps[3][1][1] = new AmmoPoint(2, false, true, false, true);
        maps[3][1][2] = new AmmoPoint(4, false, false, false, true);
        maps[3][1][3] = new AmmoPoint(4, false, true, false, false);

        maps[3][2][0] = new AmmoPoint(0, false, false, true, true);
        maps[3][2][1] = new AmmoPoint(0, false, false, true, false);
        maps[3][2][2] = new AmmoPoint(4, false, false, true, false);
        maps[3][2][3] = new SpawningPoint(4, false, true, true, false);

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

