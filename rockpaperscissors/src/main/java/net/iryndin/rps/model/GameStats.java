package net.iryndin.rps.model;

public class GameStats {
    private int totalGames;
    private int ties;
    private int humanWins;

    public void incrementTies() {
        this.ties++;
        this.totalGames++;
    }

    public void incrementHumanWins() {
        this.humanWins++;
        this.totalGames++;
    }

    public void incrementComputerWins() {
        this.totalGames++;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public int getTies() {
        return ties;
    }

    public int getHumanWins() {
        return humanWins;
    }

    public int getComputerWins() {
        return totalGames - ties - humanWins;
    }
}
