package com.bankingapp.model.account;

import javax.persistence.*;
import javax.persistence.Id;

@Entity
@Table(name = "credit_card")
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_no")
    private long card_no;

    @Column(name = "account_no", nullable = false)
    private int account_no;

    @Column(name = "card_limit")
    private Double card_limit;

    @Column(name = "statement_balance")
    private Double statement_balance;

    @Override
    public String  toString() {
        return "CreditCard{" +
                "card_no=" + card_no +
                ", account_no=" + account_no +
                ", card_limit=" + card_limit +
                ", statement_balance=" + statement_balance +
                '}';
    }

    public long getCard_no() {
        return card_no;
    }

    public void setCard_no(long card_no) {
        this.card_no = card_no;
    }

    public int getAccount_no() {
        return account_no;
    }

    public void setAccount_no(int account_no) {
        this.account_no = account_no;
    }

    public Double getCard_limit() {
        return card_limit;
    }

    public void setCard_limit(Double card_limit) {
        this.card_limit = card_limit;
    }

    public Double getStatement_balance() {
        return statement_balance;
    }
    public void setStatement_balance(Double statement_balance) {
        this.statement_balance = statement_balance;
    }
}