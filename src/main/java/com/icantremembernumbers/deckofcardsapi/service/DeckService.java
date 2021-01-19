package com.icantremembernumbers.deckofcardsapi.service;

import com.icantremembernumbers.deckofcardsapi.domain.Card;
import com.icantremembernumbers.deckofcardsapi.domain.Constants;
import com.icantremembernumbers.deckofcardsapi.domain.Deck;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DeckService {
    private final ConcurrentHashMap<String, Deck> allDecks = new ConcurrentHashMap<>();

    public Deck createAndReturnDeck(boolean shuffled) {

        final Deck deck = shuffled
                ? Deck.newShuffledDeck(UUID.randomUUID().toString())
                : Deck.newUnshuffledDeck(UUID.randomUUID().toString());

        this.allDecks.put(deck.getId(), deck);
        return deck;
    }


    public Map<String, Object> drawCardsWithStatus(String deckId, String count) {
        final Deck deck = this.allDecks.get(deckId);

        if (deck == null)
            return Map.of(Constants.DECK_ID, deckId, Constants.STATUS, "failure", Constants.ERROR, "no deck found");

        List<Card> cards = deck.drawCards(Integer.parseInt(count));

        if (cards == null)
            return outOfCards(deckId, deck);

        return Map.of(Constants.DECK_ID, deckId,
                Constants.STATUS, "success",
                Constants.REMAINING_CARDS, Integer.toString(deck.cardsRemaining()),
                Constants.CARDS, cards);
    }

    public Map<String, Object> peekCardsWithStatus(String deckId, String count) {
        final Deck deck = this.allDecks.get(deckId);

        if (deck == null)
            return Map.of(Constants.DECK_ID, deckId, Constants.STATUS, "failure", Constants.ERROR, "no deck found");

        final List<Card> cards = deck.peekCard(Integer.parseInt(count));

        if (cards == null)
            return outOfCards(deckId, deck);

        return Map.of(Constants.DECK_ID, deckId,
                Constants.STATUS, "success",
                Constants.REMAINING_CARDS, Integer.toString(deck.cardsRemaining()),
                Constants.CARDS, cards);
    }

    private static Map<String, Object> outOfCards(String deckId, Deck deck) {
        return Map.of(Constants.DECK_ID, deckId,
                Constants.STATUS, "failure",
                Constants.ERROR, "Not enough cards",
                Constants.REMAINING_CARDS, Integer.toString(deck.cardsRemaining()));
    }

    public Map<String, Object> shuffleDeck(String deckId) {
        final Deck deck = this.allDecks.get(deckId);

        if (deck == null)
            return Map.of(Constants.DECK_ID, deckId, Constants.STATUS, "failure", Constants.ERROR, "no deck found");

        deck.shuffle();
        this.allDecks.put(deck.getId(),deck);

        return Map.of(Constants.DECK_ID, deckId,
                Constants.STATUS, "success",
                Constants.REMAINING_CARDS, Integer.toString(deck.cardsRemaining()));
    }
}
