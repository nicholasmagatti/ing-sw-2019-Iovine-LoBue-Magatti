package it.polimi.ProgettoIngSW2019.common.Message.toController;

/**
 * Message to use the newton powerUp from view
 * @author Priscilla LO Bue
 */
public class NewtonRequest extends PowerUpChoiceRequest {
    private int idTarget;
    private int[] coordinatesMovement;


    /**
     * Constructor
     * @param hostName          host name of the client
     * @param idPlayer          id of the player
     * @param idTarget          id of the target player
     * @param coordinates       coordinates to move the player target
     */
    public NewtonRequest(String hostName, int idPlayer, int idPowerUp, int idTarget, int[] coordinates) {
        super(hostName, idPlayer, idPowerUp);
        this.coordinatesMovement = coordinates;
        this.idTarget = idTarget;
    }


    /**
     * @return      id of the target player
     */
    public int getIdTarget() {
        return idTarget;
    }


    /**
     * @return      the coordinates of the target movement
     */
    public int[] getCoordinatesMovement() {
        return coordinatesMovement;
    }
}
