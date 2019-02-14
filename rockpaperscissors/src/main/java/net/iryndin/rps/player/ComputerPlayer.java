package net.iryndin.rps.player;

import net.iryndin.rps.GameHistoryProvider;
import net.iryndin.rps.model.Move;
import net.iryndin.rps.strategy.GameStrategy;
import net.iryndin.rps.strategy.TriesToBeSmartGameStrategy;

public class ComputerPlayer implements Player {

    private final GameStrategy strategy;

    public ComputerPlayer(GameHistoryProvider gameHistoryProvider) {
        this.strategy = new TriesToBeSmartGameStrategy(gameHistoryProvider);
    }

    @Override
    public Move getMove() {
        return strategy.getMove();
    }
}
