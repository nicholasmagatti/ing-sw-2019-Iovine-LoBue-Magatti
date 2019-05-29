package it.polimi.ProgettoIngSW2019.common.Message.toView;

/**
 * @author Priscilla Lo Bue
 */
public class PlayerChoiceResponse {
    private int idPlayer;

    public PlayerChoiceResponse(int idPlayer) {
        if(idPlayer < 0)
            throw new IllegalArgumentException("IdPlayer cannot be negative");
        this.idPlayer = idPlayer;
    }


    public int getIdPlayer() {
        return idPlayer;
    }
}
