package com.icantremembernumbers.deckofcardsapi.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Card {
    private final CardValue value;
    private final CardSuite suite;
    private final String shortCode;

    public Card(CardValue value, CardSuite suite) {
        this.value = value;
        this.suite = suite;
        this.shortCode = imageCode();
    }

    public Card(String value, String suite) {
        this.value = CardValue.get(value);
        this.suite = CardSuite.get(suite);
        this.shortCode = imageCode();
    }

    public String imageCode() {
        return this.value.value + this.suite.suite;
    }

}
