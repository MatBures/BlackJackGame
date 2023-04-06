package Game;

import Services.InputValidatorService;
import Services.DatabaseService;
import Services.TokenService;

/**
 * Main method for blackjack game. With options:
 * 1) Start a game
 * 2) Deposit tokens
 * 3) Withdraw tokens
 * 4) Account info
 * 5) Blackjack rules
 * 6) Exit program
 */
public class Main {
    public static void main(String[] args) {
        DatabaseService databaseService = new DatabaseService();
        TokenService tokenService = new TokenService(databaseService);
        Game game = new Game(databaseService, tokenService);
        databaseService.userLoginOrSignup();
        InputValidatorService inputValidater = new InputValidatorService();

        //String repeatText holding text for choosing what options can player choose.
        String repeatText = "Choose a number for continue. " + "\n" + "1) Start a game" + "\n" + "2) Deposit tokens" + "\n" + "3) Withdraw tokens" + "\n" + "4) Account info" + "\n" + "5) Blackjack rules" + "\n" + "6) Exit program";
        System.out.println(repeatText);
        int option;

        //Do while loop with options.
        do {
            option = inputValidater.getValidatedIntegerInput();

            //1. Option for starting a game.
            if (option == 1) {
                tokenService.placeABet();
                game.startGame();
                System.out.println(repeatText);
            }

            //2. Option for depositing tokens.
            else if (option == 2) {
                tokenService.depositTokens();
                System.out.println(repeatText);
            }

            //3. Option for withdrawing tokens.
            else if (option == 3) {
                tokenService.withdrawTokens();
                System.out.println(repeatText);
            }

            //4. Option for getting account info.
            else if (option == 4) {
                databaseService.displayPlayerData();

                System.out.println(repeatText);
            }

            //5. Option for showing blackjack rules.
            else if (option == 5) {
                System.out.println("Here you can read the rules: https://www.blackjackapprenticeship.com/how-to-play-blackjack/#playerdecideshowtoplayhand");
                System.out.println(repeatText);
            }

            //6. Option for exit program
            else if (option == 6) {

            }

            //When player inputs unknown option.
            else {
                System.out.println("Unknown option, try again.");
                System.out.println(repeatText);
            }

        }while(option != 6);
        System.out.println("See you next time. I hope you won some games.");

    }
}