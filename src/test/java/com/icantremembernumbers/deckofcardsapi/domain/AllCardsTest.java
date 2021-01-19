package com.icantremembernumbers.deckofcardsapi.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AllCardsTest {

    @Test
    public void testShuffled() {
        final List<Card> cards = AllCards.baseDeck();
        final List<Card> cards1 = AllCards.shuffledDeck();
        assertNotEquals(cards, cards1);
    }

}