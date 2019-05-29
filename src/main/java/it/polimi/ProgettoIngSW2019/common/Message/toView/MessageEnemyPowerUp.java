package it.polimi.ProgettoIngSW2019.common.Message.toView;

/**
 * @author Priscilla Lo Bue
 */
public class MessageEnemyPowerUp extends Message {
    private int nCards;


    public MessageEnemyPowerUp(int idPlayer, String namePlayer, int nCards) {
        super(idPlayer, namePlayer);

        if(nCards <= 0)
            throw new IllegalArgumentException("nCards must be positive");

        this.nCards = nCards;
    }


    public int getnCards() {
        return nCards;
    }
}
