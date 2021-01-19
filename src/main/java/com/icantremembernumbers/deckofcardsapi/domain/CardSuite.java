package com.icantremembernumbers.deckofcardsapi.domain;

public enum CardSuite {
    CLUBS("C"), DIAMONDS("D"), HEARTS("H"), SPADES("S");

    public final String suite;

    private CardSuite(String suite) {
        this.suite = suite;
    }
}
