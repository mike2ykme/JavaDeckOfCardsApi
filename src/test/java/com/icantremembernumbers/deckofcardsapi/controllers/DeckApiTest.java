package com.icantremembernumbers.deckofcardsapi.controllers;

import com.icantremembernumbers.deckofcardsapi.domain.Card;
import com.icantremembernumbers.deckofcardsapi.domain.Constants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeckApiTest {
    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @SuppressWarnings("unchecked")
    public void ensureNewDeckGenerated() throws Exception {
        final String url = String.format("http://localhost:%d/api/deck/new/shuffle", port);
        final Map<String, String> returnedObject = this.restTemplate.getForObject(url, Map.class);
        assertThat(returnedObject).containsKeys(Constants.DECK_ID, Constants.SHUFFLED, Constants.REMAINING_CARDS);
        assertEquals(returnedObject.get(Constants.SHUFFLED), "true");

        System.out.println(returnedObject.get(Constants.DECK_ID));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void updateExistingDeck() throws Exception {
        final String urlNew = String.format("http://localhost:%d/api/deck/new/shuffle", port);

        Map<String, String> results = this.restTemplate.getForObject(urlNew, Map.class);

        final String urlDrawTwo = String.format("http://localhost:%d/api/deck/%s/draw?count=2", port, results.get("deckId"));

        results = this.restTemplate.getForObject(urlDrawTwo, Map.class);

        assertThat(results).containsKey(Constants.STATUS).containsValue("success");
        assertThat(results).containsKey(Constants.REMAINING_CARDS).containsValue("50");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shuffleExistingDeck() throws Exception {
        // New Deck
        final String urlNew = String.format("http://localhost:%d/api/deck/new/shuffle", port);

        Map<String, Object> results = this.restTemplate.getForObject(urlNew, Map.class);

        final String urlPeek = String.format("http://localhost:%d/api/deck/%s/peek", port, results.get("deckId"));
        results = this.restTemplate.getForObject(urlPeek, Map.class);
        List<Card> cards = (List<Card>) results.get(Constants.CARDS);
        assertTrue(cards.size() == 1);

        assertThat(results).containsKey(Constants.STATUS).containsValue("success");
        assertThat(results).containsKey(Constants.REMAINING_CARDS).containsValue("52");

        final String shuffleUrl = String.format("http://localhost:%d/api/deck/%s/shuffle", port, results.get("deckId"));
        results = this.restTemplate.getForObject(shuffleUrl, Map.class);
        assertTrue(results.get(Constants.STATUS).equals("success"));


        results = this.restTemplate.getForObject(urlPeek, Map.class);
        List<Card> cards1 = (List<Card>) results.get(Constants.CARDS);
        assertTrue(cards1.size() == 1);

        assertNotEquals(cards.get(0), cards1.get(0));

        assertThat(results).containsKey(Constants.STATUS).containsValue("success");
        assertThat(results).containsKey(Constants.REMAINING_CARDS).containsValue("52");
    }
}

