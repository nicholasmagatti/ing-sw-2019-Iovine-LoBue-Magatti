package it.polimi.ProgettoIngSW2019.common.Message.toView;


import java.util.ArrayList;
import java.util.List;

/**
 * MessageScorePlayer class
 * the information of the players who hits the dead player
 * @author Priscilla Lo Bue
 */
public class MessageScorePlayer extends InfoResponse {
    private String deadNamePlayer;
    private String killerNamePlayer;
    private String firstBloodNamePlayer;
    private List<ScorePlayerWhoHit> playersWhoHit;
    private int nSkullsDeadPlayer;


    /**
     * Constructor
     * message from controller if there are dead players
     * @param idPlayer                  id current player(who kills)
     * @param deadNamePlayer            name of the player dead
     * @param killerNamePlayer          name of the killer
     * @param firstBloodNamePlayer      name of the player who did first blood
     */
    public MessageScorePlayer(int idPlayer, String deadNamePlayer, String killerNamePlayer, String firstBloodNamePlayer, List<ScorePlayerWhoHit> playersWhoHit, int nSkullsDeadPlayer) {
        super(idPlayer);

        if(deadNamePlayer == null)
            throw new NullPointerException("deadNamePlayer cannot be null");
        if(killerNamePlayer == null)
            throw new NullPointerException("killerNamePlayer cannot be null");
        if(firstBloodNamePlayer == null)
            throw new NullPointerException("firstBloodNamePlayer cannot be null");
        if(playersWhoHit == null)
            throw new NullPointerException("playerWhoHit cannot be null");

        this.deadNamePlayer = deadNamePlayer;
        this.killerNamePlayer = killerNamePlayer;
        this.firstBloodNamePlayer = firstBloodNamePlayer;
        this.playersWhoHit = new ArrayList<>(playersWhoHit);
        this.nSkullsDeadPlayer = nSkullsDeadPlayer;
    }


    /**
     * @return      name of the dead player
     */
    public String getDeadNamePlayer() {
        return deadNamePlayer;
    }


    /**
     * @return      number of skulls of the dead player
     */
    public int getnSkullsDeadPlayer() {
        return nSkullsDeadPlayer;
    }


    /**
     * @return      name of the player who makes the first blood
     */
    public String getFirstBloodNamePlayer() {
        return firstBloodNamePlayer;
    }


    /**
     * @return      name of the killer player
     */
    public String getKillerNamePlayer() {
        return killerNamePlayer;
    }


    /**
     * @return      list of score for each player who hit the dead player
     */
    public List<ScorePlayerWhoHit> getPlayersWhoHit() {
        return playersWhoHit;
    }
}
