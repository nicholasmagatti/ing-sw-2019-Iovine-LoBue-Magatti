package it.polimi.ProgettoIngSW2019.common.Message;

/**
 * Player choice class
 * message with info of the player choice
 * @author Priscilla Lo Bue
 */
public abstract class PlayerChoice {
    private int idPlayer;


    /**
     * Constructor
     * @param idPlayer  id player
     */
    public PlayerChoice(int idPlayer) {
        if(idPlayer < 0)
            throw new IllegalArgumentException("The idPlayer cannot be negative");
        this.idPlayer = idPlayer;
    }



    /**
     * get the id of the player
     * @return id player
     */
    public int getIdPlayer() {
        return idPlayer;
    }
}
