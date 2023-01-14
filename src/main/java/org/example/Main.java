package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DatabaseService databaseService = new DatabaseService();
        Game game = new Game(databaseService);
        databaseService.loadingUserService();
        String repeatText = "Choose a number for continue. " + "\n" + "1) Start a game" + "\n" + "2) Deposit tokens" + "\n" + "3) Withdraw tokens" + "\n" + "4) Account info" + "\n" + "5) Exit program";
        System.out.println(repeatText);

        int option;

        do {
            option = scanner.nextInt();

            if (option == 1) {
                game.playGame();

            }

            else if (option == 2) {
                game.depositTokens();
                System.out.println(repeatText);
            }

            else if (option == 3) {
                game.withdrawTokens();

            }

            else if (option == 4) {
                databaseService.getAccountInfo();

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