package it.polimi.ProgettoIngSW2019.common.Message.toView;

public class MessageTagBackGrenade extends Message {
    private String nameTarget;


    public MessageTagBackGrenade(int idPlayer, String namePlayer, String nameTarget) {
        super(idPlayer, namePlayer);
        this.nameTarget = nameTarget;
    }


    public String getNameTarget() {
        return nameTarget;
    }
}
