package it.polimi.ProgettoIngSW2019.common.Message;


import java.util.List;

/**
 * ScorePlayersInfoResponse class
 * the information of the players who hits the dead player
 */
public class ScorePlayersInfoResponse extends Info {
    private String deadNamePlayer;
    private String killerNamePlayer;
    private String firstBloodNamePlayer;
    private List<ScoreInfo> playersWhoHit;
    private int nSkullsDeadPlayer;


    /**
     * Constructor
     * message from controller if there are dead players
     * @param idPlayer                  id current player(who kills)
     * @param deadNamePlayer            name of the player dead
     * @param killerNamePlayer          name of the killer
     * @param firstBloodNamePlayer      name of the player who did first blood
     */
    public ScorePlayersInfoResponse(int idPlayer, String deadNamePlayer, String killerNamePlayer, String firstBloodNamePlayer, List<ScoreInfo> playersWhoHit, int nSkullsDeadPlayer) {
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
        this.playersWhoHit = playersWhoHit;
        this.nSkullsDeadPlayer = nSkullsDeadPlayer;
    }


    public String getDeadNamePlayer() {
        return deadNamePlayer;
    }


    public int getnSkullsDeadPlayer() {
        return nSkullsDeadPlayer;
    }


    public String getFirstBloodNamePlayer() {
        return firstBloodNamePlayer;
    }


    public String getKillerNamePlayer() {
        return killerNamePlayer;
    }


    public List<ScoreInfo> getPlayersWhoHit() {
        return playersWhoHit;
    }
}
