package com.icantremembernumbers.deckofcardsapi.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckApiSmokeTests {

    private DeckApi deckApi;
    @BeforeEach
    public void init(){
        deckApi = new DeckApi(null);
    }

    @Test
    public void ensureMapReturned(){

    }

}