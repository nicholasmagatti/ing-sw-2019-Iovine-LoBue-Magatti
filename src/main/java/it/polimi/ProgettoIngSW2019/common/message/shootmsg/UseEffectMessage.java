package it.polimi.ProgettoIngSW2019.common.message.shootmsg;

import java.util.List;

public class UseEffectMessage extends ShootMessage {
    private List<Integer> idTargetPlayer;
    private int idUserPlayer;
    private List<Integer[]> targetMoveCoordinates;
    private int[] userMoveCoordinates;
    private int idWeaponUsed;

    //TODO: costruttore con l'assegnamento dei parametri

    public int getIdUserPlayer() {
        return idUserPlayer;
    }

    public int getIdWeaponUsed() {
        return idWeaponUsed;
    }

    public int[] getUserMoveCoordinates() {
        return userMoveCoordinates;
    }

    public List<Integer> getIdTargetPlayer() {
        return idTargetPlayer;
    }

    public List<Integer[]> getTargetMoveCoordinates() {
        return targetMoveCoordinates;
    }
}
