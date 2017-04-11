package net.iryndin.mtapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author iryndin
 * @since 11/04/17
 */
public class TransferRequest {
    private Long amount;
    private Integer type;
    private String description;

    public TransferRequest() {
    }

    public TransferRequest(Long amount, Integer type, String description) {
        this.amount = amount;
        this.type = type;
        this.description = description;
    }

    @JsonProperty
    public Long getAmount() {
        return amount;
    }

    @JsonProperty
    public Integer getType() {
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
