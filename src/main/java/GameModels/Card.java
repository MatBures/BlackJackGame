package GameModels;

import java.util.Arrays;
import java.util.List;

/**
 * Card model.
 */
public class Card {
    private String rank;
    private String suit;
    private int value;

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public static final List<String> RANKS = Arrays.asList("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A");
    public static final List<String> SUITS = Arrays.asList("Hearts","Diamonds","Spades","Clubs");

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getRank() {
        return rank;
    }

    public String toString() {
        return rank + " of " + suit;
    }
}