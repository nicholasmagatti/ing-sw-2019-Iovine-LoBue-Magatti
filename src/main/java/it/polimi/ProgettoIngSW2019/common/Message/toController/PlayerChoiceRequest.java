package it.polimi.ProgettoIngSW2019.common.Message.toController;

/**
 * Player choice class
 * message with info of the player choice
 * @author Priscilla Lo Bue
 */
public class PlayerChoiceRequest {
    private String hostNamePlayer;
    private int idPlayer;


    /**
     * Constructor
     * @param idPlayer          id player
     * @param hostNamePlayer    host name
     */
    public PlayerChoiceRequest(String hostNamePlayer, int idPlayer) {
        if((hostNamePlayer == null) || (hostNamePlayer.equals("")))
            throw new IllegalArgumentException("Host name client cannot be null or empty");

        if(idPlayer < 0)
            throw new IllegalArgumentException("The idPlayer cannot be negative");

        this.idPlayer = idPlayer;
        this.hostNamePlayer = hostNamePlayer;
    }



    /**
     * get the id of the player
     * @return id player
     */
    public int getIdPlayer() {
        return idPlayer;
    }



    /**
     * get the host name of the client sender
     * @return      host name
     */
    public String getHostNamePlayer() {
        return hostNamePlayer;
    }
}
