package com.icantremembernumbers.deckofcardsapi.controllers;

import com.icantremembernumbers.deckofcardsapi.domain.Card;
import com.icantremembernumbers.deckofcardsapi.domain.Constants;
import com.icantremembernumbers.deckofcardsapi.domain.Deck;
import com.icantremembernumbers.deckofcardsapi.service.DeckService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

}
