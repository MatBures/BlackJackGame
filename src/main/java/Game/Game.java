package Game;

import GameModels.Cards;
import GameModels.Deck;
import Services.DatabaseService;
import Services.TokenService;

import java.util.ArrayList;
import java.util.List;

/** Game class is the most important class for working of blackjack game.
 * It holds methods for checking if player has blackjack on opening hand, updating player's/dealer's hand value.
 * And methods like:
 * 1) Start game
 * 2) Stand option
 * 3) Hit option
 * 4) Double down option
 * 5) Surrender option
 */
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

    //Defining the Game for cooperating with other classes (DatabaseService and TokenService)
    public Game(DatabaseService databaseService, TokenService tokenService) {
        this.databaseService = databaseService;
        this.tokenService = tokenService;
    }

    //Method for starting a game. First is called dealCards method for dealing 2 and 2 cards for player and dealer.
    public void startGame() {
        dealCards();

        //Checking if player has black jack starting hand by calling checkIfPLayerHasBlackJackStartingHand method.
        if (checkIfPlayerHasBlackJackStartingHand()) {
            System.out.println(separateBlock);
            return;
        }
        System.out.println("What you want to do next? You have these options:" + "\n" + "1) Stand" + "\n" + "2) Hit" + "\n" + "3) Double down" + "\n" + "4) Surrender");

        //Then the player chooses which option he wants to play next.
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

        }
        if (option == 4) {
            surrender();
        }

        //After each game program needs to clear playerHand and dealersHand.
        //Then resetDeck method from Deck class is called for resetting all cards in deck.
        playersHand.clear();
        dealersHand.clear();
        deck.resetDeck();
    }

    //Method when player chooses stand option.
    public void stand() {
        System.out.println(separateBlock);

        //Calling showCardsInPlayerHand method for showing player's cards in console.
        showCardsInPlayerHand();

        //While dealer has hand value less than 17. By rules, he needs to draw cards until he has at least 17 hand value.
        while (dealersHandValue < 17) {
            Cards dealerCard = deck.dealCard();
            dealersHand.add(dealerCard);
            updateDealersHandValue();
            System.out.println("Dealer draws " +dealerCard);
        }
        //Method for showing dealers hand to console.
        showCardsInDealerHand();

        //Ifs for matching who will win.
        if (dealersHandValue > 21) {
            tokenService.winTokens(1);
        } else if (playersHandValue > 21) {
            tokenService.loseTokens(1);
        } else if (playersHandValue > dealersHandValue) {
            tokenService.winTokens(1);
        } else if (playersHandValue == dealersHandValue) {
            System.out.println("It's a TIE.");
        } else {
            tokenService.loseTokens(1);
        }
        System.out.println(separateBlock);
    }

    //Method when player chooses hit option.
    public void hit() {

        //Player gets one more card to playersHand
        Cards playerCard = deck.dealCard();
        playersHand.add(playerCard);

        //Calling updatePlayerHandValue method for getting value of all cards in player's hand.
        updatePlayersHandValue();
        System.out.println("You draw another card: " + playerCard);

        //Calling showCardsInPlayerHand method for showing player's cards in console.
        showCardsInPlayerHand();

        //Check if player's hand value is bigger than 21.
        if (playersHandValue > 21) {
            showCardsInDealerHand();
            tokenService.loseTokens(1);
            return;
        }
        System.out.println("Dealer's card in hand: " + "\n" + dealersHand.get(0) +".");
        System.out.println(separateBlock);

        //After hit player chooses between option 1 for more hit or option 2 for stand.
        System.out.println("Press number 1 for one more hit" + "\n" + "Press number 2 for stand" );
        int option = tokenService.getValidatedIntegerInput();
        while(option<1 || option > 2) {
            System.out.println("Choose a number 1 or 2 for option.");
            option = tokenService.getValidatedIntegerInput();
        }
        if (option == 1) {
            hit();
        }
        if ( option == 2) {
            stand();
        }
    }

    //Method when player chooses doubleDown option.
    public void doubleDown() {

        //First calling checkIfPlayerCanDoubleDown method from TokenService that checks if player can double down. (have 2x his bet.)
        tokenService.checkIfPlayerCanDoubleDown();
        System.out.println(separateBlock);

        //Calling showCardsInPlayerHand method for showing player's cards in console.
        showCardsInPlayerHand();

        //Player draws one more card.
        Cards playerCard = deck.dealCard();
        playersHand.add(playerCard);

        //Calling updatePlayerHandValue method for getting value of all cards in player's hand.
        updatePlayersHandValue();
        System.out.println("You draw another card: " + playerCard);

        //Calling showCardsInPlayerHand method for showing player's cards in console.
        showCardsInPlayerHand();

        //Ifs for checking who wins.
        if (playersHandValue > 21) {
            showCardsInDealerHand();
            tokenService.loseTokens(2);
            return;
        }
        while (dealersHandValue < 17) {
            Cards dealerCard = deck.dealCard();
            dealersHand.add(dealerCard);
            updateDealersHandValue();
            System.out.println("Dealer draws " +dealerCard);
        }
        showCardsInDealerHand();
        //Ifs for checking who wins.
        if (dealersHandValue > 21) {
            tokenService.winTokens(2);
        } else if (playersHandValue > 21) {
            tokenService.loseTokens(2);
        } else if (playersHandValue > dealersHandValue) {
            tokenService.winTokens(2);
        } else if (playersHandValue == dealersHandValue) {
            System.out.println("It's a TIE.");
        } else {
            tokenService.loseTokens(2);
        }
        System.out.println(separateBlock);
    }

    //Method when player chooses surrender option.
    public void surrender() {
        System.out.println(separateBlock);

        //Calling showCardsInPlayerHand method for showing player's cards in console.
        showCardsInPlayerHand();

        //Calling showCardsInDealerHand method for showing dealer's cards in console.
        showCardsInDealerHand();
        tokenService.surrenderLoseTokens();
        System.out.println(separateBlock);
    }

    //Method for dealing cards to player and dealer. Working with dealCard method from Deck class.
    public void dealCards() {
        playersHand.add(deck.dealCard());
        playersHand.add(deck.dealCard());
        dealersHand.add(deck.dealCard());
        //Add hidden card to hiddenDealerCard variable
        hiddenDealerCard = deck.dealCard();
        dealersHand.add(hiddenDealerCard);

        //Update hand values
        updatePlayersHandValue();
        updateDealersHandValue();

        //Calling showCardsInPlayerHand method for showing player's cards in console.
        showCardsInPlayerHand();
        System.out.println("One of the dealer's card in hand: " + "\n" + dealersHand.get(0) +".");
        System.out.println(separateBlock);
    }

    //Method for updating player's hand value. Getting all card values and plus them.
    public void updatePlayersHandValue() {
        playersHandValue = 0;
        for (Cards card : playersHand) {
            playersHandValue += card.getValue();

            //Implementation for card Ace. If players HandValue is > with normal Ace value, then ace is set to value 1.
            if (card.getRank().equals("A") && playersHandValue > 21) {
                card.setValue(1);
                playersHandValue -= 10;
            }
        }
    }

    //Method for updating dealer's hand value. Getting all card values and plus them.
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

    //Method for checking if player has blackjack in starting hand.
    public boolean checkIfPlayerHasBlackJackStartingHand() {
        if (playersHandValue == 21) {
            if(dealersHandValue == 21){
                System.out.println("both have BLACKJACK. no one wins.");
                playersHand.clear();
                dealersHand.clear();
                deck.resetDeck();
            }
            else {
                System.out.println("BLACKJACK. ");
                tokenService.blackjackWinTokens();
                playersHand.clear();
                dealersHand.clear();
                deck.resetDeck();
            }
            return true;
        }
        return false;
    }

    //Method for showing player's cards in console.
    public void showCardsInPlayerHand() {
        System.out.println("Your cards in hand: ");
        for (Cards card : playersHand) {
            System.out.println(card + ".");
        }
        System.out.println("Your total hand value is: " + playersHandValue);
        System.out.println(separateBlock);
    }

    //Method for showing dealer's cards in console
    public void showCardsInDealerHand() {
        System.out.println("Dealer cards in hand: ");
        for (Cards card : dealersHand) {
            System.out.println(card + ".");
        }
        System.out.println("Dealer's total hand value is: " + dealersHandValue);
        System.out.println(separateBlock);
    }

}






