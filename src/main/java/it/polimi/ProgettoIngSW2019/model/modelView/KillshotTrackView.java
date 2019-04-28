package it.polimi.ProgettoIngSW2019.model.modelView;

import it.polimi.ProgettoIngSW2019.model.KillToken;

import java.util.ArrayList;
import java.util.List;

public class KillshotTrackView {
    private List<KillToken> killshotTrack;
    private List<KillToken> frenzyKillshotTrack;

    public List<KillToken> getKillshotTrack() {
        return killshotTrack;
    }

    public List<KillToken> getFrenzyKillshotTrack() {
        return frenzyKillshotTrack;
    }

    //TODO: override of toString

}