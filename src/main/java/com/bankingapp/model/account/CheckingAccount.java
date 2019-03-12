package com.bankingapp.model.account;

import javax.persistence.*;

@Entity
@Table(name = "checking_account")
public class CheckingAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
}
