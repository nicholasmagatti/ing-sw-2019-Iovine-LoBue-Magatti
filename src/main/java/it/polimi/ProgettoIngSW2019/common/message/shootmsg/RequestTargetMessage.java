package it.polimi.ProgettoIngSW2019.common.message.shootmsg;

import java.util.List;

public class RequestTargetMessage extends ShootMessage {
    List<List<Integer>> targetIdList;
    String targetPerSquare;
    int nrOfTarget;

    public RequestTargetMessage(){}

    public RequestTargetMessage(List<List<Integer>> targetIdList, String targetPerSquare, int nrOfTarget){
        this.targetIdList = targetIdList;
        this.targetPerSquare = targetPerSquare;
        this.nrOfTarget = nrOfTarget;
    }

    public int getNrOfTarget() {
        return nrOfTarget;
    }

    public String getTargetPerSquare() {
        return targetPerSquare;
    }

    public List<List<Integer>> getTargetIdList() {
        return targetIdList;
    }
}
