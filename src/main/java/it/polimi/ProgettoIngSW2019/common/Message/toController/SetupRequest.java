package it.polimi.ProgettoIngSW2019.common.Message.toController;

public class SetupRequest {
    private String hostname;
    private int mapChosen;
    private int nrOfSkullChosen;

    public SetupRequest(String hostname, int mapChosen, int nrOfSkullChosen){
        this.hostname = hostname;
        this.mapChosen = mapChosen;
        this.nrOfSkullChosen = nrOfSkullChosen;
    }

    public String getHostname() {
        return hostname;
    }

    public int getMapChosen() {
        return mapChosen;
    }

    public int getNrOfSkullChosen() {
        return nrOfSkullChosen;
    }
}
