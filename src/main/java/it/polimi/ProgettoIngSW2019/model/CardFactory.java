package it.polimi.ProgettoIngSW2019.model;

import it.polimi.ProgettoIngSW2019.model.enums.DeckType;

public class CardFactory {
    public Card getCard(int idCard, DeckType deckType){
        //TODO: temporary variable to make Sonar work (it will be deteted)
        Card temporaryVariable = new AmmoCard();
        return temporaryVariable;
    }
}
