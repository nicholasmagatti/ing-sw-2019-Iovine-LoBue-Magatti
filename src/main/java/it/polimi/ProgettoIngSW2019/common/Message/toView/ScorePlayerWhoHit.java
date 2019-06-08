package it.polimi.ProgettoIngSW2019.common.Message.toView;

/**
 * ScorePlayerWhoHit class
 * correlation of player who hits with the damage he did to a specific dead player
 * @author Priscilla Lo Bue
 */
public class ScorePlayerWhoHit {
    private String namePlayerWhoHit;
    private int nPoints;


    /**
     * Constructor
     * @param namePlayerWhoHit  name of the player who hit
     * @param nPoints           number of point the player gain for his hit
     */
    public ScorePlayerWhoHit(String namePlayerWhoHit, int nPoints) {
        if(namePlayerWhoHit == null)
            throw new NullPointerException("namePlayerWhoHit cannot be null");
        this.namePlayerWhoHit = namePlayerWhoHit;
        this.nPoints = nPoints;
    }


    /**
     * get the number of points the player did
     * @return  get number of points
     */
    public int getnPoints() {
        return nPoints;
    }


    /**
     * get name of the player who hit the dead player
     * @return  get name player
     */
    public String getNamePlayerWhoHit() {
        return namePlayerWhoHit;
    }
}
