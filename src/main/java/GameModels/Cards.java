package GameModels;

import java.util.Arrays;
import java.util.List;

/**
 * Creating and setting Cards parameters.
 */
public class Cards {
    private String rank;
    private String suit;
    private int value;

    public Cards(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    //Made two lists for RANKS and SUITS of cards. Needed "parameters" for generating deck in Deck class.
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