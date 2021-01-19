package com.icantremembernumbers.deckofcardsapi.service;

import com.icantremembernumbers.deckofcardsapi.domain.Card;
import com.icantremembernumbers.deckofcardsapi.domain.Constants;
import com.icantremembernumbers.deckofcardsapi.domain.Deck;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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
            return noDeck(deckId);

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
            return noDeck(deckId);

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

    public static Map<String, Object> noDeck(String deckId) {
        return Map.of(Constants.DECK_ID, deckId, Constants.STATUS, "failure", Constants.ERROR, "no deck found");
    }

    public Map<String, Object> shuffleDeck(String deckId) {
        final Deck deck = this.allDecks.get(deckId);

        if (deck == null)
            return noDeck(deckId);

        deck.shuffle();
        this.allDecks.put(deck.getId(), deck);

        return Map.of(Constants.DECK_ID, deckId,
                Constants.STATUS, "success",
                Constants.REMAINING_CARDS, Integer.toString(deck.cardsRemaining()));
    }

    public Map<String, Object> addPileToDeck(String deckId, String pileName, Optional<String> cards, boolean shuffle) {
        final Deck deck = this.allDecks.get(deckId);

        if (deck == null)
            return noDeck(deckId);

        final boolean b = deck.newDeckPile(pileName, cards, shuffle);
        final List<Card> pile = deck.getPile(pileName);
        return Map.of(Constants.DECK_ID, deckId,
                Constants.STATUS, Constants.SUCCESS,
                Constants.REMAINING_CARDS, Integer.toString(deck.cardsRemaining()),
                Constants.PILES, Map.of(
                        pileName, Map.of(
                                Constants.STATUS, Boolean.toString(b),
                                Constants.REMAINING_CARDS, Integer.toString(pile.size())
                        )
                )
        );

    }

    public Map<String, Object> shufflePile(String deckId, String pileName) {
        final Deck deck = this.allDecks.get(deckId);
        if (deck == null)
            return noDeck(deckId);

        boolean shuffled = deck.shufflePile(pileName);

        return Map.of(Constants.DECK_ID, deckId,
                Constants.STATUS, Constants.SUCCESS,
                Constants.REMAINING_CARDS, Integer.toString(deck.cardsRemaining()),
                Constants.PILES, Map.of(
                        pileName, Map.of(
                                Constants.STATUS, Constants.SUCCESS,
                                Constants.SHUFFLED, Boolean.toString(shuffled)
                        )
                )
        );
    }

    public Map<String, Object> allPiles(String deckId) {
        final Deck deck = this.allDecks.get(deckId);

        if (deck == null)
            return noDeck(deckId);

//        final List<String> allPileNames = deck.getAllPileNames();

        return Map.of(
                Constants.DECK_ID, deckId,
                Constants.PILES, deck.getAllPileNames()
        );
    }
}
/*
public Map<String, Object> shuffleDeck(String deckId) {
        final Deck deck = this.allDecks.get(deckId);

        if (deck == null)
            return noDeck(deckId);

        deck.shuffle();
        this.allDecks.put(deck.getId(),deck);

        return Map.of(Constants.DECK_ID, deckId,
                Constants.STATUS, "success",
                Constants.REMAINING_CARDS, Integer.toString(deck.cardsRemaining()));
    }
 */