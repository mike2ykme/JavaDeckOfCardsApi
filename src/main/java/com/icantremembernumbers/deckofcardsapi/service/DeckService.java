package com.icantremembernumbers.deckofcardsapi.service;

import com.icantremembernumbers.deckofcardsapi.domain.ApiCard;
import com.icantremembernumbers.deckofcardsapi.domain.Card;
import com.icantremembernumbers.deckofcardsapi.domain.Constants;
import com.icantremembernumbers.deckofcardsapi.domain.Deck;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class DeckService {
    private final ConcurrentHashMap<String, Deck> allDecks = new ConcurrentHashMap<>();
    public volatile String baseUrl;

    public Deck createAndReturnDeck(boolean shuffled) {
        System.out.println();
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

        List<ApiCard> apiCards = cards.stream().map(card -> new ApiCard(card, getImagePath())).collect(Collectors.toList());


        return Map.of(Constants.DECK_ID, deckId,
                Constants.STATUS, "success",
                Constants.REMAINING_CARDS, Integer.toString(deck.cardsRemaining()),
                Constants.CARDS, apiCards);
    }

    private String getImagePath() {
        // We're checking for null, but without a deck this can't get any cards & without that it'll return early.
        if (baseUrl == null)
            return "http://localhost:8080" + "/images/";

        return baseUrl + "/images/";
    }

    public Map<String, Object> peekCardsWithStatus(String deckId, String count) {
        final Deck deck = this.allDecks.get(deckId);

        if (deck == null)
            return noDeck(deckId);

        final List<Card> cards = deck.peekCard(Integer.parseInt(count));

        if (cards == null)
            return outOfCards(deckId, deck);

        List<ApiCard> apiCards = cards.stream().map(card -> new ApiCard(card, getImagePath())).collect(Collectors.toList());

        return Map.of(Constants.DECK_ID, deckId,
                Constants.STATUS, "success",
                Constants.REMAINING_CARDS, Integer.toString(deck.cardsRemaining()),
                Constants.CARDS, apiCards);
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

    public static Map<String, Object> noPile(String deckId, String pileName) {
        return Map.of(Constants.DECK_ID, deckId,
                Constants.STATUS, "failure",
                Constants.ERROR, "no pile found",
                Constants.PILES, pileName);
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
        List<Card> pile = deck.getPile(pileName);

        if (pile == null)
            pile = new ArrayList<>();

//        List<ApiCard> apiCards = pile.stream().map(card -> new ApiCard(card, getImagePath())).collect(Collectors.toList());

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

    public Map<String, Object> peekPile(String deckId, String pileName, Optional<Integer> count) {
        final Deck deck = this.allDecks.get(deckId);

        if (deck == null)
            return noDeck(deckId);

        List<Card> pile = deck.getPile(pileName);

        if (pile == null)
            return noPile(deckId, pileName);

        List<Card> peekCards = deck.peekPile(pileName, count);
        if (peekCards == null)
            peekCards = new ArrayList<>();

        List<ApiCard> apiCards = peekCards.stream().map(card -> new ApiCard(card, getImagePath())).collect(Collectors.toList());

        return Map.of(Constants.DECK_ID, deckId,
                Constants.STATUS, Constants.SUCCESS,
                Constants.REMAINING_CARDS, Integer.toString(deck.cardsRemaining()),
                pileName, Map.of(
                        Constants.STATUS, Constants.SUCCESS,
                        Constants.CARDS, apiCards
                )
        );
//        return Map.of(Constants.DECK_ID, deckId,
//                Constants.STATUS, Constants.SUCCESS,
//                Constants.REMAINING_CARDS, Integer.toString(deck.cardsRemaining()),
//                Constants.PILES, Map.of(
//                        pileName, Map.of(
//                                Constants.STATUS, Constants.SUCCESS,
//                                Constants.CARDS, peekCards
//                        )
//                )
//        );
    }

    public Map<String, Object> drawPile(String deckId, String pileName, Optional<Integer> count) {
        final Deck deck = this.allDecks.get(deckId);

        if (deck == null)
            return noDeck(deckId);

        List<Card> pile = deck.getPile(pileName);

        if (pile == null)
            return noPile(deckId, pileName);


        List<Card> drawCards = deck.drawPile(pileName, count);

        if (drawCards == null)
            drawCards = new ArrayList<>();

        List<ApiCard> apiCards = drawCards.stream().map(card -> new ApiCard(card, getImagePath())).collect(Collectors.toList());

        return Map.of(Constants.DECK_ID, deckId,
                Constants.STATUS, Constants.SUCCESS,
                Constants.REMAINING_CARDS, Integer.toString(deck.cardsRemaining()),
                pileName, Map.of(
                        Constants.STATUS, Constants.SUCCESS,
                        Constants.CARDS, apiCards
                )
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