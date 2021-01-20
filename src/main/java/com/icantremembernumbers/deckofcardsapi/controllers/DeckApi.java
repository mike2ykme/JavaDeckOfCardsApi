package com.icantremembernumbers.deckofcardsapi.controllers;

import com.icantremembernumbers.deckofcardsapi.domain.Constants;
import com.icantremembernumbers.deckofcardsapi.domain.Deck;
import com.icantremembernumbers.deckofcardsapi.service.DeckService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
        // Using a volatile variable and resetting multiple times since it'll be the same value regardless of how many
        // times it's set.
        if (service.baseUrl == null)
            service.baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toString();

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


    /*
        Pile Methods
     */
    @GetMapping("/{deckId}/pile")
    public Map<String, Object> allPiles(@PathVariable String deckId) {
        return this.service.allPiles(deckId);
    }

    @GetMapping("/{deckId}/pile/{pileName}/add")
    public Map<String, Object> addPile(@PathVariable String deckId, @PathVariable String pileName, @RequestParam(required = false) Optional<String> cards) {
        return this.service.addPileToDeck(deckId, pileName, cards, false);
    }

    @GetMapping("/{deckId}/pile/{pileName}/shuffle")
    public Map<String, Object> shuffleDeck(@PathVariable String deckId, @PathVariable String pileName) {
        return this.service.shufflePile(deckId, pileName);
    }

    @GetMapping("/{deckId}/pile/{pileName}/peek")
    public Map<String, Object> peekPileCard(@PathVariable String deckId, @PathVariable String pileName, @RequestParam(required = false) Optional<Integer> count) {
        return this.service.peekPile(deckId, pileName, count);
    }

    @GetMapping("/{deckId}/pile/{pileName}/draw")
    public Map<String, Object> drawPileCard(@PathVariable String deckId, @PathVariable String pileName, @RequestParam(required = false) Optional<Integer> count) {
//        System.out.println(ServletUriComponentsBuilder.fromCurrentContextPath().build().toString());
        return this.service.drawPile(deckId, pileName, count);
    }
}

