package com.icantremembernumbers.deckofcardsapi.domain;

import java.util.HashMap;
import java.util.Map;

public enum CardSuite {
    CLUBS("C"), DIAMONDS("D"), HEARTS("H"), SPADES("S");

    public final String suite;
    public static final Map<String, CardSuite> lookup = new HashMap<>();

    private CardSuite(String suite) {
        this.suite = suite;
    }


    static {
        for (CardSuite c : CardSuite.values()) {
            lookup.put(c.suite, c);
        }
    }

    public static CardSuite get(String str) {
        return lookup.get(str);
    }
}
