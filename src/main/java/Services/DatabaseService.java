package Services;

import GameModels.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * DatabaseService class stands for connection on database server
 * creating and logging players. And everything about database work.
 */
public class DatabaseService {

    // List of player currently playing the game
    public List<Player> playingPlayer = new ArrayList<>();

    // Database connection details
    private String jdbcURL = "jdbc:postgresql://localhost:5432/DatabaseOfPlayers";
    private String username = "postgres";
    private String password = "5595";

    Scanner scanner = new Scanner(System.in);

    //Method for loading user. User has to input firstName and surName. Then it's added to playingPlayer list (needed for more working through application).
    //User can login or create an account for playing blackjack.
    public void userLoginOrSignup() {

        System.out.println("Welcome to Blackjack game.");
        System.out.println("First of all you need to login to an account or create one.");

        boolean validInput = false;

        // While loop until user provides valid input
        while (!validInput) {
            System.out.println("Enter 'login' or 'create' to continue.");
            String choice = scanner.nextLine().toLowerCase();
            switch (choice) {

                // Login case
                case "login":

                    // Get user's first name and surname
                    System.out.println("Please enter your first name.");
                    String firstName = scanner.nextLine();
                    System.out.println("Now enter your surname.");
                    String surName = scanner.nextLine();

                    // Check if player exists in the database
                    Player player = getPlayer(firstName, surName);
                    if (player != null) {
                        playingPlayer.add(player);

                        // If player exists, add it to the list of players and display token count
                        System.out.println("Welcome back " + player.getFullName() + ". You have \u001B[31m" + player.getPlayerTokens() + "\u001B[0m tokens in your bank. That's nice!");
                        validInput = true;

                    } else {
                        // If player doesn't exist, tell the user to try again or create a new account
                        System.out.println("Invalid first name or surname combination. Try again or create a new account.");
                    }
                    break;

                // Create case
                case "create":
                    boolean validName = false;
                    while (!validName) {

                        // Get user's first name and surname
                        System.out.println("Please enter your first name.");
                        firstName = scanner.nextLine();
                        System.out.println("Now enter your surname.");
                        surName = scanner.nextLine();

                        // Check if player with same name already exists
                        Player existingPlayer = getPlayer(firstName, surName);
                        if (existingPlayer != null) {
                            System.out.println("This user already exists.");
                            validName = true;
                            break;

                        }

                        // Add new player to the playingPlayer list and the database
                        Player newPlayer = new Player(firstName, surName, 100);
                        playingPlayer.add(newPlayer);
                        addPlayerToDatabase(newPlayer);
                        System.out.println("WELCOME, Good luck and have fun. You have \u001B[31m" + newPlayer.getPlayerTokens() + "\u001B[0m tokens in your bank.");
                        validName = true;
                        validInput = true;
                    }
                    break;
                default:
                    System.out.println("Invalid input. Try again.");
                    break;
            }
        }
    }
    public Player getPlayer(String firstName, String surName) {
        Player player = null;

        try {
            // Establish a connection to the PostgreSQL database
            Connection connection = DriverManager.getConnection(jdbcURL, username, password);

            // Prepare a SQL statement to select a player from the database by first name and surname
            String sql = "SELECT * FROM players WHERE first_name = ? AND last_name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, firstName);
            statement.setString(2, surName);

            // Execute the SQL statement and get the result set
            ResultSet result = statement.executeQuery();

            // If a player with the given name exists, create a new Player object and set its properties
            if (result.next()) {
                int tokens = result.getInt("tokens");
                player = new Player(firstName, surName, tokens);
            }

            // Close the database connection
            connection.close();

        } catch (SQLException e) {
            // If the connection fails, print an error message and stack trace
            System.out.println("Failed connecting to PostgreSQL server");
            e.printStackTrace();
        }

        // Return the Player object or null if no player was found
        return player;
    }

    public void addPlayerToDatabase(Player player) {
        try {
            // Establish a connection to the PostgreSQL database
            Connection connection = DriverManager.getConnection(jdbcURL, username, password);

            // Check if a player with the same first and last name already exists in the database
            String sql = "SELECT * FROM players WHERE first_name = ? AND last_name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, player.getFirstName());
            statement.setString(2, player.getSurName());
            ResultSet result = statement.executeQuery();

            // If a player already exists, return without adding the new player
            if (result.next()) {
                return;
            }

            // Otherwise, add the new player to the database
            sql = "INSERT INTO players (first_name, last_name, tokens) VALUES (?, ?, ?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, player.getFirstName());
            statement.setString(2, player.getSurName());
            statement.setInt(3, player.getPlayerTokens());
            statement.executeUpdate();

            // Close the database connection
            connection.close();

        } catch (SQLException e) {
            // If the connection fails, print an error message and stack trace
            System.out.println("Failed connecting to PostgreSQL server");
            e.printStackTrace();
        }
    }

    public synchronized void savePlayerData(Player player) {
        try {
            // Establish a connection to the PostgreSQL database
            Connection connection = DriverManager.getConnection(jdbcURL, username, password);

            // Prepare a SQL statement to update the player's token count in the database
            String sql = "UPDATE players SET tokens=" + player.getPlayerTokens() + " WHERE first_name='" + player.getFirstName() + "' AND last_name='" + player.getSurName() + "'";
            Statement statement = connection.createStatement();

            // Execute the SQL statement to update the player's token count
            statement.executeUpdate(sql);

            // Close the database connection
            connection.close();

        } catch (SQLException e) {
            // If the connection fails, print an error message and stack trace
            System.out.println("Failed connecting to PostgreSQL server");
            e.printStackTrace();
        }
    }

    public void displayPlayerData() {
        // Display playing player name and token count
        for (Player player : playingPlayer) {
            System.out.println(player.getFullName() + " you have \u001B[31m" + player.getPlayerTokens() + "\u001B[0m tokens in your bank.");
        }
    }
}


