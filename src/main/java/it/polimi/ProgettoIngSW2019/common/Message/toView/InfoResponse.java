package it.polimi.ProgettoIngSW2019.common.Message.toView;

public class InfoResponse {
    private int idPlayer;



    /**
     * constructor
     * @param idPlayer  id player
     */
    public InfoResponse(int idPlayer) {
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