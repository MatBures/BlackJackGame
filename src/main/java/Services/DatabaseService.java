package Services;

import GameModels.Player;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * DatabaseService class stands for loading user and getting user info.
 */
public class DatabaseService {
    public List<Player> playingPlayer = new ArrayList<>();
    public static String filePath = "src/main/resources/Database_of_players.txt";

    Scanner scanner = new Scanner(System.in);

    //Method for loading user. User has to input firstName and surName. Then it's added to playingPlayer List (needed for more working through game).
    //If user exists, the token count is loaded and the player is not added to Database_of_players.txt. If the player doesn't exist, then its written to Database_of_players.txt file.
    public void loadingUserService() {
        System.out.println("Welcome to Blackjack game.");
        System.out.println("Please enter your first name.");
        String firstName = scanner.nextLine();
        System.out.println("Now enter your surname.");
        String surName = scanner.nextLine();
        Player player = new Player(firstName, surName, 100);
        playingPlayer.add(player);

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains(player.getFullName())) {
                    found = true;
                    String[] tokens = line.split(", ");
                    player.setPlayerTokens(Integer.parseInt(tokens[1]));
                    break;
                }
            }
            reader.close();
            if (!found) {
                FileWriter writer = new FileWriter(filePath, true);
                writer.write(player.getFullName() + ", " + player.getPlayerTokens() + "\n");
                playingPlayer.add(player);
                writer.close();
                System.out.println("You was successfully added to the game. Good luck and have fun. You have \u001B[31m" + player.getPlayerTokens() + "\u001B[0m tokens in your bank.");
            } else {
                System.out.println("Welcome back "+ player.getFullName() + ". You have \u001B[31m" + player.getPlayerTokens() + "\u001B[0m tokens in your bank. That's nice!");
            }
        } catch (FileNotFoundException e) {
            System.out.println("File was not found.");
        } catch (IOException e) {
            System.out.println("Your name couldn't be saved.");

        }
    }

    //Method for showing account info of playing player.
    public void getAccountInfo () {
        System.out.println("You have \u001B[31m" + playingPlayer.get(0).getPlayerTokens() + "\u001B[0m tokens in your bank.");
    }

}


