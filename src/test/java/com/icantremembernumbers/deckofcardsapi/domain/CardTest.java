package com.icantremembernumbers.deckofcardsapi.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    public void testingImageCodes() {
        Card card = new Card(CardValue.ACE, CardSuite.HEARTS);

        assertEquals("AH",card.imageCode());

        card = new Card(CardValue.TEN,CardSuite.HEARTS);
        assertEquals("0H",card.imageCode());
    }

}