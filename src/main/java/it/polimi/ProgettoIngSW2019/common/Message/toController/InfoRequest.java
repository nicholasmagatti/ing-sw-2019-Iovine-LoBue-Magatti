package it.polimi.ProgettoIngSW2019.common.Message.toController;

/**
 * InfoRequest Class
 * used to request end send info of cards, players...
 */
public class InfoRequest {
    private int idPlayer;



    /**
     * constructor
     * @param idPlayer  id player
     */
    public InfoRequest(int idPlayer) {
        if(idPlayer < 0)
            throw new IllegalArgumentException("IdPlayer cannot be negative");
        this.idPlayer = idPlayer;
    }



    /**
     * get the id of the player
     * @return  id player
     */
    public int getIdPlayer() {
        return idPlayer;
    }
}
