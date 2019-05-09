package it.polimi.ProgettoIngSW2019.common.message;

public class SpawnMessage extends UserInputMessage {
    private int idPlayer;
    private int idPowerUp;
    private int[] coordinates;

    public int getIdPlayer() {
        return idPlayer;
    }

    public int getIdPowerUp() {
        return idPowerUp;
    }

    public int[] getCoordinates() {
        return coordinates;
    }
}
