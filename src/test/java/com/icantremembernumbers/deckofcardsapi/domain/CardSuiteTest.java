package com.icantremembernumbers.deckofcardsapi.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardSuiteTest {

    @Test
    public void testGetSuiteCompare() {
        final CardSuite h = CardSuite.get("H");
        assertEquals(CardSuite.HEARTS, h);
    }

}