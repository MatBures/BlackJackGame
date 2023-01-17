package GameModels;


import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Cards> cards;

    public Deck() {
        cards = new ArrayList<Cards>();
        generateDeck();
    }

    public Cards dealCard() {
        return cards.remove(0);
    }

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


}

