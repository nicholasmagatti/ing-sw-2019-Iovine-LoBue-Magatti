package it.polimi.ProgettoIngSW2019.common.Message;

/**
 * Info Class
 * used to request end send info of cards, players...
 * @author Prscilla Lo Bue
 */
public abstract class Info {
    int idPlayer;


    /**
     * get the id of the player
     * @return  id player
     */
    public int getIdPlayer() {
        return idPlayer;
    }
}
