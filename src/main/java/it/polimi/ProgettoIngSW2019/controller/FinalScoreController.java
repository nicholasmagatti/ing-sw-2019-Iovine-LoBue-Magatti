package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.Message.toView.MessageScorePlayer;
import it.polimi.ProgettoIngSW2019.common.Message.toView.ScorePlayerWhoHit;
import it.polimi.ProgettoIngSW2019.common.Message.toView.TotalPointsAndWinner;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.TurnManager;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;

import java.util.ArrayList;
import java.util.List;

/**
 * Class created and used by the EndTurnController when the game has to come to its end.
 * @author Nicholas Magatti
 */
public class FinalScoreController extends Controller {

    /**
     * Constructor
     * @param turnManager               TurnManager
     * @param virtualView               VirtualView
     * @param idConverter               IdConverter
     * @param createJson                CreateJson
     * @param hostNameCreateList        HostNameCreateList
     */
    FinalScoreController(TurnManager turnManager, VirtualView virtualView, IdConverter idConverter, CreateJson createJson, HostNameCreateList hostNameCreateList) {
        super(turnManager, virtualView, idConverter, createJson, hostNameCreateList);
    }

    /**
     * Method that brings the game to its end: calculate the points and send all the information
     * about the total points and the winner to the players.
     */
    void endGame(){
        notifyTheGameIsEnding();
        scoreNonDeadPlayersWithDamage();
        scoreKillshotTrack();
        finalResults();
    }

    /**
     * This method does nothing because this class was meant to be an observer but also needed
     * to extend the class Controller to use the method sendInfo.
     * @param message
     */
    @Override
    public void update(Event message) {

    }

    /**
     * Send to the clients a message that inform that the game is ending.
     */
    private void notifyTheGameIsEnding(){
        sendInfo(EventType.MSG_CONCLUSION, "", getHostNameCreateList().addAllHostName());
    }

    /**
     * At the end of the game, score the damage line of the players who have not died in this
     * turn but who have damage on their damage line.
     */
    private void scoreNonDeadPlayersWithDamage(){
        List<Player> playersToScore = new ArrayList<>();
        for(Player p : getTurnManager().getGameTable().getPlayers()){
            if(!p.isPlayerDown() && !p.getDamageLine().isEmpty()){
                playersToScore.add(p);
            }
        }
        for(Player player : playersToScore){
            int[] pointsAssigned = getTurnManager().scoreDamageLineOf(player);
            Player whoGotFirstBlood = getTurnManager().assignFirstBlood(player);
            List<ScorePlayerWhoHit> scorePlayerWhoHitList = new ArrayList<>();
            for(int id=0; id < pointsAssigned.length; id++){
                ScorePlayerWhoHit scorePlayerWhoHit =
                        new ScorePlayerWhoHit(convertPlayerWithId(id).getCharaName(), pointsAssigned[id]);
                scorePlayerWhoHitList.add(scorePlayerWhoHit);
            }
            MessageScorePlayer messageScorePlayer =
                    new MessageScorePlayer(player.getIdPlayer(), player.getCharaName(),
                            "", whoGotFirstBlood.getCharaName(), scorePlayerWhoHitList, 0);
            String jsonMsg = new Gson().toJson(messageScorePlayer);
            sendInfo(EventType.MSG_SCORE_ALIVE_PLAYER, jsonMsg, getHostNameCreateList().addAllHostName());
        }

    }

    /**
     * Score the killshot track and send to the clients the points they got from that.
     */
    private void scoreKillshotTrack(){
        List<ScorePlayerWhoHit> listPlayerWhoHit = new ArrayList<>();
        int[] scoresById = getTurnManager().scoreKillshotTrack();
        for(int id=0; id < scoresById.length; id++){
            listPlayerWhoHit.add(new ScorePlayerWhoHit(convertPlayerWithId(id).getCharaName(), scoresById[id]));
        }
        String jsonMsg = new Gson().toJson(listPlayerWhoHit);
        sendInfo(EventType.MSG_SCORE_KILLSHOT_TRACK, jsonMsg, getHostNameCreateList().addAllHostName());
    }

    /**
     * Send to the players the information about the total points of every player and the winner.
     */
    private void finalResults(){
        int [] totalPoints = totalPoints();
        List<Player> winners = getWinner(totalPoints);
        List<String> nameWinners = new ArrayList<>();
        for(Player p : winners){
            nameWinners.add(p.getCharaName());
        }
        TotalPointsAndWinner totalPointsAndWinner = new TotalPointsAndWinner(totalPoints, nameWinners);
        String jsonMsg = new Gson().toJson(totalPointsAndWinner);
        sendInfo(EventType.MSG_FINAL_RESULTS, jsonMsg, getHostNameCreateList().addAllHostName());
    }

    /**
     * Return the total points of each player at the end of the game.
     * @return the total points of each player.
     */
    private int[] totalPoints(){
        int[] totPoints = new int[getTurnManager().getGameTable().getPlayers().length];
        for(int i=0; i < totPoints.length; i++){
            totPoints[i] = getTurnManager().getGameTable().getPlayers()[i].getScore();
        }
        return totPoints;
    }

    /**
     * Return the winners of the game, according to the rules (plural in case of a tie).
     * @param pointsById - total points of every player ordered by id
     * @return the winners of the game.
     */
    private List<Player> getWinner(int[] pointsById){
        List<Player> playersWithHigherScore = new ArrayList<>();
        List<Integer> idsPlayersWithHighScores = new ArrayList<>();
        int max = 0;
        for(int pointsPlayer : pointsById){
            if(max < pointsPlayer){
                max = pointsPlayer;
            }
        }
        for(int id=0; id < pointsById.length; id++){
            if(pointsById[id] == max){
                idsPlayersWithHighScores.add(id);
            }
        }

        for(int id : idsPlayersWithHighScores){
            playersWithHigherScore.add(convertPlayerWithId(id));
        }

        if(playersWithHigherScore.size() == 1){
            return playersWithHigherScore;
        }
        else{
            return playersWithHigherScoreOnKillshotTracksBetweenThese(playersWithHigherScore);
        }
    }

    /**
     * Return the players who got the higher score on the killshot track between the ones given.
     * @param players
     * @return the players who got the higher score on the killshot track between the ones given.
     */
    private List<Player> playersWithHigherScoreOnKillshotTracksBetweenThese(List<Player> players){
        List<Player> listToReturn = new ArrayList<>();
        int max = 0;
        for(Player p : players){
            int pointsOnKT = getTurnManager().getGameTable().tokensOnKillshotTrackBelongingTo(p);
            if(max < pointsOnKT){
                max = pointsOnKT;
            }
        }
        for(Player p : players){
            if(getTurnManager().getGameTable().tokensOnKillshotTrackBelongingTo(p) == max){
                listToReturn.add(p);
            }
        }
        return listToReturn;
    }


}
