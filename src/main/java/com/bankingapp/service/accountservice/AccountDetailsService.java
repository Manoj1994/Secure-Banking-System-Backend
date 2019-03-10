package com.bankingapp.service.accountservice;

import com.bankingapp.model.account.BankAccount;
import com.bankingapp.model.account.DebitAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Component
public class AccountDetailsService {

    @Autowired
    EntityManager entityManager;

    public DebitAccount getDebitAccount(int accountNumber) {
        String sql = "SELECT * FROM bank_accounts where account_number = " + accountNumber;

        Query query = entityManager.createQuery(sql, DebitAccount.class);
        query.setParameter("account_number", accountNumber);

        return (DebitAccount) query.getSingleResult();

    }
//    public String getEmailID(Integer userID)
//    {
//        String query = "SELECT email from external_users where id= '" + userID + "'";
//        String email = jdbcTemplate.queryForList(query, String.class).get(0);
//        return email;
//    }
}
