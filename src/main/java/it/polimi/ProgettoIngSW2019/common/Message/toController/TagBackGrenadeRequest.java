package it.polimi.ProgettoIngSW2019.common.Message.toController;

public class TagBackGrenadeRequest extends PowerUpChoiceRequest {
    private int idTarget;


    public TagBackGrenadeRequest(String hostName, int idPlayer, int idPowerUp, int idTarget) {
        super(hostName, idPlayer, idPowerUp);

        if(idTarget < 0)
            throw new IllegalArgumentException("idTarget player cannot be negative");

        this.idTarget = idTarget;
    }


    public int getIdTarget() {
        return idTarget;
    }
}
