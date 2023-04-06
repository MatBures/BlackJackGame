package Services;

import GameModels.Card;


import java.util.ArrayList;
import java.util.Collections;

public class DeckService {
    private ArrayList<Card> cards;
    //Method for generating deck when game starts.
    public void generateDeck() {
        for (String rank : Card.RANKS) {
            for (String suit : Card.SUITS) {
                Card card = new Card(rank, suit);
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

    //Method when game deals cards, each card is removed from deck. In game is only one specific card with RANK/SUIT.
    public Card dealCard() {
        return cards.remove(0);
    }

    //Method for resetting deck (each game start with all cards).
    public void resetDeck() {
        if (cards == null) {
            cards = new ArrayList<Card>();
        }
        cards.clear();
        generateDeck();
    }

}

