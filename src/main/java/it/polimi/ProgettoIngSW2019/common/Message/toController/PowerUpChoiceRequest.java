package it.polimi.ProgettoIngSW2019.common.Message.toController;


public class PowerUpChoiceRequest extends PlayerChoiceRequest {
    private int idPowerUp;


    public PowerUpChoiceRequest(String hostName, int idPlayer, int idPowerUp) {
        super(hostName, idPlayer);

        this.idPowerUp = idPowerUp;
    }


    public int getIdPowerUp() {
        return idPowerUp;
    }
}
