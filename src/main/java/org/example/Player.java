package org.example;

public class Player {
    private String firstName;
    private String surName;
    private int playerTokens;

    public Player(String firstName, String surName, int playerTokens) {
        this.firstName = firstName;
        this.surName = surName;
        this.playerTokens = playerTokens;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurName() {
        return surName;
    }


    public int getPlayerTokens() {
        return playerTokens;
    }

    public String getFullName() {
        return firstName + " " + surName;
    }

    public void setPlayerTokens(int playerTokens) {
        this.playerTokens = playerTokens;
    }

    public String toString() {
        return firstName + ", " + surName + ", " + playerTokens;
    }
}

