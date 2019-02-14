package net.iryndin.rps.model;

public enum Move {
    ROCK, PAPER, SCISSORS;

    public static Move createMove(Command command) {
        switch (command) {
            case ROCK: return Move.ROCK;
            case PAPER: return Move.PAPER;
            case SCISSORS: return Move.SCISSORS;
        }
        throw new IllegalArgumentException("Cannot create Move from this command: " + command);
    }
}
