package it.polimi.ProgettoIngSW2019.common.Message.toView;

/**
 * @author Priscilla Lo Bue
 */
public class SpawnChoiceResponse extends PlayerChoiceResponse {
    private int x;
    private int y;


    public SpawnChoiceResponse(int idPlayer, int x, int y) {
        super(idPlayer);

        if(x < 0)
            throw new IllegalArgumentException("Coordinate x cannot be negative");

        if(y < 0)
            throw new IllegalArgumentException("Coordinate y cannot be negative");

        this.x = x;
        this.y = y;
    }


    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }
}
