package it.polimi.ProgettoIngSW2019.virtual_view;

import it.polimi.ProgettoIngSW2019.common.message.SetupMessage;

public interface ISetupVirtualView {
    void sendData(SetupMessage msg);
    void retriveData();
}
