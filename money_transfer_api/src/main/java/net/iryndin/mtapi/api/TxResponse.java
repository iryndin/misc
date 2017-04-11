package net.iryndin.mtapi.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * @author iryndin
 * @since 11/04/17
 */
public class TxResponse {
    private Long id;
    private Long creditAccountId;
    private Long debitAccountId;
    private Long amount;
    private Date createDate;
    private String description;
    private Integer type;

    public TxResponse() {
    }

    public TxResponse(Long id, Long creditAccountId, Long debitAccountId, Long amount,
                      Date createDate, String description, Integer type) {
        this.id = id;
        this.creditAccountId = creditAccountId;
        this.debitAccountId = debitAccountId;
        this.amount = amount;
        this.createDate = createDate;
        this.description = description;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    @JsonProperty("account_credit_id")
    public Long getCreditAccountId() {
        return creditAccountId;
    }

    @JsonProperty("account_debit_id")
    public Long getDebitAccountId() {
        return debitAccountId;
    }

    public Long getAmount() {
        return amount;
    }

    @JsonProperty("create_date")
    public Date getCreateDate() {
        return createDate;
    }

    public String getDescription() {
        return description;
    }

    public Integer getType() {
        return type;
    }
}
