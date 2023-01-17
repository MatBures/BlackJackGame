package Game;


import Services.DatabaseService;
import Services.TokenService;

public class Main {
    public static void main(String[] args) {
        DatabaseService databaseService = new DatabaseService();
        TokenService tokenService = new TokenService(databaseService);
        Game game = new Game(databaseService, tokenService);
        databaseService.loadingUserService();
        String repeatText = "Choose a number for continue. " + "\n" + "1) Start a game" + "\n" + "2) Deposit tokens" + "\n" + "3) Withdraw tokens" + "\n" + "4) Account info" + "\n" + "5) Blackjack rules" + "\n" + "6) Exit program";
        System.out.println(repeatText);

        int option;

        do {
            option = tokenService.getValidatedIntegerInput();

            if (option == 1) {
                tokenService.placeABet();
                game.startGame();
                System.out.println(repeatText);

            }

            else if (option == 2) {
                tokenService.depositTokens();
                System.out.println(repeatText);
            }

            else if (option == 3) {
                tokenService.withdrawTokens();
                System.out.println(repeatText);

            }

            else if (option == 4) {
                databaseService.getAccountInfo();
                System.out.println(repeatText);

            }
            else if (option == 5) {
                System.out.println("Here you can read the rules: https://www.blackjackapprenticeship.com/how-to-play-blackjack/#playerdecideshowtoplayhand");
                System.out.println(repeatText);
            }
            else if (option == 6) {

            }
            else {
                System.out.println("Unknown option, try again.");
                System.out.println(repeatText);
            }

        }while(option != 6);
        System.out.println("See you next time. I hope you won some games.");

    }
}