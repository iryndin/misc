package net.iryndin.rps;

import net.iryndin.rps.model.SingleGameRound;

import java.util.List;

/**
 * Provides access to GameHistory
 */
public interface GameHistoryProvider {
    List<SingleGameRound> provideGameHistory();
}
