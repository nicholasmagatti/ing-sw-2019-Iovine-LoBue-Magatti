package it.polimi.ProgettoIngSW2019.common.Message.toView;

/**
 * message with the number of actions of the Player to do
 * @author Priscilla Lo Bue
 */
public class MessageActionLeft extends Message {
    private int nActionsLeft;


    /**
     * Constructor
     * @param idPlayer          id player
     * @param namePlayer        name player
     * @param nActionsLeft      number actions of the player
     */
    public MessageActionLeft(int idPlayer, String namePlayer, int nActionsLeft) {
        super(idPlayer, namePlayer);

        if(nActionsLeft < 0)
            throw new IllegalArgumentException("nActionsLeft cannot be negative");

        this.nActionsLeft = nActionsLeft;
    }


    /**
     * @return number actions of the player
     */
    public int getnActionsLeft() {
        return nActionsLeft;
    }
}
