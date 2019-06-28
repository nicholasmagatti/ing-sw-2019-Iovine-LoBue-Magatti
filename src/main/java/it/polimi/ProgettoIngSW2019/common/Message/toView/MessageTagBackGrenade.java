package it.polimi.ProgettoIngSW2019.common.Message.toView;

/**
 * Message to clients when a player use the PowerUp TagBack Grenade
 */
public class MessageTagBackGrenade extends Message {
    private String nameTarget;


    /**
     * Constructor
     * @param idPlayer      id player
     * @param namePlayer    name of the player
     * @param nameTarget    name of the target player
     */
    public MessageTagBackGrenade(int idPlayer, String namePlayer, String nameTarget) {
        super(idPlayer, namePlayer);
        this.nameTarget = nameTarget;
    }


    /**
     * get the name of player target of tagBack grenade
     * @return      name of the target
     */
    public String getNameTarget() {
        return nameTarget;
    }
}
