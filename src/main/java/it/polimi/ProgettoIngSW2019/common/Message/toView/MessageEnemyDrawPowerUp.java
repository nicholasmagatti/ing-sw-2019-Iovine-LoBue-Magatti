package it.polimi.ProgettoIngSW2019.common.Message.toView;

/**
 * message to enemy from server with a player draw a card
 * @author Priscilla Lo Bue
 */
public class MessageEnemyDrawPowerUp extends Message {
    private int nCards;


    /**
     * Constructor
     * @param idPlayer          idPlayer draw card
     * @param namePlayer        name player draw card
     * @param nCards            number of cards the player drawn
     */
    public MessageEnemyDrawPowerUp(int idPlayer, String namePlayer, int nCards) {
        super(idPlayer, namePlayer);
        this.nCards = nCards;
    }


    /**
     * get the number of cards the player drawn
     * @return      number of cards drawn
     */
    public int getnCards() {
        return nCards;
    }
}
