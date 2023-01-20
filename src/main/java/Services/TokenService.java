package Services;

import GameModels.Player;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

import static Services.DatabaseService.filePath;

/**
 * TokenService class stands for depositing tokens, withdrawing tokens, winning tokens, losing tokens, placing bets and updating player tokens.
 * It works closely with Game class and DatabaseService class.
 */
public class TokenService {
    private Scanner scanner = new Scanner(System.in);
    private DatabaseService database = new DatabaseService();
    private Scanner validateScanner = new Scanner(System.in);
    private String separateBlock = "-----------------------------------";
    private int bet;

    //Method for depositing tokens to playerAccount.
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

    //Method for withdrawing tokens from player account.
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

    //Method for placing bets. It is called in Main class when starting option 1) start a game.
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

    //Method while player wins a tokens. Int betMultiplier is used for multiplying win 1* is for normal lose/win. And 2* is for doubleDown option of play.
    public void winTokens(int betMultiplier) {
        Player player = database.playingPlayer.get(0);
        System.out.println("You WIN " + "\u001B[31m" + betMultiplier*bet + "\u001B[0m" + " tokens.");
        player.addTokens(betMultiplier*bet);
        updatePlayerTokenCount(player);
    }
    //Method while player loses a tokens. Int betMultiplier is used for multiplying win 1* is for normal lose/win. And 2* is for doubleDown option of play.
    public void loseTokens(int betMultiplier) {
        Player player = database.playingPlayer.get(0);
        System.out.println("You LOSE " + "\u001B[31m" + betMultiplier*bet + "\u001B[0m" + " tokens.");
        player.removeTokens(betMultiplier*bet);
        updatePlayerTokenCount(player);
    }
    //Method while player loses tokens. Player loses half of its bet by rules.
    public void surrenderLoseTokens() {
        Player player = database.playingPlayer.get(0);
        System.out.println("You LOSE " + "\u001B[31m" + bet / 2 + "\u001B[0m" + " tokens.");
        player.removeTokens(bet / 2);
        updatePlayerTokenCount(player);
    }

    //Method while player has starting hand value 21, its called "BLACKJACK".
    public void blackjackWinTokens() {
        Player player = database.playingPlayer.get(0);
        System.out.println("You WIN " + "\u001B[31m" + 1.5*bet + "\u001B[0m" + " tokens.");
        player.addTokens((int) (1.5*bet));
        updatePlayerTokenCount(player);
    }

    //Method for checking if player can double down. Player needs to have at least double of its bet.
    public boolean checkIfPlayerCanDoubleDown() {
        Player player = database.playingPlayer.get(0);
        if (player.getPlayerTokens() >= bet*2) {
            return true;
        } else {
            System.out.println("You don't have enough tokens to double down.");
            return false;
        }
    }

    //Method for updating player token count.
    public void updatePlayerTokenCount(Player player) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).startsWith(player.getFullName())) {
                    lines.set(i, player.getFullName() + ", " + player.getPlayerTokens());
                    break;
                }
            }
            // write the updated file
            Files.write(Paths.get(filePath), lines);
        } catch (IOException e) {
            System.out.println("Some error occurred. Please call support.");

        }
    }

    //Method for checking if scanned input is integer. If not, its returned and user needs to input number again.
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
    public TokenService(DatabaseService database) {
        this.database = database;
    }
}




