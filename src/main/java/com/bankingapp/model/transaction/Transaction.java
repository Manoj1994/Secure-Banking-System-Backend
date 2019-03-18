package com.bankingapp.model.transaction;
import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id", nullable = false)
    private int transaction_id;

    @Column(name = "transaction_amount")
    private Double transaction_amount;

    @Column(name = "transaction_timestamp")
    private Timestamp transaction_timestamp;

    @Column(name = "transaction_type")
    private int transaction_type;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private int status;

    @Column(name = "account_no")
    private int account_no;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "request_id")
    private int request_id;

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public Double getTransaction_amount() {
        return transaction_amount;
    }

    public void setTransaction_amount(Double transaction_amount) {
        this.transaction_amount = transaction_amount;
    }

    public Timestamp getTransaction_timestamp() {
        return transaction_timestamp;
    }

    public void setTransaction_timestamp(Timestamp transaction_timestamp) {
        this.transaction_timestamp = transaction_timestamp;
    }

    public int getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(int transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAccount_no() {
        return account_no;
    }

    public void setAccount_no(int account_no) {
        this.account_no = account_no;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public int getRequest_id() {
        return request_id;
    }

    public void setRequest_id(int request_id) {
        this.request_id = request_id;
    }
}
