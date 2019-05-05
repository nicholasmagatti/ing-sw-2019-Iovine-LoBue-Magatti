package it.polimi.ProgettoIngSW2019.model;

import it.polimi.ProgettoIngSW2019.model.enums.DeckType;

/**
 * Class abstract Card
 * @author Priscilla Lo Bue
 */

public abstract class Card {
    private int idCard;
    private DeckType cardType;



    /**
     * This is the constructor
     * @param idCard    the id card (identify the card)
     * @param cardType  type of the card: ammo, powerUp, weapon
     */
    public Card(int idCard, DeckType cardType) {
        if(idCard < 0)
            throw new IllegalArgumentException("the idCard cannot be negative");
        this.idCard = idCard;
        this.cardType = cardType;
    }


    /**
     * get id Card
     * @return      int the idCard
     */
    public int getIdCard() {
        return idCard;
    }


    /**
     * get the type of the card
     * @return      a deckType
     */
    public DeckType getCardType() {
        return cardType;
    }
}