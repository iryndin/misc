package net.iryndin.rps.strategy;

import net.iryndin.rps.GameHistoryProvider;
import net.iryndin.rps.model.Move;
import net.iryndin.rps.model.SingleGameRound;

import java.util.List;
import java.util.ListIterator;
import java.util.Random;

/**
 * Strategy implemented according to this article:
 * <a href="https://www.wikihow.com/Win-at-Rock,-Paper,-Scissors">How to Win at Rock, Paper, Scissors</a>.
 *
 * This strategy tries to be smart analyzing game history, but at the same time keeps a fraction of Randomness.
 *
 * Basic decision points:
 * <ul>
 *     <li>Do not go ROCK first move</li>
 *     <li>Look for your opponent using the same move twice in a row.
 *     If your opponent plays the same move twice in a row, they’re not likely to use it a third time.
 *     So, you can assume they won’t throw that move.
 *     Put out a move that will give you either a win or stalemate, guaranteeing you won’t lose.</li>
 *     <li>Switch moves if you lose.
 *     If your opponent won a round, you have to predict whether they would use that move again,
 *     or if they would do a different one depending on their level on skill.
 *     Beginner- Probably the same move.
 *     Medium- They would most likely pull a rock.
 *     Expert- Most likely scissors, or whatever move you used last time.
 *     They want to surprise you so for example, if you did scissors and they beat you with rock,
 *     chances are they're doing scissors next so prepare to do rock.</li>
 * </ul>
 */
public class TriesToBeSmartGameStrategy implements GameStrategy {

    private final GameHistoryProvider gameHistoryProvider;
    private final GameStrategy randomStrategy = new RandomGameStrategy();

    public TriesToBeSmartGameStrategy(GameHistoryProvider gameHistoryProvider) {
        this.gameHistoryProvider = gameHistoryProvider;
    }

    @Override
    public Move getMove() {
        List<SingleGameRound> gameHistory = gameHistoryProvider.provideGameHistory();

        if (gameHistory.isEmpty()) {
            if (new Random().nextFloat() > 0.5) {
                // Do not make ROCK first move
                return new Random().nextBoolean() ? Move.PAPER : Move.SCISSORS;
            } else {
                return randomStrategy.getMove();
            }
        } else if (gameHistory.size() == 1) {
            return randomStrategy.getMove();
        } else {
            // game history has at least 2 moves
            ListIterator<SingleGameRound> li = gameHistory.listIterator(gameHistory.size());
            SingleGameRound lastMove = li.previous();
            SingleGameRound preLastMove = li.previous();

            // if human plays the same move twice in a row, they’re not likely to use it a third time.
            if (lastMove.getHumanMove() == preLastMove.getHumanMove()) {
                if (new Random().nextBoolean()) {
                    switch (lastMove.getHumanMove()) {
                        // we expect next move is not going to be ROCK, so let's play accordingly
                        // for PAPER not-loosing moves are PAPER or SCISSORS
                        // for SCISSORS not-loosing moves are ROCK or SCISSORS
                        // so go with SCISSORS to not lose
                        case ROCK:
                            return Move.SCISSORS;
                        // for ROCK: not loosing moves are: ROCK or PAPER
                        // for SCISSORS not-loosing moves are ROCK or SCISSORS
                        // so go with SCISSORS to not lose
                        case PAPER:
                            return Move.ROCK;
                        // for PAPER not-loosing moves are PAPER or SCISSORS
                        // for ROCK: not loosing moves are: ROCK or PAPER
                        // so go with PAPER to not lose
                        case SCISSORS:
                            return Move.PAPER;

                    }
                } else {
                    return randomStrategy.getMove();
                }
            }
            return randomStrategy.getMove();
        }
    }
}
