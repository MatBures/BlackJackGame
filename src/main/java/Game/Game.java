package Game;

import GameModels.Cards;
import GameModels.Deck;
import Services.DatabaseService;
import Services.TokenService;

import java.util.ArrayList;
import java.util.List;

public class Game {
    DatabaseService databaseService;
    TokenService tokenService;
    Deck deck = new Deck();
    private List<Cards> playersHand = new ArrayList<>();
    private List<Cards> dealersHand = new ArrayList<>();
    private Cards hiddenDealerCard;
    private int playersHandValue;
    private int dealersHandValue;
    private String separateBlock = "-----------------------------------";

    public Game(DatabaseService databaseService, TokenService tokenService) {
        this.databaseService = databaseService;
        this.tokenService = tokenService;
    }

    public void startGame() {
        dealCards();
        if (checkIfPlayerHasBlackJackStartingHand()) {
            System.out.println(separateBlock);
            return;
        }
        System.out.println("What you want to do next? You have these options:" + "\n" + "1) Stand" + "\n" + "2) Hit" + "\n" + "3) Double down" + "\n" + "4) Surrender");

        int option = tokenService.getValidatedIntegerInput();
        while(option<1 || option > 4) {
            System.out.println("Wrong number. Please choose 1-4.");
            option = tokenService.getValidatedIntegerInput();

        }

            if (option == 1) {
                stand();

            }
            if (option == 2) {
                hit();

            }
            if (option == 3) {
                doubleDown();

            }
            if (option == 4) {
                surrender();

            }
        }

    public void hit() {

    }

    public void stand() {
        System.out.println(separateBlock);
        System.out.println("Your hand: " + playersHand.get(0) + " / " + playersHand.get(1) + "." + " Your total hand value is " + playersHandValue);
        System.out.println("Dealer's hand: " + dealersHand.get(0) +" / " + dealersHand.get(1) + "." + " Dealer's hand value is " + dealersHandValue);
        while (dealersHandValue < 17) {
            Cards dealerCard = deck.dealCard();
            dealersHand.add(dealerCard);
            updateDealersHandValue();
            System.out.println("Dealer draws another card: " + dealerCard + ". Dealer's total hand value is " + dealersHandValue );
        }
        if (dealersHandValue > 21) {
            tokenService.winTokens();
        } else if (playersHandValue > 21) {
            tokenService.loseTokens();
        } else if (playersHandValue > dealersHandValue) {
            tokenService.winTokens();
        } else if (playersHandValue == dealersHandValue) {
            System.out.println("It's a TIE.");
        } else {
            tokenService.loseTokens();
        }
        System.out.println(separateBlock);
        playersHand.clear();
        dealersHand.clear();
    }

    public void doubleDown() {

    }

    public void surrender() {

    }

    public void dealCards() {
        playersHand.add(deck.dealCard());
        playersHand.add(deck.dealCard());
        dealersHand.add(deck.dealCard());
        // add hidden card to hiddenDealerCard variable
        hiddenDealerCard = deck.dealCard();
        dealersHand.add(hiddenDealerCard);
        updatePlayersHandValue();
        updateDealersHandValue();
        System.out.println("Your hand: " + playersHand.get(0) + " / " + playersHand.get(1) +"." + " Your total hand value is " + playersHandValue);
        System.out.println("Dealer's hand: " + dealersHand.get(0) +".");
    }

    public void updatePlayersHandValue() {
        playersHandValue = 0;
        for (Cards card : playersHand) {
            playersHandValue += card.getValue();
        }
        for (Cards card : playersHand) {
            if (card.getRank().equals("A") && playersHandValue > 21) {
                card.setValue(1);
                playersHandValue -= 10;
            }
        }
    }

    public void updateDealersHandValue() {
        dealersHandValue = 0;
        for (Cards card : dealersHand) {
            dealersHandValue += card.getValue();
        }
        for (Cards card : dealersHand) {
            if (card.getRank().equals("A") && dealersHandValue > 21) {
                card.setValue(1);
                dealersHandValue -= 10;
            }
        }
    }

    public boolean checkIfPlayerHasBlackJackStartingHand() {
        if (playersHandValue == 21) {
            if(dealersHandValue == 21){
                System.out.println("both have BLACKJACK. no one wins.");
            }
            else {
                System.out.println("BLACKJACK. ");
                tokenService.winTokens();

            }
            return true;
        }
        return false;
    }

}






