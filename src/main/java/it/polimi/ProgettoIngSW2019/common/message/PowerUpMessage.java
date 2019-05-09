package it.polimi.ProgettoIngSW2019.common.message;

public class PowerUpMessage extends UserInputMessage {
    private int idPlayer;
    private int idPowerUp;

    public int getIdPlayer() {
        return idPlayer;
    }

    public int getIdPowerUp() {
        return idPowerUp;
    }
}
