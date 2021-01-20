package com.icantremembernumbers.deckofcardsapi.domain;

public class ApiCard extends Card {
    public final String imageUrl;

    public ApiCard(CardValue value, CardSuite suite, String imageUrl) {
        super(value, suite);
        this.imageUrl = imageUrl + getShortCode() + ".svg";
    }

    public ApiCard(String value, String suite, String imageUrl) {
        super(value, suite);
        this.imageUrl = imageUrl + getShortCode() + ".svg";
    }

    public ApiCard(Card card, String imageUrl) {
        super(card.getValue(), card.getSuite());
        this.imageUrl = imageUrl + getShortCode() + ".svg";
    }

}
