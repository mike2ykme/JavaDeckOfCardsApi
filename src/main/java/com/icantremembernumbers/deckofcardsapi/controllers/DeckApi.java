package com.icantremembernumbers.deckofcardsapi.controllers;

import com.icantremembernumbers.deckofcardsapi.domain.Card;
import com.icantremembernumbers.deckofcardsapi.domain.Constants;
import com.icantremembernumbers.deckofcardsapi.domain.Deck;
import com.icantremembernumbers.deckofcardsapi.service.DeckService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/deck")
public class DeckApi {

    DeckService service;

    public DeckApi(DeckService service) {
        this.service = service;
    }

    @GetMapping("/new/shuffle")
    public Map<String, String> getNewShuffledDeck() {
        final Deck shuffledDeck = this.service.createAndReturnDeck(true);
        return Map.of(Constants.STATUS, "success",
                Constants.SHUFFLED, "true",
                Constants.DECK_ID, shuffledDeck.getId(),
                Constants.REMAINING_CARDS, Integer.toString(shuffledDeck.cardsRemaining()));
    }

    @GetMapping("/{deckId}/draw")
    public Map<String, Object> drawCards(@PathVariable String deckId, @RequestParam(defaultValue = "1") String count) {
        return this.service.drawCardsWithStatus(deckId, count);
    }

    @GetMapping("/{deckId}/peek")
    public Map<String, Object> peekCard(@PathVariable String deckId, @RequestParam(defaultValue = "1") String count) {
        return this.service.peekCardsWithStatus(deckId, count);
    }

    @GetMapping("/{deckId}/shuffle")
    public Map<String, Object> shuffleDeck(@PathVariable String deckId) {
        return this.service.shuffleDeck(deckId);
    }

    @GetMapping("/{deckId}/pile/")
    public Map<String, Object> allPiles(@PathVariable String deckId) {
        return this.service.allPiles(deckId);
    }
    @GetMapping("/{deckId}/pile/{pileName}/add/")
    public Map<String, Object> addPile(@PathVariable String deckId, @PathVariable String pileName, @RequestParam(required = false) Optional<String> cards) {
        return this.service.addPileToDeck(deckId, pileName, cards, false);
    }

    @GetMapping("/{deckId}/pile/{pileName}/shuffle/")
    public Map<String, Object> shuffleDeck(@PathVariable String deckId, @PathVariable String pileName) {
        return this.service.shufflePile(deckId,pileName);
    }

}


/*
public void init() {
        BASE_URL = format("http://localhost:%d/api/deck/", port);
        URL_NEW = BASE_URL + "new/shuffle";
        URL_DRAW = BASE_URL + "%s/draw?count=%d";
        URL_SHUFFLE = BASE_URL + "%s/shuffle";
        URL_PEEK = BASE_URL + "%s/peek";
        URL_ADD_PILE = BASE_URL + "/%s/pile/%s/add/";
    }
 */