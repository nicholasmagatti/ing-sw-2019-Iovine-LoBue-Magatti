package it.polimi.ProgettoIngSW2019.common.Message.toView;

import it.polimi.ProgettoIngSW2019.common.LightModel.PowerUpLM;

import java.util.List;

/**
 * message with the number of actions of the Player to do
 * @author Priscilla Lo Bue
 */
public class MessageActionLeft extends Message {
    private int nActionsLeft;
    private List<PowerUpLM> powerUpsCanUse;


    /**
     * Constructor
     * @param idPlayer          id player
     * @param namePlayer        name player
     * @param nActionsLeft      number actions of the player
     * @param powerUpsCanUse    list of powerUps the player can use
     */
    public MessageActionLeft(int idPlayer, String namePlayer, int nActionsLeft, List<PowerUpLM> powerUpsCanUse) {
        super(idPlayer, namePlayer);

        if(nActionsLeft < 0)
            throw new IllegalArgumentException("nActionsLeft cannot be negative");

        if(powerUpsCanUse == null)
            throw new IllegalArgumentException("powerUpsInHand cannot be nulls");

        this.nActionsLeft = nActionsLeft;
        this.powerUpsCanUse = powerUpsCanUse;
    }


    /**
     * @return number actions of the player
     */
    public int getnActionsLeft() {
        return nActionsLeft;
    }


    /**
     * @return  list of powerUps the player can use
     */
    public List<PowerUpLM> getPowerUpsCanUse() {
        return powerUpsCanUse;
    }
}
