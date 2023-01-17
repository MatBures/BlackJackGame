package Services;

import GameModels.Player;

import java.io.*;
import java.util.Scanner;

import static Services.DatabaseService.filePath;

public class TokenService {
    private Scanner scanner = new Scanner(System.in);
    private DatabaseService database = new DatabaseService();
    private Scanner validateScanner = new Scanner(System.in);
    private String separateBlock = "-----------------------------------";
    private int bet;

    public void depositTokens() {
        System.out.println("How many tokens you want to deposit.");
        int depositNumber = getValidatedIntegerInput();
        Player player = database.playingPlayer.get(0);
        player.addTokens(depositNumber);

        try {
            File file = new File(filePath);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = "";
            String oldLine = "";
            while ((line = reader.readLine()) != null) {
                if (line.contains(player.getFirstName() + " " + player.getSurName())) {
                    oldLine += player.getFirstName() + " " + player.getSurName() + ", " + player.getPlayerTokens() + "\r\n";
                } else {
                    oldLine += line + "\r\n";
                }
            }
            reader.close();
            FileWriter writer = new FileWriter(filePath);
            writer.write(oldLine);
            writer.close();
            System.out.println("Your tokens were successfully added to your account.");
        }  catch (IOException e) {
            System.out.println("Couldn't add more tokens to your account. Call support");
        }
    }

    public void withdrawTokens() {
        System.out.println("How many tokens you want to withdraw.");
        int withdrawNumber = getValidatedIntegerInput();
        Player player = database.playingPlayer.get(0);
        if (withdrawNumber > player.getPlayerTokens()) {
            System.out.println("You don't have enough tokens to withdraw that amount.");
            return;
        }
        player.removeTokens(withdrawNumber);

        try {
            File file = new File(filePath);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = "";
            String oldLine = "";
            while ((line = reader.readLine()) != null) {
                if (line.contains(player.getFirstName() + " " + player.getSurName())) {
                    oldLine += player.getFirstName() + " " + player.getSurName() + ", " + player.getPlayerTokens() + "\r\n";
                } else {
                    oldLine += line + "\r\n";
                }
            }
            reader.close();
            FileWriter writer = new FileWriter(filePath);
            writer.write(oldLine);
            writer.close();
            System.out.println("Your tokens were successfully withdrew from your account.");
        } catch (IOException e) {
            System.out.println("Couldn't withdraw tokens from your account. Call support");
        }
    }

    public TokenService(DatabaseService database) {
        this.database = database;
    }

    public int getValidatedIntegerInput() {
        int inputtedInteger;
        if (validateScanner.hasNextInt()) {
            inputtedInteger = validateScanner.nextInt();
            return inputtedInteger;
        }
        validateScanner.next();
        System.out.println("Invalid input. You need to write a number.");
        return getValidatedIntegerInput();
    }
    public void placeABet() {
        System.out.println("Lets get started." + "\n" + "Place your first bet. (Minimum is \033[31m10\033[0m)");
        bet = getValidatedIntegerInput();
        Player player = database.playingPlayer.get(0);
        while (bet > player.getPlayerTokens() || bet < 10) {
            if (player.getPlayerTokens() < 10) {
                System.out.println("You don't have enough tokens for the minimum bet. Next time deposit tokens before trying to play. Bye!");
                System.exit(0);
            }
            if(bet > player.getPlayerTokens()){
                System.out.println("You don't have that many tokens to bet. Try again.");
            }

            if(bet < 10) {
                System.out.println("Your bet is below minimum. (Minimum is \033[31m10\033[0m)");
            }
            bet = getValidatedIntegerInput();
        }
        System.out.println("Your first bet is " + "\u001B[31m" + bet + "\u001B[0m" + "\n" + separateBlock);

    }
    public void winTokens() {
        Player player = database.playingPlayer.get(0);
        System.out.println("You WIN " + "\u001B[31m" + bet + "\u001B[0m" + " tokens.");
        player.addTokens(bet);
        database.updatePlayerTokenCount(player);
    }

    public void loseTokens() {
        Player player = database.playingPlayer.get(0);
        System.out.println("You LOSE " + "\u001B[31m" + bet + "\u001B[0m" + " tokens.");
        player.removeTokens(bet);
        database.updatePlayerTokenCount(player);
    }
    public void surrenderLoseTokens() {
        Player player = database.playingPlayer.get(0);
        System.out.println("You LOSE " + "\u001B[31m" + bet/2 + "\u001B[0m" + " tokens.");
        player.removeTokens(bet);
        database.updatePlayerTokenCount(player);

    }
    public void doubleDownWinTokens() {
        Player player = database.playingPlayer.get(0);
        System.out.println("You WIN " + "\u001B[31m" + 2*bet + "\u001B[0m" + " tokens.");
        player.addTokens(bet);
        database.updatePlayerTokenCount(player);
    }

    public void doubleDownLoseTokens() {
        Player player = database.playingPlayer.get(0);
        System.out.println("You LOSE " + "\u001B[31m" + 2*bet + "\u001B[0m" + " tokens.");
        player.removeTokens(bet);
        database.updatePlayerTokenCount(player);
    }
    public boolean checkIfPlayerCanDoubleDown() {
        Player player = database.playingPlayer.get(0);
        if (player.getPlayerTokens() >= bet*2) {
            return true;
        } else {
            System.out.println("You don't have enough tokens to double down.");
            return false;
        }
    }
}




