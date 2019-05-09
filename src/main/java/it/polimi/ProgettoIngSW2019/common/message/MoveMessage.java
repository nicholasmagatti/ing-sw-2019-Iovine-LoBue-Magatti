package it.polimi.ProgettoIngSW2019.common.message;

public class MoveMessage extends UserInputMessage {
    private int idPlayer;
    private int[] coordinates;

    public int getIdPlayer() {
        return idPlayer;
    }

    public int[] getCoordinates() {
        return coordinates;
    }
}
