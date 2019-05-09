package it.polimi.ProgettoIngSW2019.network_handler;

import it.polimi.ProgettoIngSW2019.common.message.SetupMessage;
import it.polimi.ProgettoIngSW2019.utilities.Observer;
import it.polimi.ProgettoIngSW2019.virtual_view.ISetupVirtualView;

public class SetupMessageForwarder implements Observer<SetupMessage> {
    private ISetupVirtualView setupVirtualView;

    public SetupMessageForwarder(ISetupVirtualView setupVirtualView){
        this.setupVirtualView = setupVirtualView;
    }

    @Override
    public void update(SetupMessage message) {
        setupVirtualView.sendData(message);
    }
}
