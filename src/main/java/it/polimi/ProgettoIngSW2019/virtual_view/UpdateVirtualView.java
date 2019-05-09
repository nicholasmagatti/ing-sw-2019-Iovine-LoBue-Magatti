package it.polimi.ProgettoIngSW2019.virtual_view;

import it.polimi.ProgettoIngSW2019.utilities.Observable;

public class UpdateVirtualView extends Observable implements IUpdateVirtualView {
    @Override
    public void updateData() {
        notify();
    }
}
