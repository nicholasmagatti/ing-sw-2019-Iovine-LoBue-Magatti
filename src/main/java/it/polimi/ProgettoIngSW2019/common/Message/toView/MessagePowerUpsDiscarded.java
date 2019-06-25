package it.polimi.ProgettoIngSW2019.common.Message.toView;

import it.polimi.ProgettoIngSW2019.common.LightModel.PowerUpLM;

import java.util.List;

public class MessagePowerUpsDiscarded extends Message {
    private List<PowerUpLM> powerUpsToDiscard;


    public MessagePowerUpsDiscarded(int idPlayer, String namePlayer, List<PowerUpLM> powerUpsToDiscard) {
        super(idPlayer, namePlayer);
        this.powerUpsToDiscard = powerUpsToDiscard;
    }


    public List<PowerUpLM> getPowerUpsToDiscard() {
        return powerUpsToDiscard;
    }
}
