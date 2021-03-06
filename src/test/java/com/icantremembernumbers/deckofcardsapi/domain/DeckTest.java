package com.icantremembernumbers.deckofcardsapi.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    Deck shuffled;
    Deck unshuffled;

    @BeforeEach
    public void init() {
        unshuffled = Deck.newUnshuffledDeck("DECK_ID");
        shuffled = Deck.newShuffledDeck("DECK_SHUFFLED");
    }

    @Test
    public void createNewDeck() {
        assertEquals(52, unshuffled.cardsRemaining());
        assertEquals(52, shuffled.cardsRemaining());
    }

    @Test
    public void getTopCard() {
        Card topCard = unshuffled.getCard();
        assertEquals(51, unshuffled.cardsRemaining());
        //ace of clubs
        assertEquals(CardSuite.CLUBS, topCard.getSuite());
        assertEquals(CardValue.ACE, topCard.getValue());

        Card peekTopCard = shuffled.peekCard();
        assertEquals(52, shuffled.cardsRemaining());
        Card shuffledTopCard = shuffled.getCard();
        assertEquals(51, shuffled.cardsRemaining());

        assertEquals(peekTopCard, shuffledTopCard);
        System.out.println(peekTopCard);
        System.out.println(shuffledTopCard);
    }

    @Test
    public void putTopCardInDiscard() {
        Card topCard = shuffled.getCard();
        Card discarded = shuffled.getLastDiscard();
        assertEquals(topCard, discarded);

        Card peekCard = shuffled.peekCard();
        discarded = shuffled.getLastDiscard();
        assertNotEquals(peekCard, discarded);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testShuffleAgain() {
//        List<Card> remainingCards = (List<Card>) ReflectionTestUtils.getField(shuffled, Deck.class, "remainingCards");
        Map<String,List<Card>> allPiles = (Map<String,List<Card>>) ReflectionTestUtils.getField(shuffled, Deck.class, "allPiles");
//        List<Card> remainingCards = (List<Card>) ReflectionTestUtils.getField(shuffled, Deck.class, "remainingCards");
        List<Card> oldRemainingCards = new ArrayList<>(allPiles.get(Deck.REMAINING_CARDS));
        shuffled.shuffle();
//        remainingCards = (List<Card>) ReflectionTestUtils.getField(shuffled, Deck.class, "remainingCards");
        List<Card> remainingCards = allPiles.get(Deck.REMAINING_CARDS);

        assertNotEquals(oldRemainingCards, remainingCards);
    }

    @Test
    public void testGetMultipleCards() {
        List<Card> drawnCards = unshuffled.drawCards(2);
        List<Card> overDraw = unshuffled.drawCards(500);

        assertThat(drawnCards).contains(new Card(CardValue.ACE, CardSuite.CLUBS), new Card(CardValue.TWO, CardSuite.CLUBS));
        assertNotNull(drawnCards);
        assertNull(overDraw);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testAddNewDeckPile(){
        String TEST_ADD_PILE = "TEST_ADD_PILE";
        String TEST_ADD_PILE_SUBSET = "TEST_ADD_PILE_SUBSET";
//        final Deck shuffledDeck = Deck.newShuffledDeck("TEST_ADD_PILE");

        boolean result = shuffled.newDeckPile(TEST_ADD_PILE, false);
        assertTrue(shuffled.containsPile(TEST_ADD_PILE));

        boolean result1 = shuffled.newDeckPile(TEST_ADD_PILE_SUBSET,new String[]{"AH","QH"}, true);
        assertTrue(shuffled.containsPile(TEST_ADD_PILE_SUBSET));

        final Map<String,List<Card>> allPiles = (Map<String,List<Card>>) ReflectionTestUtils.getField(shuffled, Deck.class, "allPiles");
        final List<Card> cards = allPiles.get(TEST_ADD_PILE_SUBSET);
        assertTrue(cards.size() ==2);
        assertThat(cards).contains(new Card(CardValue.ACE,CardSuite.HEARTS),new Card(CardValue.QUEEN,CardSuite.HEARTS));

    }
}