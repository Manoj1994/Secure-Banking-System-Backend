package com.bankingapp.model.request;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "transaction_request")
public class TransactionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id", updatable = false, nullable = false)
    private int request_id;

    @Column(name = "created_at")
    private Timestamp created_at;

    @Column(name = "status_id")
    private int status_id;

    @Column(name = "created_by")
    private int created_by;

    @Column(name = "approved_by")
    private int approved_by;

    @Column(name = "approved_at")
    private Timestamp approved_at;

    @Column(name = "from_account")
    private int from_account;

    @Column(name = "to_account")
    private int to_account;

    @Column(name = "critical")
    private boolean critical;

    @Column(name = "transaction_amount")
    private Double transaction_amount;

    public int getRequest_id() {
        return request_id;
    }

    public void setRequest_id(int request_id) {
        this.request_id = request_id;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    public int getApproved_by() {
        return approved_by;
    }

    public void setApproved_by(int approved_by) {
        this.approved_by = approved_by;
    }

    public Timestamp getApproved_at() {
        return approved_at;
    }

    public void setApproved_at(Timestamp approved_at) {
        this.approved_at = approved_at;
    }

    public int getFrom_account() {
        return from_account;
    }

    public void setFrom_account(int from_account) {
        this.from_account = from_account;
    }

    public int getTo_account() {
        return to_account;
    }

    public void setTo_account(int to_account) {
        this.to_account = to_account;
    }

    public Double getTransaction_amount() {
        return transaction_amount;
    }

    public void setTransaction_amount(Double transaction_amount) {
        this.transaction_amount = transaction_amount;
    }

    public boolean isCritical() {
        return critical;
    }

    public void setCritical(boolean critical) {
        this.critical = critical;
    }

    @Override
    public String toString() {
        return "TransactionRequest{" +
                "request_id=" + request_id +
                ", created_at=" + created_at +
                ", status_id=" + status_id +
                ", created_by=" + created_by +
                ", approved_by=" + approved_by +
                ", approved_at=" + approved_at +
                ", from_account=" + from_account +
                ", to_account=" + to_account +
                ", critical=" + critical +
                ", transaction_amount=" + transaction_amount +
                '}';
    }
}
