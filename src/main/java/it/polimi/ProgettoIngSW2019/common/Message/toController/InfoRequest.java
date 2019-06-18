package it.polimi.ProgettoIngSW2019.common.Message.toController;

/**
 * InfoRequest Class
 * used to request end send info of cards, players...
 */
public class InfoRequest {
    private String hostNamePlayer;
    private int idPlayer;



    /**
     * constructor
     * @param hostNamePlayer    host name client
     * @param idPlayer          id player
     */
    public InfoRequest(String hostNamePlayer, int idPlayer) {
        if((hostNamePlayer == null) || (hostNamePlayer.equals("")))
            throw new IllegalArgumentException("Host name client cannot be null or empty");

        if(idPlayer < 0)
            throw new IllegalArgumentException("IdPlayer cannot be negative");

        this.idPlayer = idPlayer;
        this.hostNamePlayer = hostNamePlayer;
    }



    /**
     * get the id of the player
     * @return  id player
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
