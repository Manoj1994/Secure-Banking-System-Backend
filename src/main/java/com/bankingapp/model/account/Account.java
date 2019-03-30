package com.bankingapp.model.account;

import javax.persistence.*;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_no", nullable = false)
    private int account_no;

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "routing_no")
    private int routing_no;

    @Column(name = "account_type")
    private int account_type;

    @Column(name = "interest")
    private Double interest;

    @Column(name = "created")
    private Timestamp created;

    @Column(name = "updated")
    private Timestamp updated;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return getAccount_no() == account.getAccount_no() &&
                getUser_id() == account.getUser_id() &&
                getRouting_no() == account.getRouting_no() &&
                getAccount_type() == account.getAccount_type() &&
                Objects.equals(getBalance(), account.getBalance()) &&
                Objects.equals(getInterest(), account.getInterest());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccount_no(), getUser_id(), getBalance(), getRouting_no(), getAccount_type(), getInterest(), getCreated(), getUpdated());
    }

    @Override
    public String toString() {
        return "Account{" +
                "account_no=" + account_no +
                ", user_id=" + user_id +
                ", balance=" + balance +
                ", routing_no=" + routing_no +
                ", account_type=" + account_type +
                ", interest=" + interest +
                ", created=" + created +
                ", updated=" + updated +
                '}';
    }

    public int getAccount_no()
    {
        return account_no;
    }

    public void setAccount_no(int account_no)
    {
        this.account_no = account_no;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public int getRouting_no() {
        return routing_no;
    }

    public void setRouting_no(int routing_no) {
        this.routing_no = routing_no;
    }

    public int getAccount_type() {
        return account_type;
    }

    public void setAccount_type(int account_type) {
        this.account_type = account_type;
    }

    public Double getInterest() {
        return interest;
    }

    public void setInterest(Double interest) {
        this.interest = interest;
    }

    public Timestamp getCreated() {
        return created;
    }

    public void setCreated(Timestamp created) {
        this.created = created;
    }

    public Timestamp getUpdated() {
        return updated;
    }

    public void setUpdated(Timestamp updated) {
        this.updated = updated;
    }
}
