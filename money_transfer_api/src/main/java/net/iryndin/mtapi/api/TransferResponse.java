package net.iryndin.mtapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author iryndin
 * @since 11/04/17
 */
public class TransferResponse {
    private long txId;

    public TransferResponse() {
    }

    public TransferResponse(long txId) {
        this.txId = txId;
    }

    @JsonProperty
    public long getTxId() {
        return txId;
    }
}
