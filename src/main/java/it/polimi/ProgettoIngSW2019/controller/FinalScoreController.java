package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.model.GameTable;
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
    public FinalScoreController(TurnManager turnManager, VirtualView virtualView, IdConverter idConverter, CreateJson createJson, HostNameCreateList hostNameCreateList) {
        super(turnManager, virtualView, idConverter, createJson, hostNameCreateList);
    }

    void endGame(){
        scoreNonDeadPlayersWithDamage();
    }

    @Override
    public void update(Event message) {

    }

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

            //crete message
            //sendInfo(...)
        }

    }
}
