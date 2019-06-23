package it.polimi.ProgettoIngSW2019.common.Message.toController;

public class SetupRequest {
    private String hostname;
    private int mapChosen;
    private int nrOfSkullChosen;

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
