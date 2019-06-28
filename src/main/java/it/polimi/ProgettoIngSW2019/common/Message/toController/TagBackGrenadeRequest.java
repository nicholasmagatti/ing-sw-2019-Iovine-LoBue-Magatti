package it.polimi.ProgettoIngSW2019.common.Message.toController;

/**
 * Message from view to use the tagBack Grenade powerUp
 * @author Priscilla Lo Bue
 */
public class TagBackGrenadeRequest extends PowerUpChoiceRequest {
    private int idTarget;

    /**
     * Constructor
     * @param hostName          host name of the client
     * @param idPlayer          id of the player
     * @param idPowerUp         id of the powerUp to use
     * @param idTarget          id of the target player
     */
    public TagBackGrenadeRequest(String hostName, int idPlayer, int idPowerUp, int idTarget) {
        super(hostName, idPlayer, idPowerUp);

        if(idTarget < 0)
            throw new IllegalArgumentException("idTarget player cannot be negative");

        this.idTarget = idTarget;
    }


    /**
     * @return      id of the target player
     */
    public int getIdTarget() {
        return idTarget;
    }
}
