package it.polimi.ProgettoIngSW2019.common.LightModel;

import it.polimi.ProgettoIngSW2019.model.KillToken;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nicholas Magatti
 */
public class KillshotTrackLM {
    private List<KillToken> track;
    private int initialNumberOfSkulls; //chosen by the first user: the length of the track before the frenzy mode

    /**
     * Constructor
     * @param killshotTrack - killshot track from the model that this method will make a copy of to have it in the view
     * @param initialNumberOfSkulls - initial number of skulls chosen by the first user
     */
    public KillshotTrackLM(List<KillToken> killshotTrack, int initialNumberOfSkulls){
        track = new ArrayList<>(killshotTrack);
        this.initialNumberOfSkulls = initialNumberOfSkulls;
    }

    public List<KillToken> getTrack() {
        return track;
    }

    public int getInitialNumberOfSkulls() {
        return initialNumberOfSkulls;
    }
}
