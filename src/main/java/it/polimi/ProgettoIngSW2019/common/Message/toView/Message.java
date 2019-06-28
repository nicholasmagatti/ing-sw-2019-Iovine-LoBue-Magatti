package it.polimi.ProgettoIngSW2019.common.Message.toView;

/**
 * General message from controller to View
 * @author Priscilla Lo Bue
 */
public class Message extends InfoResponse {
    private String namePlayer;

    /**
     * Constructor
     * @param idPlayer      id player
     * @param namePlayer    name of the player
     */
    public Message(int idPlayer, String namePlayer) {
        super(idPlayer);
        if(namePlayer == null)
            throw new NullPointerException("Name player cannot be null");

        this.namePlayer = namePlayer;
    }


    /**
     * get the name of the player
     * @return      name of the player
     */
    public String getNamePlayer() {
        return namePlayer;
    }
}
