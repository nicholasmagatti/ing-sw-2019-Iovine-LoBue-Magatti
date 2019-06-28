package it.polimi.ProgettoIngSW2019.common.Message.toController;

/**
 * message from view to use Teleporter powerUp
 * @author Priscilla Lo Bue
 */
public class TeleporterRequest extends PowerUpChoiceRequest {
    private int[] coordinates;

    /**
     * Constructor
     * @param hostName          host name of the client
     * @param idPlayer          id of the player
     * @param idPowerUp         id of the powerUp to use
     * @param coordinates       coordinates to move the player
     */
    public TeleporterRequest(String hostName, int idPlayer, int idPowerUp, int[] coordinates) {
        super(hostName, idPlayer, idPowerUp);
        this.coordinates = coordinates;
    }


    /**
     * @return      coordinates to move the player
     */
    public int[] getCoordinates() {
        return coordinates;
    }
}
