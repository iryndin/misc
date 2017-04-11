package net.iryndin.mtapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author iryndin
 * @since 11/04/17
 */
public class BalanceResponse {
    private long balance;

    public BalanceResponse() {
    }

    public BalanceResponse(long balance) {
        this.balance = balance;
    }

    @JsonProperty
    public long getBalance() {
        return balance;
    }
}
