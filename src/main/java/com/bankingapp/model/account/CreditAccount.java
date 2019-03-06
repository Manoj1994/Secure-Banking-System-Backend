package com.bankingapp.model.account;

import java.sql.Date;

public class CreditAccount extends BankAccount {

    private int Id;
    private int interset;
    private Long creditCardNumber;
    private double availBalance;
    private double lastBillAmount;
    private Date dueDateTimestamp;
    private float apr;
    private Date cycleDate;
    private int currentDueAmount;
    private int creditLimit;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getInterset() {
        return interset;
    }

    public void setInterset(int interset) {
        this.interset = interset;
    }

    public Long getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(Long creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public double getAvailBalance() {
        return availBalance;
    }

    public void setAvailBalance(double availBalance) {
        this.availBalance = availBalance;
    }

    public double getLastBillAmount() {
        return lastBillAmount;
    }

    public void setLastBillAmount(double lastBillAmount) {
        this.lastBillAmount = lastBillAmount;
    }

    public Date getDueDateTimestamp() {
        return dueDateTimestamp;
    }

    public void setDueDateTimestamp(Date dueDateTimestamp) {
        this.dueDateTimestamp = dueDateTimestamp;
    }

    public float getApr() {
        return apr;
    }

    public void setApr(float apr) {
        this.apr = apr;
    }

    public Date getCycleDate() {
        return cycleDate;
    }

    public void setCycleDate(Date cycleDate) {
        this.cycleDate = cycleDate;
    }

    public int getCurrentDueAmount() {
        return currentDueAmount;
    }

    public void setCurrentDueAmount(int currentDueAmount) {
        this.currentDueAmount = currentDueAmount;
    }

    public int getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(int creditLimit) {
        this.creditLimit = creditLimit;
    }
}
