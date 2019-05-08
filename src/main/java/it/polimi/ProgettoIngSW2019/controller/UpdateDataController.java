package it.polimi.ProgettoIngSW2019.controller;

import it.polimi.ProgettoIngSW2019.model.GameTable;
import it.polimi.ProgettoIngSW2019.model.LightModel;
import it.polimi.ProgettoIngSW2019.utilities.Observer;

public class UpdateDataController implements Observer<String>{
    private GameTable gameTable;
    private LightModel lightModel;


    public void update(String msg) {

    }
}
