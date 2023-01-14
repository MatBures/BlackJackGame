package org.example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    private Scanner scanner = new Scanner(System.in);
    private DatabaseService database;
    private Scanner validateScanner = new Scanner(System.in);

    public void depositTokens() {
        System.out.println("How many tokens you want to deposit.");
        int depositNumber = getValidatedIntegerInput();
        Player player = database.playingPlayer.get(0);
        player.addTokens(depositNumber);

        try {
            File file = new File("C:\\Users\\Jakub\\eclipse-workspace\\BlackJackProject\\src\\main\\resources\\Database_of_players.txt");
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
            FileWriter writer = new FileWriter("C:\\Users\\Jakub\\eclipse-workspace\\BlackJackProject\\src\\main\\resources\\Database_of_players.txt");
            writer.write(oldLine);
            writer.close();
            System.out.println("Your tokens were successfully added to your account.");
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't add more tokens to your account. Call support");
        } catch (IOException e) {
            System.out.println("Couldn't add more tokens to your account. Call support");
        }
    }

    public void withdrawTokens() {

    }

    public void playGame() {

    }

    public Game(DatabaseService database) {
        this.database = database;
    }
    private int getValidatedIntegerInput() {
        int inputtedInteger;
        if (validateScanner.hasNextInt()) {
            inputtedInteger = validateScanner.nextInt();
            return inputtedInteger;
        }

        validateScanner.next();
        System.out.println("Invalid input. You need to write a number.");
        return getValidatedIntegerInput();
    }
}





