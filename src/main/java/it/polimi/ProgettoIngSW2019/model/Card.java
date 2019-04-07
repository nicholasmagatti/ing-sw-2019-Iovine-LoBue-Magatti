package it.polimi.ProgettoIngSW2019.model;

import it.polimi.ProgettoIngSW2019.model.enums.DeckType;
import it.polimi.ProgettoIngSW2019.utilities.Observable;


public abstract class Card extends Observable <String>{
    private int idCard;
    private DeckType cardType;
}