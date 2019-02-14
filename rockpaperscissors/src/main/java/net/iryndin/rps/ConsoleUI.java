package net.iryndin.rps;

import net.iryndin.rps.model.Command;
import net.iryndin.rps.model.GameStats;
import net.iryndin.rps.model.Move;

import java.util.Scanner;

public class ConsoleUI implements UI {

    private Scanner scanner = new Scanner(System.in);

    @Override
    public void showGameBanner() {
        System.out.println("Rock, Paper, Scissors Game");
        System.out.println(" Available commands:");
        System.out.println("   'q' - quit the game");
        System.out.println("   'a' - show stats");
        System.out.println("   'g' - play new game");
        System.out.println("   'r' - rock");
        System.out.println("   'p' - paper");
        System.out.println("   's' - scissors");
    }

    @Override
    public Command getCommand() {
        while (true) {
            System.out.println("Enter command ('q' - quit, 'a' - show stats, 'r' - rock, 'p' - paper, 's' - scissors): ");
            String s = scanner.next().toLowerCase();
            switch (s) {
                case "q":
                    return Command.QUIT;
                case "a":
                    return Command.SHOW_STATS;
                case "r":
                    return Command.ROCK;
                case "p":
                    return Command.PAPER;
                case "s":
                    return Command.SCISSORS;
                default: {
                    System.out.println("Wrong command!");
                    break;
                }
            }
        }
    }

    @Override
    public void sayGoodBye() {
        System.out.println("Exiting the game, bye!");
    }

    @Override
    public void showStats(GameStats gameStats) {
        int numberOfGames = gameStats.getTotalGames();
        int wins = gameStats.getHumanWins();
        int losses = gameStats.getComputerWins();
        int ties = gameStats.getTies();
        double percentageWon = numberOfGames > 0 ? (wins + ((double) ties) / 2) / numberOfGames : 0;

        // Line
        System.out.print("+");
        printDashes(68);
        System.out.println("+");

        // Print titles
        System.out.printf("|  %6s  |  %6s  |  %6s  |  %12s  |  %14s  |\n",
                "WINS", "LOSSES", "TIES", "GAMES PLAYED", "PERCENTAGE WON");

        // Line
        System.out.print("|");
        printDashes(10);
        System.out.print("+");
        printDashes(10);
        System.out.print("+");
        printDashes(10);
        System.out.print("+");
        printDashes(16);
        System.out.print("+");
        printDashes(18);
        System.out.println("|");

        // Print values
        System.out.printf("|  %6d  |  %6d  |  %6d  |  %12d  |  %13.2f%%  |\n",
                wins, losses, ties, numberOfGames, percentageWon * 100);

        // Line
        System.out.print("+");
        printDashes(68);
        System.out.println("+");
    }

    private void printDashes(int numberOfDashes) {
        for (int i = 0; i < numberOfDashes; i++) {
            System.out.print("-");
        }
    }

    @Override
    public void reportError(Exception e) {
        System.out.println("Error happened. Exception message: " + e.getMessage());
    }

    @Override
    public void reportGameResultTie(Move humanMove, Move computerMove) {
        System.out.println(String.format("Your move: %s, computer move: %s. Result: TIE!", humanMove, computerMove));
    }

    @Override
    public void reportGameResultHumanWins(Move humanMove, Move computerMove) {
        System.out.println(String.format("Your move: %s, computer move: %s. Result: YOU WIN!", humanMove, computerMove));
    }

    @Override
    public void reportGameResultComputerWins(Move humanMove, Move computerMove) {
        System.out.println(String.format("Your move: %s, computer move: %s. Result: COMPUTER WIN!", humanMove, computerMove));
    }
}
