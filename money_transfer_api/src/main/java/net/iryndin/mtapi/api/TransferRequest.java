package net.iryndin.mtapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author iryndin
 * @since 11/04/17
 */
public class TransferRequest {
    private long amount;
    private int type;
    private String description;

    public TransferRequest() {
    }

    public TransferRequest(long amount, int type, String description) {
        this.amount = amount;
        this.type = type;
        this.description = description;
    }

    @JsonProperty
    public long getAmount() {
        return amount;
    }

    @JsonProperty
    public int getType() {
        return type;
    }

    @JsonProperty
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "TransferRequest{" +
                "amount=" + amount +
                ", type=" + type +
                ", description='" + description + '\'' +
                '}';
    }
}
