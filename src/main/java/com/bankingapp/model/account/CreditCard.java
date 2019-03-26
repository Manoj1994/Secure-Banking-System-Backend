package com.bankingapp.model.account;

import javax.persistence.*;

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
    private int card_limit;

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

    public int getCard_limit() {
        return card_limit;
    }

    public void setCard_limit(int card_limit) {
        this.card_limit = card_limit;
    }
}
