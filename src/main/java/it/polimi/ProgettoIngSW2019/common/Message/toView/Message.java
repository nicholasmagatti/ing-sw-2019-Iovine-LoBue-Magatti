package it.polimi.ProgettoIngSW2019.common.Message.toView;

/**
 * @author Priscilla Lo Bue
 */
public class Message extends InfoResponse {
    private String namePlayer;

    public Message(int idPlayer, String namePlayer) {
        super(idPlayer);
        if(namePlayer == null)
            throw new NullPointerException("Name player cannot be null");

        this.namePlayer = namePlayer;
    }


    public String getNamePlayer() {
        return namePlayer;
    }
}
