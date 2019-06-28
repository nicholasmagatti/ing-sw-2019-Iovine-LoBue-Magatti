package it.polimi.ProgettoIngSW2019.common.Message.toController;

public class NewtonRequest extends PowerUpChoiceRequest {
    private int idTarget;
    private int[] coordinatesMovement;


    public NewtonRequest(String hostName, int idPlayer, int idTarget, int[] coordinates) {
        super(hostName, idPlayer, idTarget);
        this.coordinatesMovement = coordinates;
        this.idTarget = idTarget;
    }


    public int getIdTarget() {
        return idTarget;
    }


    public int[] getCoordinatesMovement() {
        return coordinatesMovement;
    }
}
