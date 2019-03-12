package com.bankingapp.service.accountservice;

import com.bankingapp.model.account.BankAccount;
import com.bankingapp.model.account.SavingsAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Component
public class AccountBalanceService {

    @Autowired
    EntityManager entityManager;

    public boolean validateDebitAmount(int accountNumber, Double amount) {

        String sql = "Select b from debit_bank_accounts b where b.account_number = :account_number";
        Query query = entityManager.createQuery(sql, BankAccount.class);
        query.setParameter("accounNumber", accountNumber);

        SavingsAccount bankAccount = (SavingsAccount) query.getSingleResult();

        if (bankAccount.getCurrentBalance() <= 0 || amount < bankAccount.getCurrentBalance()) {
            return false;
        }
        return true;
    }
}
