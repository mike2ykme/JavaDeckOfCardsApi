package com.icantremembernumbers.deckofcardsapi.domain;

public enum CardValue {
    ACE("A"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"), TEN("0"), JACK("J"), QUEEN("Q"), KING("K");

    public final String value;

    private CardValue(String value) {
        this.value = value;
    }


}
