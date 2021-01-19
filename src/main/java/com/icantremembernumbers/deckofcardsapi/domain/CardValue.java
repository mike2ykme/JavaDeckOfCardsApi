package com.icantremembernumbers.deckofcardsapi.domain;

import java.util.HashMap;
import java.util.Map;

public enum CardValue {
    ACE("A"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"), TEN("0"), JACK("J"), QUEEN("Q"), KING("K");

    public final String value;
    public static final Map<String, CardValue> lookup = new HashMap<>();

    private CardValue(String value) {
        this.value = value;
    }

    static {
        for (CardValue c : CardValue.values()) {
            lookup.put(c.value, c);
        }
    }

    public static CardValue get(String str) {
        return lookup.get(str);
    }
}
