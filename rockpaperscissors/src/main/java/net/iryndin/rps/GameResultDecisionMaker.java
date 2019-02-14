package net.iryndin.rps;

import net.iryndin.rps.model.Move;
import net.iryndin.rps.model.WhoWins;

public class GameResultDecisionMaker {

    public WhoWins makeDecision(Move humanMove, Move computerMove) {
        if (humanMove == computerMove) {
            return WhoWins.TIE;
        } else if (firstMoveBeatsSecondMove(humanMove, computerMove)) {
            return WhoWins.HUMAN_WINS;
        } else {
            return WhoWins.COMPUTER_WINS;
        }
    }

    private boolean firstMoveBeatsSecondMove(Move move1, Move move2) {
        switch (move1) {
            case ROCK: return move2 == Move.SCISSORS;
            case PAPER: return move2 == Move.ROCK;
            case SCISSORS: return move2 == Move.PAPER;
            default:
                throw new IllegalStateException("Unknown move: " + move1);
        }
    }
}
