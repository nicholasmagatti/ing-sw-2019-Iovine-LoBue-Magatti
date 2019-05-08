package it.polimi.ProgettoIngSW2019.network_handler;

import it.polimi.ProgettoIngSW2019.utilities.Observer;
import it.polimi.ProgettoIngSW2019.virtual_view.IUpdateVirtualView;

public class UpdateDataForwarder implements Observer {
    IUpdateVirtualView updateVirtualView;

    public UpdateDataForwarder(IUpdateVirtualView updateVirtualView){
        this.updateVirtualView = updateVirtualView;
    }

    @Override
    public void update(Object message) {

    }
}
