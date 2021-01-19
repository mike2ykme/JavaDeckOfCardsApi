package com.icantremembernumbers.deckofcardsapi.service;

import com.icantremembernumbers.deckofcardsapi.domain.Constants;
import com.icantremembernumbers.deckofcardsapi.domain.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


class DeckServiceTest {
    DeckService deckService;

    @BeforeEach
    public void init(){
        deckService = new DeckService();
    }
    @Test
    public void createNewDeck(){
        Deck deck = deckService.createAndReturnDeck(true);
    }

    @Test
    public void getSomeCards(){
        Deck deck = deckService.createAndReturnDeck(false);
        Map<String, Object> stringObjectMap = deckService.drawCardsWithStatus(deck.getId(), "2");
        assertThat(stringObjectMap.get(Constants.STATUS)).isEqualTo("success");


        stringObjectMap = deckService.drawCardsWithStatus(deck.getId(), "500");
        assertThat(stringObjectMap.get(Constants.STATUS)).isEqualTo("failure");
    }

}