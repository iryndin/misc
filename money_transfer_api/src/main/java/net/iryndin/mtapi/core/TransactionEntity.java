package net.iryndin.mtapi.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author iryndin
 * @since 11/04/17
 */
@Entity
@Table(name = "tx")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private long amount;

    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private int type;

    @OneToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="credit_account_id")
    private AccountEntity creditAccount;

    @OneToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="debit_account_id")
    private AccountEntity debitAccount;

    public TransactionEntity() {
    }

    public TransactionEntity(Long id, long amount, Date createDate, String description, int type,
                             AccountEntity creditAccount, AccountEntity debitAccount) {
        this.id = id;
        this.amount = amount;
        this.createDate = createDate;
        this.description = description;
        this.type = type;
        this.creditAccount = creditAccount;
        this.debitAccount = debitAccount;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public AccountEntity getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(AccountEntity creditAccount) {
        this.creditAccount = creditAccount;
    }

    public AccountEntity getDebitAccount() {
        return debitAccount;
    }

    public void setDebitAccount(AccountEntity debitAccount) {
        this.debitAccount = debitAccount;
    }
}
