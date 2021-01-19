package com.icantremembernumbers.deckofcardsapi.domain;

import org.springframework.lang.Nullable;

import java.util.*;

public class Deck {
    private final String id;
    private final List<Card> remainingCards;// = new ArrayList<>(52);
    private final List<Card> discardedCards = new ArrayList<>(52);
    private final HashMap<String, List<Card>> allPiles = new HashMap<>();

    private Deck(List<Card> remainingCards, String id) {
        this.id = id;
        this.remainingCards = remainingCards;
    }

    public static Deck newUnshuffledDeck(String id) {
        var deck = new Deck(AllCards.baseDeck(), id);
        return deck;
    }

    public static Deck newShuffledDeck(String id) {
        var deck = new Deck(AllCards.shuffledDeck(), id);
        return deck;
    }


    synchronized public int cardsRemaining() {
        return this.remainingCards.size();

    }

    synchronized @Nullable
    public Card getCard() {
        if (cardsRemaining() > 0) {
            final Card removed = remainingCards.remove(0);
            this.discardedCards.add(removed);
            return removed;
        }

        return null;
    }

    synchronized @Nullable
    public Card peekCard() {
        if (cardsRemaining() > 0)
            return this.remainingCards.get(0);

        return null;
    }

    synchronized @Nullable
    public List<Card> peekCard(int i) {
        if (cardsRemaining() >= i) {
            List<Card> cards = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                cards.add(this.remainingCards.get(i));
            }
            return cards;
        }
        return null;
    }

    synchronized @Nullable
    public Card getLastDiscard() {
        if (this.discardedCards.size() > 0)
            return this.discardedCards.get(discardedCards.size() - 1);
        return null;
    }

    synchronized public void shuffle() {
        Collections.shuffle(remainingCards, new Random(System.currentTimeMillis()));
    }

    public String getId() {
        return this.id;
    }

    synchronized public @Nullable
    List<Card> drawCards(int i) {
        if (cardsRemaining() >= i) {
            ArrayList<Card> cards = new ArrayList<>(i);
            for (int j = 0; j < i; j++) {
                cards.add(getCard());
            }
            return cards;
        }
        return null;
    }
}
