package it.polimi.ProgettoIngSW2019.common.Message.toView;

/**
 * Common Message request from view when a player does an action
 * @author Priscilla Lo Bue
 */
public class PlayerChoiceResponse {
    private int idPlayer;

    /**
     * Constructor
     * @param idPlayer      id Player
     */
    public PlayerChoiceResponse(int idPlayer) {
        if(idPlayer < 0)
            throw new IllegalArgumentException("IdPlayer cannot be negative");
        this.idPlayer = idPlayer;
    }


    /**
     * get the id of the player who wants to do an action
     * @return         id of the player
     */
    public int getIdPlayer() {
        return idPlayer;
    }
}
