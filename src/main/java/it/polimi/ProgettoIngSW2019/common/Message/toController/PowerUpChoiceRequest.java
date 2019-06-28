package it.polimi.ProgettoIngSW2019.common.Message.toController;


/**
 * Message common from view to use a powerUp
 * @author Priscilla Lo bue
 */
public class PowerUpChoiceRequest extends PlayerChoiceRequest {
    private int idPowerUp;


    /**
     * Constructor
     * @param hostName          host name of the client
     * @param idPlayer          id of the player
     * @param idPowerUp         id of the powerUp to use
     */
    public PowerUpChoiceRequest(String hostName, int idPlayer, int idPowerUp) {
        super(hostName, idPlayer);

        this.idPowerUp = idPowerUp;
    }


    /**
     * @return      id of the powerUp to use
     */
    public int getIdPowerUp() {
        return idPowerUp;
    }
}
