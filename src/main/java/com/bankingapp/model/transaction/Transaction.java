package com.bankingapp.model.transaction;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;
    private int payer_id;
    private int payee_id;
    private Double amount;
    private String transaction_type;
    private String description;
    private String status;
    private String approver;
    private boolean critical;
    private Timestamp timestamp_created;
    private Timestamp timestamp_updated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPayer_id() {
        return payer_id;
    }

    public void setPayer_id(int payer_id) {
        this.payer_id = payer_id;
    }

    public int getPayee_id() {
        return payee_id;
    }

    public void setPayee_id(int payee_id) {
        this.payee_id = payee_id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public boolean isCritical() {
        return critical;
    }

    public void setCritical(boolean critical) {
        this.critical = critical;
    }

    public Timestamp getTimestamp_created() {
        return timestamp_created;
    }

    public void setTimestamp_created(Timestamp timestamp_created) {
        this.timestamp_created = timestamp_created;
    }

    public Timestamp getTimestamp_updated() {
        return timestamp_updated;
    }

    public void setTimestamp_updated(Timestamp timestamp_updated) {
        this.timestamp_updated = timestamp_updated;
    }
}
