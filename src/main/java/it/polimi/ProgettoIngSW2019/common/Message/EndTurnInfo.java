package it.polimi.ProgettoIngSW2019.common.Message;


import java.util.List;

/**
 * EndTurnInfo class
 * the information of the players who hits the dead player
 */
public class EndTurnInfo extends Info {
    private String deadNamePlayer;
    private String killerNamePlayer;
    private String firstBloodNamePlayer;
    private boolean doubleKill;
    private List<ScoreInfo> playersWhoHit;
    //messages score


    /**
     * Constructor
     * message from view
     * message from controller if there aren't dead players
     * @param idPlayer  idPlayer end turn (who kills)
     */
    public EndTurnInfo(int idPlayer) {
        super(idPlayer);
    }


    /**
     * Constructor
     * message from controller if there are dead players
     * @param idPlayer                  id current player(who kills)
     * @param deadNamePlayer            name of the player dead
     * @param killerNamePlayer          name of the killer
     * @param firstBloodNamePlayer      name of the player who did first blood
     * @param doubleKill                boolean if the killer did more of 1 kill
     */
    public EndTurnInfo(int idPlayer, String deadNamePlayer, String killerNamePlayer, String firstBloodNamePlayer, boolean doubleKill, List<ScoreInfo> playersWhoHit) {
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
        this.doubleKill = doubleKill;
        this.playersWhoHit = playersWhoHit;
    }
}
