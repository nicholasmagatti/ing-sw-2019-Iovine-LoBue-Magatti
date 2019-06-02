package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.enums.EventType;
import it.polimi.ProgettoIngSW2019.common.utilities.Observer;
import it.polimi.ProgettoIngSW2019.model.TurnManager;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;

import java.util.List;

/**
 * Controller Class
 * it has the model classes needed to the all the Controllers
 * @author Priscilla Lo Bue
 */
public abstract class Controller implements Observer<Event> {
    private IdConverter idConverter;
    private TurnManager turnManager;
    private VirtualView virtualView;
    private CreateJson createJson;
    private IdPlayersCreateList idPlayersCreateList;


    /**
     * Constructor
     * @param turnManager   access to TurnManger model
     * @param idConverter   acess to IdConverter
     */
    public Controller(TurnManager turnManager, IdConverter idConverter, VirtualView virtualView, CreateJson createJson, IdPlayersCreateList idPlayersCreateList) {
        this.turnManager = turnManager;
        this.idConverter = idConverter;
        this.virtualView = virtualView;
        this.createJson = createJson;
        this.idPlayersCreateList = idPlayersCreateList;
    }


    /**
     * access to IdConverter
     * @return  idConverter
     */
    public IdConverter getIdConverter() {
        return idConverter;
    }


    /**
     * access to TurnManager
     * @return  turnManager
     */
    public TurnManager getTurnManager() {
        return turnManager;
    }


    /**
     * access to VirtualView
     * @return  virtualView
     */
    public VirtualView getVirtualView() {
        return virtualView;
    }


    /**
     * get createJson
     * @return  createJson
     */
    public CreateJson getCreateJson() {
        return createJson;
    }


    /**
     * get IdPlayersCreateList
     * @return  idPlayersCreateList
     */
    public IdPlayersCreateList getIdPlayersCreateList() {
        return idPlayersCreateList;
    }


    /**
     * send info/event by virtual view
     * @param eventType     type of the event
     * @param msgJson       message in json format
     * @param idPlayers     list of player who will receive the message
     */
    public void sendInfo(EventType eventType, String msgJson, List<Integer> idPlayers) {
        Event event = new Event(eventType, msgJson);
        virtualView.sendMessage(event, idPlayers);
    }
}
