package it.polimi.ProgettoIngSW2019.model;

import it.polimi.ProgettoIngSW2019.model.enums.DeckType;

import java.util.Collections;
import java.util.List;


/**
 * Class Deck: the single deck
 * @author Priscilla Lo Bue
 */
public class Deck {
    private DeckType deckType;
    private List <Card> cards;


    /**
     * method that creates the deck
     * @param deckType  type of the cards
     */
    public Deck (DeckType deckType){
        DeckFactory deckFactory = new DeckFactory();
        this.cards = deckFactory.setDeck(deckType);
        this.deckType = deckType;
    }


    /**
     * get the cards of the deck
     * @return cards, a list of cards
     */
    public List<Card> getCards(){
        return this.cards;
    }


    /**
     * get the deck type
     * @return  deckType
     */
    public DeckType getDecktType(){
        return this.deckType;
    }


    /**
     * shuffle the deck randomly
     */
    public void shuffle(){
        Collections.shuffle(cards);
    }


    /**
     * draw a card from the top of the deck and remove the card from it
     * @return  the drawned card
     */
    public Card drawCard () {
        Card card;

        card = cards.get(0);
        cards.remove(card);

        return card;
    }

}
