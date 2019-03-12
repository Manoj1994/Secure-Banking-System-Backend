package com.bankingapp.model.account;

import javax.persistence.*;

@Entity
@Table(name = "savings_account")
public class SavingsAccount extends BankAccount{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private double currentBalance;

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }
}
