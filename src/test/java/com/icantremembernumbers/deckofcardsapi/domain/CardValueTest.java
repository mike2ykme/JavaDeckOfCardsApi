package com.icantremembernumbers.deckofcardsapi.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardValueTest {

    @Test
    public void testGetByString(){
        CardValue v = CardValue.get("A");
        assertEquals(v, CardValue.ACE);
    }

}