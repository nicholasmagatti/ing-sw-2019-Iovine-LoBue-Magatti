package it.polimi.ProgettoIngSW2019.common.Message.toView;

import it.polimi.ProgettoIngSW2019.common.LightModel.PowerUpLM;

import java.util.ArrayList;
import java.util.List;

/**
 * Message to all players when a player discard a powerUp to pay as ammo or after using it
 * @author Priscilla Lo Bue
 */
public class MessagePowerUpsDiscarded extends Message {
    private List<PowerUpLM> powerUpsToDiscard;


    /**
     * Constructor
     * @param idPlayer              idPlayer
     * @param namePlayer            name of the player
     * @param powerUpsToDiscard     powerUps LM list to discard
     */
    public MessagePowerUpsDiscarded(int idPlayer, String namePlayer, List<PowerUpLM> powerUpsToDiscard) {
        super(idPlayer, namePlayer);
        this.powerUpsToDiscard = new ArrayList<>(powerUpsToDiscard);
    }


    /**
     * get the powerUps list LM to discard
     * @return      powerUps LM list
     */
    public List<PowerUpLM> getPowerUpsToDiscard() {
        return powerUpsToDiscard;
    }
}
