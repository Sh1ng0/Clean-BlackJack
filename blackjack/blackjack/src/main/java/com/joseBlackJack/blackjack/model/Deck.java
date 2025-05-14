package com.joseBlackJack.blackjack.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

public class Deck {

    private List<Card> deckList;

    public Deck() {
        deckList = new ArrayList<>();
    }



    public void initializeDeck() {
        deckList.clear();
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                deckList.add(new Card(suit, rank));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(deckList);
    }

    public Card dealCard() {
        if(deckList.isEmpty()) throw new NoSuchElementException("No cards left in the deck");
        return deckList.remove(0);
    }


    public void dealCards(AbstractPlayer player, int numberOfCards) {
        for (int i = 0; i < numberOfCards; i++) {
            player.receiveCard(this.dealCard());
        }
    }

    public int cardsRemaining() {
        return deckList.size();
    }
}