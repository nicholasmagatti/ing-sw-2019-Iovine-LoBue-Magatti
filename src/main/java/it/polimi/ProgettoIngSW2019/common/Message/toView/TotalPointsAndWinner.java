package it.polimi.ProgettoIngSW2019.common.Message.toView;

import java.util.List;

/**
 * Message that informs the client on the details of the winner(s) and the total points for each player.
 */
public class TotalPointsAndWinner{
    int[] totalScores;
    List<String> winners;

    /**
     * Message to communicate to the view the results of the game.
     * @param totalScores - ordered by id player
     * @param winners - name of the winners (plural in case of a tie)
     */
    public TotalPointsAndWinner(int[] totalScores, List<String> winners){
        this.totalScores = totalScores;
        this.winners = winners;
    }

    /**
     * Get the total scores ordered by id player
     * @returntotal scores ordered by id player
     */
    public int[] getTotalScores() {
        return totalScores;
    }

    /**
     * Get the list of the winners (plural in case of a tie)
     * @return list of the winners (plural in case of a tie)
     */
    public List<String> getWinners() {
        return winners;
    }
}
