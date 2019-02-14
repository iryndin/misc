package net.iryndin.rps;

import net.iryndin.rps.model.Command;
import net.iryndin.rps.model.GameStats;
import net.iryndin.rps.model.Move;

public interface UI {
    void showGameBanner();
    Command getCommand();
    void sayGoodBye();
    void showStats(GameStats gameStats);
    void reportError(Exception e);

    void reportGameResultTie(Move humanMove, Move computerMove);
    void reportGameResultHumanWins(Move humanMove, Move computerMove);
    void reportGameResultComputerWins(Move humanMove, Move computerMove);
}
