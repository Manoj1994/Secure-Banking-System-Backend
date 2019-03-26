package com.bankingapp.model.account;

import javax.persistence.*;

@Entity
@Table(name = "debit_card")
public class DebitCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "card_no")
    private long card_no;

    @Column(name = "account_no")
    private int account_no;

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
}
