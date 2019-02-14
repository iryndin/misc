package net.iryndin.rps.model;

public class SingleGameRound {
    private final Move humanMove;
    private final Move computerMove;
    private final WhoWins whoWins;

    public SingleGameRound(Move humanMove, Move computerMove, WhoWins whoWins) {
        this.humanMove = humanMove;
        this.computerMove = computerMove;
        this.whoWins = whoWins;
    }

    public Move getHumanMove() {
        return humanMove;
    }

    public Move getComputerMove() {
        return computerMove;
    }

    public WhoWins getWhoWins() {
        return whoWins;
    }
}
