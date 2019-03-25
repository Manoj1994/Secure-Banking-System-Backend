package com.bankingapp.model.account;

import javax.persistence.*;
import java.sql.Timestamp;

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

}