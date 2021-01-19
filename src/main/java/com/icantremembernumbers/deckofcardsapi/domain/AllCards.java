package com.icantremembernumbers.deckofcardsapi.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AllCards {
    private static final List<Card> allCards = new ArrayList<>(52);

    static {
        final CardSuite[] suites = CardSuite.values();
        final CardValue[] values = CardValue.values();

        for (CardSuite s : suites) {
            for (CardValue v : values) {
                allCards.add(new Card(v, s));
            }
        }
    }

    public static List<Card> baseDeck() {
        return new ArrayList<>(allCards);
    }

    public static List<Card> shuffledDeck() {
        List<Card> shuffledCards = new ArrayList<>(allCards);//List.copyOf(allCards);

        Collections.shuffle(shuffledCards, new Random(System.currentTimeMillis()));
        return shuffledCards;

    }
}
