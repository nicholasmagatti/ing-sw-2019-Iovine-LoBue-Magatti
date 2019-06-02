package it.polimi.ProgettoIngSW2019.common.Message.toView;

/**
 * @author Priscilla Lo Bue
 */
public class MessageEnemyDrawPowerUp extends Message {
    private int nCards;


    public MessageEnemyDrawPowerUp(int idPlayer, String namePlayer, int nCards) {
        super(idPlayer, namePlayer);
        this.nCards = nCards;
    }


    public int getnCards() {
        return nCards;
    }
}
