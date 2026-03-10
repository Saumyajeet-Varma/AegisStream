package com.aegis.transaction.model;

import com.aegis.common.event.TransactionStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    private String id;
    private String userId;
    private BigDecimal amount;
    private String currency;
    private long timestamp;

    @Enumerated(EnumType.String)
    private TransactionStatus status;

    public Transaction() {}

    @PrePersist
    public void generateIdAndTimeStamp() {

        if(this.id == null) {
            this.id = UUID.randomUUID().toString();
        }

        if(this.timestamp == 0) {
            this.timestamp = Instant.now().toEpochMilli();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
}
