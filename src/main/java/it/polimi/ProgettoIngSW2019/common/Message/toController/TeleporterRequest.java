package it.polimi.ProgettoIngSW2019.common.Message.toController;

public class TeleporterRequest extends PowerUpChoiceRequest {
    private int[] coordinates;

    public TeleporterRequest(String hostName, int idPlayer, int idPowerUp, int[] coordinates) {
        super(hostName, idPlayer, idPowerUp);
        this.coordinates = coordinates;
    }


    public int[] getCoordinates() {
        return coordinates;
    }
}
