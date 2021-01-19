package com.icantremembernumbers.deckofcardsapi.domain;

import org.springframework.lang.Nullable;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;

public class Deck {
    public static final String DISCARDED_CARDS = "discardedCards";
    public static final String REMAINING_CARDS = "remainingCards";
    private final String id;
    private final HashMap<String, List<Card>> allPiles = new HashMap<>();

    private Deck(List<Card> remainingCards, String id) {
        this.id = id;
        this.allPiles.put(REMAINING_CARDS, remainingCards);
        this.allPiles.put(DISCARDED_CARDS, new ArrayList<>(52));
    }

    public static Deck newUnshuffledDeck(String id) {
        return new Deck(AllCards.baseDeck(), id);
    }

    public static Deck newShuffledDeck(String id) {
        return new Deck(AllCards.shuffledDeck(), id);
    }


    synchronized public int cardsRemaining() {
        return getPile(REMAINING_CARDS).size();

    }

    public synchronized List<Card> getPile(String name) {
        return this.allPiles.get(name);
    }

    synchronized @Nullable
    public Card getCard() {
        if (cardsRemaining() > 0) {
            final Card removed = getPile(REMAINING_CARDS).remove(0);
            getPile(DISCARDED_CARDS).add(removed);
            return removed;
        }

        return null;
    }

    synchronized @Nullable
    public Card peekCard() {
        if (cardsRemaining() > 0)
            return getPile(REMAINING_CARDS).get(0);
//            return this.remainingCards.get(0);

        return null;
    }

    synchronized @Nullable
    public List<Card> peekCard(int i) {
        if (cardsRemaining() >= i) {
            List<Card> cards = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                cards.add(getPile(REMAINING_CARDS).get(j));
            }
            return cards;
        }
        return null;
    }

    synchronized @Nullable
    public Card getLastDiscard() {
        if (getPile(DISCARDED_CARDS).size() > 0)
            return getPile(DISCARDED_CARDS).get(getPile(DISCARDED_CARDS).size() - 1);
        return null;
    }

    synchronized public void shuffle() {
        Collections.shuffle(getPile(REMAINING_CARDS), new Random(System.currentTimeMillis()));
    }

    public String getId() {
        return this.id;
    }

    synchronized public @Nullable
    List<Card> drawCards(int i) {
        if (cardsRemaining() >= i) {
            ArrayList<Card> cards = new ArrayList<>(i);
            for (int j = 0; j < i; j++) {
                cards.add(getCard());
            }
            return cards;
        }
        return null;
    }

    synchronized public boolean newDeckPile(String pileName, boolean shuffled) {

        allPiles.put(pileName, shuffled
                ? AllCards.shuffledDeck()
                : AllCards.baseDeck()
        );
        return true;
    }

    synchronized public boolean newDeckPile(String pileName, String[] cardSubset, boolean shuffled) {
        final List<Card> filteredSubsetofCards = Arrays.stream(cardSubset)
                .filter(s -> s.length() == 2)
                .map(Deck::stringToCard)
                .collect(Collectors.toList());
        if (shuffled)
            Collections.shuffle(filteredSubsetofCards, new Random(System.currentTimeMillis()));

        allPiles.put(pileName, filteredSubsetofCards);

        return true;
    }

    synchronized public boolean newDeckPile(String pileName, Optional<String> cards, boolean shuffled) {
        if (cards.isPresent()) {
            final String[] split = cards.get().split(",");
            return newDeckPile(pileName, split, shuffled);
        }
        return newDeckPile(pileName, shuffled);
    }

    private static Card stringToCard(String s) {
        final String upperCased = s.toUpperCase();
        final String value = upperCased.substring(0, 1);
        final String suite = upperCased.substring(1, 2);
        return new Card(value, suite);
    }

    synchronized public boolean containsPile(String pileName) {
        return allPiles.containsKey(pileName);
    }

    synchronized public boolean shufflePile(String pileName) {
        if (containsPile(pileName)) {
            Collections.shuffle(allPiles.get(pileName));
            return true;
        }
        return false;
    }

    synchronized public List<String> getAllPileNames() {
        return allPiles.entrySet()
                .stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
