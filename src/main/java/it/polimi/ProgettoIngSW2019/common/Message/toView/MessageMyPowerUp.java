package it.polimi.ProgettoIngSW2019.common.Message.toView;

import java.util.List;

public class MessageMyPowerUp extends Message {
    private List<String> namePowerUps;


    public MessageMyPowerUp(int idPlayer, String namePlayer, List<String> namePowerUps) {
        super(idPlayer, namePlayer);

        if(namePowerUps == null)
            throw new NullPointerException("The name PowerUps list cannot be null");

        this.namePowerUps = namePowerUps;
    }


    public List<String> getNamePowerUps() {
        return namePowerUps;
    }
}
