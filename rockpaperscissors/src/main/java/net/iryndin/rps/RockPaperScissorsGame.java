package net.iryndin.rps;

import net.iryndin.rps.model.Command;
import net.iryndin.rps.model.GameStats;
import net.iryndin.rps.model.Move;
import net.iryndin.rps.model.SingleGameRound;
import net.iryndin.rps.model.WhoWins;
import net.iryndin.rps.player.ComputerPlayer;
import net.iryndin.rps.player.Player;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class RockPaperScissorsGame implements GameHistoryProvider {

    private final UI ui = new ConsoleUI();
    private final GameStats gameStats = new GameStats();
    private List<SingleGameRound> gameHistory = new LinkedList<>();
    private final Player computerPlayer = new ComputerPlayer(this);
    private GameResultDecisionMaker gameResultDecisionMaker = new GameResultDecisionMaker();

    public void startGame() {

        ui.showGameBanner();

        while (true) {
            try {
                Command command = ui.getCommand();
                switch (command) {
                    case QUIT:
                        ui.sayGoodBye();
                        return;
                    case SHOW_STATS:
                        ui.showStats(gameStats);
                        break;
                    case ROCK:
                    case PAPER:
                    case SCISSORS:
                        playGame(command);
                        break;
                    default:
                        throw new IllegalStateException("Unknown command: " + command);
                }
            } catch (Exception e) {
                ui.reportError(e);
            }
        }
    }

    private void playGame(Command command) {
        Move humanMove = Move.createMove(command);
        Move computerMove = computerPlayer.getMove();
        WhoWins whoWins = gameResultDecisionMaker.makeDecision(humanMove, computerMove);
        SingleGameRound singleGameRound = new SingleGameRound(humanMove, computerMove, whoWins);
        gameHistory.add(singleGameRound);

        switch (whoWins) {
            case TIE:
                this.gameStats.incrementTies();
                ui.reportGameResultTie(humanMove, computerMove);
                break;
            case HUMAN_WINS:
                this.gameStats.incrementHumanWins();
                ui.reportGameResultHumanWins(humanMove, computerMove);
                break;
            case COMPUTER_WINS:
                this.gameStats.incrementComputerWins();
                ui.reportGameResultComputerWins(humanMove, computerMove);
                break;
        }
    }

    @Override
    public List<SingleGameRound> provideGameHistory() {
        return Collections.unmodifiableList(this.gameHistory);
    }
}
