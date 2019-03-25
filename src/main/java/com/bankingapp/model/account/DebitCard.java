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

    @Column(name = "user_id")
    private int user_id;
}
