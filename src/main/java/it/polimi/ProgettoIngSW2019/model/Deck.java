package it.polimi.ProgettoIngSW2019.model;

import it.polimi.ProgettoIngSW2019.model.enums.DeckType;

import java.util.ArrayList;

public class Deck {
    private DeckType deckType;
    private ArrayList <Card> cards;
    private CardFactory cardFactory;

    public ArrayList<Card> getCards(){
        //TODO: temporary variable to make Sonar work (it will be deteted)
        ArrayList<Card> temporaryVariable = new ArrayList<Card>();
        return temporaryVariable;
    }


    public DeckType getDecktType(){
        //TODO: temporary variable to make Sonar work (it will be deteted)
        DeckType temporaryVariable = DeckType.WEAPON_CARD;
        return temporaryVariable;
    }


    public void shuffle(){

    }


    public void initialize(DeckType deckType){

    }


    public void drawCard(DeckType deckType){

    }
}
