package com.bankingapp.model.account;

public class DebitAccount extends BankAccount{

    private double currentBalance;

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }
}
