package com.icantremembernumbers.deckofcardsapi.controllers;

import com.icantremembernumbers.deckofcardsapi.domain.Card;
import com.icantremembernumbers.deckofcardsapi.domain.Constants;
import com.icantremembernumbers.deckofcardsapi.domain.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static java.lang.String.format;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeckApiTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    String BASE_URL;// = format("http://localhost:%d/api/deck/", port);
    String URL_NEW;// = BASE_URL + "new/shuffle";
    String URL_DRAW;// = BASE_URL + "%s/draw?count=%d";
    String URL_SHUFFLE;// = BASE_URL + "%s/shuffle";
    String URL_PEEK;// = BASE_URL + "%s/peek";
    String URL_ADD_PILE;// = BASE_URL + "%s/peek";
    String URL_ALL_PILES;// = BASE_URL + "%s/peek";

    @BeforeEach
    public void init() {
        BASE_URL = format("http://localhost:%d/api/deck/", port);
        URL_NEW = BASE_URL + "new/shuffle";
        URL_DRAW = BASE_URL + "%s/draw?count=%d";
        URL_SHUFFLE = BASE_URL + "%s/shuffle";
        URL_PEEK = BASE_URL + "%s/peek";
        URL_ADD_PILE = BASE_URL + "/%s/pile/%s/add/";
        URL_ALL_PILES = BASE_URL + "/%s/pile/";
    }

    @Test
    @SuppressWarnings("unchecked")
    public void ensureNewDeckGenerated() throws Exception {
        // New Deck
        final Map<String, String> returnedObject = this.restTemplate.getForObject(URL_NEW, Map.class);
        assertThat(returnedObject).containsKeys(Constants.DECK_ID, Constants.SHUFFLED, Constants.REMAINING_CARDS);
        assertEquals(returnedObject.get(Constants.SHUFFLED), "true");

        System.out.println(returnedObject.get(Constants.DECK_ID));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void updateExistingDeck() throws Exception {
        // New Deck
        Map<String, String> results = this.restTemplate.getForObject(URL_NEW, Map.class);

        final String urlDrawTwo = format(URL_DRAW, results.get("deckId"), 2);

        results = this.restTemplate.getForObject(urlDrawTwo, Map.class);

        assertThat(results).containsKey(Constants.STATUS).containsValue("success");
        assertThat(results).containsKey(Constants.REMAINING_CARDS).containsValue("50");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shuffleExistingDeck() throws Exception {
        // New Deck
        System.out.println(URL_NEW);
        Map<String, Object> results = this.restTemplate.getForObject(URL_NEW, Map.class);

        results = this.restTemplate.getForObject(format(URL_PEEK, results.get("deckId")), Map.class);
        List<Card> peekCards = (List<Card>) results.get(Constants.CARDS);
        assertTrue(peekCards.size() == 1);

        assertThat(results).containsKey(Constants.STATUS).containsValue("success");
        assertThat(results).containsKey(Constants.REMAINING_CARDS).containsValue("52");

        Map<String, Object> drawResults = this.restTemplate.getForObject(format(URL_DRAW, results.get("deckId"), 1), Map.class);

        List<Card> drawCards = (List<Card>) drawResults.get(Constants.CARDS);

        results = this.restTemplate.getForObject(format(URL_SHUFFLE, results.get("deckId")), Map.class);
        assertTrue(results.get(Constants.STATUS).equals("success"));
        assertEquals(peekCards.get(0), drawCards.get(0));


        results = this.restTemplate.getForObject(format(URL_PEEK, results.get("deckId")), Map.class);
        List<Card> cards1 = (List<Card>) results.get(Constants.CARDS);
        assertTrue(cards1.size() == 1);

        assertNotEquals(peekCards.get(0), cards1.get(0));

        assertThat(results).containsKey(Constants.STATUS).containsValue("success");
        assertThat(results).containsKey(Constants.REMAINING_CARDS).containsValue("51");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testCreatingNewPileAndListingThem() {
        // New Deck
        Map<String, Object> newDeck = this.restTemplate.getForObject(URL_NEW, Map.class);

        // Add Pile to it
        String pile_name = "new_pile_name";
        final Map results = (Map<String, Object>) restTemplate.getForObject(format(URL_ADD_PILE, newDeck.get(Constants.DECK_ID), pile_name), Map.class);

        // Ensure there's specific properties
        assertThat(results).containsKeys(Constants.STATUS, Constants.DECK_ID, Constants.REMAINING_CARDS, Constants.PILES);
        final Map<String, Map<String, String>> piles = (Map<String, Map<String, String>>) results.get(Constants.PILES);
        assertThat(piles).containsKeys(pile_name);
        final String remainingCrds = piles.get(pile_name).get(Constants.REMAINING_CARDS);
        assertThat(remainingCrds).isEqualTo("52");

        // Ensure we can see all Piles
        final Map allPiles = (Map<String, Object>)
                restTemplate.getForObject(format(URL_ALL_PILES, newDeck.get(Constants.DECK_ID)), Map.class);

        assertThat(allPiles).containsKeys(Constants.DECK_ID, Constants.PILES);
        final List<String> pileNames = (List<String>) allPiles.get(Constants.PILES);
        assertThat(pileNames).contains(pile_name, Deck.REMAINING_CARDS, Deck.DISCARDED_CARDS);

    }
}
/*
{
    "success": true,
    "deck_id": "3p40paa87x90",
    "remaining": 12,
    "piles": {
        "discard": {
            "remaining": 2
        }
    }
}
 */
