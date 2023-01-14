package org.example;


public class Main {
    public static void main(String[] args) {
        DatabaseService databaseService = new DatabaseService();
        TokenService tokenService = new TokenService(databaseService);
        Game game = new Game();
        databaseService.loadingUserService();
        String repeatText = "Choose a number for continue. " + "\n" + "1) Start a game" + "\n" + "2) Deposit tokens" + "\n" + "3) Withdraw tokens" + "\n" + "4) Account info" + "\n" + "5) Exit program";
        System.out.println(repeatText);

        int option;

        do {
            option = tokenService.getValidatedIntegerInput();

            if (option == 1) {
                game.startGame();

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

            }
            else {
                System.out.println("Unknown option, try again.");
                System.out.println(repeatText);
            }

        }while(option != 5);
        System.out.println("See you next time. I hope you won some games.");

    }
}