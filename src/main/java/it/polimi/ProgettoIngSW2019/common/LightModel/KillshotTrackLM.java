package it.polimi.ProgettoIngSW2019.common.LightModel;

import it.polimi.ProgettoIngSW2019.model.KillToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Killshot track for view
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

    /**
     * Get the (copy of) the killshot track
     * @return copy of the killshot track
     */
    public List<KillToken> getTrack() {
        return track;
    }

    /**
     * Get the number of skulls set by the first user for the game
     * @return the number of skulls for the game
     */
    public int getInitialNumberOfSkulls() {
        return initialNumberOfSkulls;
    }
}
