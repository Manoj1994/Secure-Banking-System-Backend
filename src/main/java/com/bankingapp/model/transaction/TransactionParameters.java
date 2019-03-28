package com.bankingapp.model.transaction;

import org.springframework.stereotype.Component;

@Component
public class TransactionParameters {

    public int TRANSFER = 1;
    public int ISSUE_CASHIERS_CHECK = 2;
    public int DEPOSIT_MONEY = 3;
    public int WITHDRAW_MONEY = 4;
}
