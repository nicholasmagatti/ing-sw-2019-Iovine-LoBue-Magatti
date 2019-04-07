package it.polimi.ProgettoIngSW2019.model;

public class HitPoint {

    //character name
    private String nameChara;

    //number of damage tokens (1 or 2)
    private int nrOfHit;

    public HitPoint(String nameChara, int nrOfHit){

    }

    public String getNameChara(){
        return nameChara;
    }

    public int getNrOfHit(){
        return nrOfHit;
    }
}
