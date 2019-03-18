package com.bankingapp.service.accountservice;

import com.bankingapp.model.account.Account;
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
        Query query = entityManager.createQuery(sql, Account.class);
        query.setParameter("accounNumber", accountNumber);

        Account bankAccount = (Account) query.getSingleResult();

        if (bankAccount.getBalance() <= 0 || amount < bankAccount.getBalance()) {
            return false;
        }
        return true;
    }

    public Double getBalance(int account_no) {

        String sql = "Select b.balance from " + Account.class.getName() +" b where b.account_no = :account_no";
        Query query = entityManager.createQuery(sql);
        query.setParameter("account_no", account_no);

        return (Double) query.getSingleResult();
    }
}
