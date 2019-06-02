package it.polimi.ProgettoIngSW2019.common.Message.toView;

import java.util.ArrayList;
import java.util.List;

/**
 * Message with the powerUp draw info
 * @author Priscilla Lo Bue
 */
public class MessageDrawMyPowerUp extends Message {
    private int[] idPowerUps;
    private List<String> namePowerUps;


    /**
     * Constructor
     * @param idPlayer      id player
     * @param namePlayer    name player
     * @param idPowerUps     id powerUp draw
     * @param namePowerUps  name powerUp draw
     */
    public MessageDrawMyPowerUp(int idPlayer, String namePlayer, int[] idPowerUps, List<String> namePowerUps) {
        super(idPlayer, namePlayer);

        for(int id: idPowerUps) {
            if (id < 0)
                throw new IllegalArgumentException("Id card cannot be negative");
        }

        this.idPowerUps = idPowerUps;

        if(namePowerUps == null)
            throw new NullPointerException("The name PowerUps list cannot be null");

        this.namePowerUps = new ArrayList<>(namePowerUps);
    }


    /**
     * get name of the powerUps draw
     * @return  name powerUps
     */
    public List<String> getNamePowerUps() {
        return namePowerUps;
    }


    /**
     * get Id PowerUp draw
     * @return  id powerUp
     */
    public int[] getIdPowerUp() {
        return idPowerUps;
    }
}
