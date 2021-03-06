package it.polimi.ProgettoIngSW2019.common.Message.toController;

/**
 * SpawnChoiceRequest class
 * message from view to controller with the powerUp to discard for spawn
 * @author Priscilla Lo Bue
 */
public class SpawnChoiceRequest extends PlayerChoiceRequest {
    private int idPowerUpToDiscard;


    /**
     * Constructor
     * @param hostNamePlayer        host name client
     * @param idPlayer              id player
     * @param idPowerUpToDiscard    id powerUp to discard
     */
    public SpawnChoiceRequest(String hostNamePlayer, int idPlayer, int idPowerUpToDiscard) {
        super(hostNamePlayer, idPlayer);
        if(idPowerUpToDiscard < 0)
            throw new IllegalArgumentException("The id of the powerUp cannot be negative");
        this.idPowerUpToDiscard = idPowerUpToDiscard;
    }


    /**
     * get the id of the powerUp to discard
     * @return  id powerUp
     */
    public int getIdPowerUpToDiscard() {
        return idPowerUpToDiscard;
    }
}
