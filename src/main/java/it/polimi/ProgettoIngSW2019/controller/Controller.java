package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.common.Event;
import it.polimi.ProgettoIngSW2019.common.utilities.Observer;
import it.polimi.ProgettoIngSW2019.model.TurnManager;
import it.polimi.ProgettoIngSW2019.virtual_view.VirtualView;

/**
 * Controller Class
 * it has the model classes needed to the all the Controllers
 * @author Priscilla Lo Bue
 */
public abstract class Controller implements Observer<Event> {
    private IdConverter idConverter;
    private TurnManager turnManager;
    private VirtualView virtualView;


    /**
     * Constructor
     * @param turnManager   access to TurnManger model
     * @param idConverter   acess to IdConverter
     */
    public Controller(TurnManager turnManager, IdConverter idConverter, VirtualView virtualView) {
        this.turnManager = turnManager;
        this.idConverter = idConverter;
        this.virtualView = virtualView;
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
}
