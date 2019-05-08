package it.polimi.ProgettoIngSW2019.model;

/**
 * Maps that manages all the possible maps that can be chosen for the game
 * @author Nicholas Magatti
 */
//TODO: add to uml
public class Maps {
    //[][] indicate row and column respectively ([row][column])
    private final int rows = 3;
    private final int columns = 4;

    private Square[][] map1 = new Square[rows][columns];
    private Square[][] map2 = new Square[rows][columns];
    private Square[][] map3 = new Square[rows][columns];
    private Square[][] map4 = new Square[rows][columns];

    /**
     * Constructor that creates all the maps that will be shown to the first player,
     * so that he/she can choose a specific map to use for the game
     */
    public Maps(){

        //map1
        map1[0][0] = new AmmoPoint(1, true, false, false, true);
        map1[0][1] = new AmmoPoint(3, true, false, false, false);
        map1[0][2] = new SpawningPoint(3, true, true, false, false);
        map1[0][3] = null;

        map1[1][0] = new SpawningPoint(1, false, true, false, true);
        map1[1][1] = new AmmoPoint(2, false, false, false, true);
        map1[1][2] = new AmmoPoint(2, false, false, true, false);
        map1[1][3] = new AmmoPoint(4, true, true, false, false);

        map1[2][0] = new AmmoPoint(0, false, false, true, true);
        map1[2][1] = new AmmoPoint(0, false, false, true, false);
        map1[2][2] = new AmmoPoint(0, true, false, true, false);
        map1[2][3] = new SpawningPoint(4, false, true, true, false);


        //map2
        map2[0][0] = new AmmoPoint(3, true, false, false, true);
        map2[0][1] = new AmmoPoint(3, true, false, true, false);
        map2[0][2] = new SpawningPoint(3, true, true, false, false);
        map2[0][3] = null;

        map2[1][0] = new SpawningPoint(1, false, false, true, true);
        map2[1][1] = new AmmoPoint(1, true, false, false, false);
        map2[1][2] = new AmmoPoint(1, false, false, true, false);
        map2[1][3] = new AmmoPoint(4, true, true, false, false);

        map2[2][0] = null;
        map2[2][1] = new AmmoPoint(0, false, false, true, true);
        map2[2][2] = new AmmoPoint(0, true, false, true, false);
        map2[2][3] = new SpawningPoint(4, false, true, true, false);


        //map3
        map3[0][0] = new AmmoPoint(3, true, false, false, true);
        map3[0][1] = new AmmoPoint(3, true, false, true, false);
        map3[0][2] = new SpawningPoint(3, true, false, false, false);
        map3[0][3] = new AmmoPoint(5, true, true, false, false);

        map3[1][0] = new SpawningPoint(1, false, false, true, true);
        map3[1][1] = new AmmoPoint(1, true, true, false, false);
        map3[1][2] = new AmmoPoint(4, false, false, false, true);
        map3[1][3] = new AmmoPoint(4, false, true, false, false);

        map3[2][0] = null;
        map3[2][1] = new AmmoPoint(0, false, false, true, true);
        map3[2][2] = new AmmoPoint(4, false, false, true, false);
        map3[2][3] = new SpawningPoint(4, false, true, true, false);


        //map4
        map4[0][0] = new AmmoPoint(1, true, false, false, true);
        map4[0][1] = new AmmoPoint(3, true, false, false, false);
        map4[0][2] = new SpawningPoint(3, true, false, false, false);
        map4[0][3] = new AmmoPoint(5, true, true, false, false);

        map4[1][0] = new SpawningPoint(1, false, true, false, true);
        map4[1][1] = new AmmoPoint(2, false, true, false, true);
        map4[1][2] = new AmmoPoint(4, false, false, false, true);
        map4[1][3] = new AmmoPoint(4, false, true, false, false);

        map4[2][0] = new AmmoPoint(0, false, false, true, true);
        map4[2][1] = new AmmoPoint(0, false, false, true, false);
        map4[2][2] = new AmmoPoint(4, false, false, true, false);
        map4[2][3] = new SpawningPoint(4, false, true, true, false);

    }

    /**
     * Return the map1
     * @return map1
     */
    public Square[][] getMap1() {
        return map1;
    }

    /**
     * Return the map2
     * @return map2
     */
    public Square[][] getMap2() {
        return map2;
    }

    /**
     * Return the map3
     * @return map3
     */
    public Square[][] getMap3() {
        return map3;
    }

    /**
     * Return the map4
     * @return map4
     */
    public Square[][] getMap4() {
        return map4;
    }
}
