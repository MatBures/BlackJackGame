package org.example;

import java.io.*;
import java.util.Scanner;

import static org.example.DatabaseService.filePath;

public class TokenService {
    private Scanner scanner = new Scanner(System.in);
    private DatabaseService database;
    private Scanner validateScanner = new Scanner(System.in);

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
}

