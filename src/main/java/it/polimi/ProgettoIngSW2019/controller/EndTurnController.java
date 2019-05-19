package it.polimi.ProgettoIngSW2019.controller;

import com.google.gson.Gson;
import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.Message.EndTurnInfo;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.enums.SquareType;
import it.polimi.ProgettoIngSW2019.custom_exception.IllegalAttributeException;
import it.polimi.ProgettoIngSW2019.model.Deck;
import it.polimi.ProgettoIngSW2019.model.Player;
import it.polimi.ProgettoIngSW2019.model.Square;
import it.polimi.ProgettoIngSW2019.model.TurnManager;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;

import java.util.List;

/**
 * EndTurnController class
 * score dead players
 * reset map
 */
public class EndTurnController extends Controller {
    private Deck weaponDeck;
    private Deck ammoDeck;
    private Player ownerPlayer;


    /**
     * Constructor
     * @param turnManager   turnManager
     * @param idConverter   idConverter
     * @param virtualView   virtualView
     */
    public EndTurnController(TurnManager turnManager, IdConverter idConverter, VirtualView virtualView) {
        super(turnManager, idConverter, virtualView);
        weaponDeck = turnManager.getGameTable().getWeaponDeck();
        ammoDeck = turnManager.getGameTable().getAmmoDeck();
    }


    /**
     * receive the message from the view
     * check type event
     * do actions based by type event
     * @param event event sent
     */
    public void update(Event event) {
        if(event.getCommand().equals(EventType.REQUEST_ENDTURN_INFO))
            endTurn(event.getMessageInJsonFormat());
    }


    /**
     * @param messageJson json message
     */
    public void endTurn(String messageJson) {
        EndTurnInfo endTurnInfo = new Gson().fromJson(messageJson, EndTurnInfo.class);

        if(endTurnInfo.getIdPlayer() != getTurnManager().getCurrentPlayer().getIdPlayer())
            throw new IllegalAttributeException("It is not Player: " + endTurnInfo.getIdPlayer() + " turn");

        ownerPlayer = getIdConverter().getPlayerById(endTurnInfo.getIdPlayer());
        scoreDeadPlayers();

        //TODO: comunicare risultati

        resetMap();
    }


    //TODO: da sistemare
    public void scoreDeadPlayers() {
        //creo lista di players morti
        List<Player> deadPlayers = getTurnManager().checkDeadPlayers();

        for (Player player: deadPlayers) {
            //aggiungo i player nel killToken
            getTurnManager().getGameTable().addTokenOnKillshotTrack(player, ownerPlayer);
            //aggiungo un teschio(skull) ad ogni player morto
            player.increaseNumberOfSkulls();
            //TODO: passo ogni player in spawnState
        }
    }


    /**
     * reset map
     * add cards missed in the map
     */
    public void resetMap() {
        for(Square[] squareLine: getTurnManager().getGameTable().getMap()) {
            for (Square square : squareLine) {
                if (square != null) {
                    if (square.getSquareType() == SquareType.SPAWNING_POINT)
                        square.reset(weaponDeck);
                    if (square.getSquareType() == SquareType.AMMO_POINT)
                        square.reset(ammoDeck);
                }
            }
        }
    }
}
