package net.iryndin.adexchange.model;

public class ImmutablePair<A, B> {
    private final A first;
    private final B second;

    public ImmutablePair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }
}
