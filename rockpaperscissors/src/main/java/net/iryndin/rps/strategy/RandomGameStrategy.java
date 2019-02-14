package net.iryndin.rps.strategy;

import net.iryndin.rps.model.Move;

import java.util.Random;

public class RandomGameStrategy implements GameStrategy {

    private static final Move[] MOVES = Move.values();

    @Override
    public Move getMove() {
        int i = new Random().nextInt(3);
        return MOVES[i];
    }
}
