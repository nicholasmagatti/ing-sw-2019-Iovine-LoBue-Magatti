package it.polimi.ProgettoIngSW2019.common.Message;

/**
 * ScoreInfo class
 * correlation of player who hits with the damage he did to a specific dead player
 * @author Priscilla Lo Bue
 */
public class ScoreInfo {
    private String namePlayerWhoHit;
    private int nPoints;


    /**
     * Constructor
     * @param namePlayerWhoHit  name of the player who hit
     * @param nPoints           number of point the player gain for his hit
     */
    public ScoreInfo(String namePlayerWhoHit, int nPoints) {
        if(namePlayerWhoHit == null)
            throw new NullPointerException("namePlayerWhoHit cannot be null");
        this.namePlayerWhoHit = namePlayerWhoHit;
        this.nPoints = nPoints;
    }
}
