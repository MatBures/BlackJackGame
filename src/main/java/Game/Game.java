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
        playersHand.clear();
        dealersHand.clear();
        dealCards();
        if (checkIfPlayerHasBlackJackStartingHand()) {
            System.out.println(separateBlock);
            return;
        }
        System.out.println("What you want to do next? You have these options:" + "\n" + "1) Stand" + "\n" + "2) Hit" + "\n" + "3) Double down" + "\n" + "4) Surrender");

        int option = tokenService.getValidatedIntegerInput();
        while(option<1 || option > 4) {
            System.out.println("Choose a number 1-4 for option.");
            option = tokenService.getValidatedIntegerInput();
            System.out.println(separateBlock);

        }
            if (option == 1) {
                stand();

            }
            if (option == 2) {
                hit();

            }
            if (option == 3) {
                if(tokenService.checkIfPlayerCanDoubleDown() == true) {
                    doubleDown();
                }
                else {
                    System.out.println("You have just these options:" + "\n" + "1) Stand" + "\n" + "2) Hit" + "\n" + "3) Surrender");
                    option = tokenService.getValidatedIntegerInput();
                while(option<1 || option > 3) {

                }
                if (option == 1) {
                    stand();
                }
                if (option == 2) {
                    hit();
                }
                if (option == 3) {
                    surrender();
                }
            }

            if (option == 4) {
                surrender();
            }
        }
    }

    public void stand() {
        System.out.println(separateBlock);
        System.out.println("Your hand: " + playersHand.get(0) + " / " + playersHand.get(1) + "." + " Your total hand value is " + playersHandValue);
        System.out.println("Dealer's hand: " + dealersHand.get(0) +" / " + dealersHand.get(1) + "." + " Dealer's total hand value is " + dealersHandValue);
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

    }

    public void hit() {
        int option;
        System.out.println(separateBlock);
        Cards playerCard = deck.dealCard();
        playersHand.add(playerCard);
        updatePlayersHandValue();
        System.out.println("You draw another card: " + playerCard + ". Your total hand value is " + playersHandValue );
        do {

            if (playersHandValue > 21) {
                System.out.println("Dealer's hand: " + dealersHand.get(0) +" / " + dealersHand.get(1) + "." + " Dealer's total hand value is " + dealersHandValue);
                tokenService.loseTokens();
                System.out.println(separateBlock);
                return;
            }
            System.out.println("Press number 1 for one more hit" + "\n" + "Press number 2 for stand");
            option = tokenService.getValidatedIntegerInput();
            if(option ==1) {
                Cards playerCards = deck.dealCard();
                playersHand.add(playerCards);
                updatePlayersHandValue();
                System.out.println("You draw another card: " + playerCards + ". Your total hand value is " + playersHandValue );
            }
            else if (option ==2) {
                stand();
                break;
            }
            else{
                System.out.println("You need to choose option 1 or 2.");
            }
        }while(true);

    }

    public void doubleDown() {
        tokenService.checkIfPlayerCanDoubleDown();
        System.out.println(separateBlock);
        System.out.println("Your hand: " + playersHand.get(0) + " / " + playersHand.get(1) + "." + " Your total hand value is " + playersHandValue);
        Cards playerCard = deck.dealCard();
        playersHand.add(playerCard);
        updatePlayersHandValue();
        System.out.println("You draw another card: " + playerCard + ". Your total hand value is " + playersHandValue );
        System.out.println("Dealer's hand: " + dealersHand.get(0) +" / " + dealersHand.get(1) + "." + " Dealer's total hand value is " + dealersHandValue);
        while (dealersHandValue < 17) {
            Cards dealerCard = deck.dealCard();
            dealersHand.add(dealerCard);
            updateDealersHandValue();
            System.out.println("Dealer draws another card: " + dealerCard + ". Dealer's total hand value is " + dealersHandValue );
        }
        if (dealersHandValue > 21) {
            tokenService.doubleDownWinTokens();
        } else if (playersHandValue > 21) {
            tokenService.doubleDownLoseTokens();
        } else if (playersHandValue > dealersHandValue) {
            tokenService.doubleDownWinTokens();
        } else if (playersHandValue == dealersHandValue) {
            System.out.println("It's a TIE.");
        } else {
            tokenService.doubleDownLoseTokens();
        }
        System.out.println(separateBlock);
    }

    public void surrender() {
        System.out.println(separateBlock);
        System.out.println("Your hand: " + playersHand.get(0) + " / " + playersHand.get(1) + "." + " Your total hand value is " + playersHandValue);
        System.out.println("Dealer's hand: " + dealersHand.get(0) +" / " + dealersHand.get(1) + "." + " Dealer's total hand value is " + dealersHandValue);
        tokenService.surrenderLoseTokens();
        System.out.println(separateBlock);
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






