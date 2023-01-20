package GameModels;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Deck class holds generating deck method for playing blackjack and reset deck after each game.
 */
public class Deck {
    private ArrayList<Cards> cards;

    public Deck() {
        cards = new ArrayList<Cards>();
        generateDeck();
    }

    //Method when game deals cards, each card is removed from deck. In game is only one specific card with RANK/SUIT.
    public Cards dealCard() {
        return cards.remove(0);
    }

    //Method for generating deck when game starts.
    public void generateDeck() {
        for (String rank : Cards.RANKS) {
            for (String suit : Cards.SUITS) {
                Cards card = new Cards(rank, suit);
                if (rank.equals("J") || rank.equals("Q") || rank.equals("K")) {
                    card.setValue(10);
                } else if (rank.equals("A")) {
                    card.setValue(11);
                } else {
                    card.setValue(Integer.parseInt(rank));
                }
                cards.add(card);
            }
        }
        Collections.shuffle(cards);
    }

    //Method for resetting deck (each game start with all cards).
    public void resetDeck() {
        cards.clear();
        generateDeck();
    }


}

