package com.bankingapp.service.customerservice;

import com.bankingapp.model.account.CheckingAccount;
import com.bankingapp.model.account.Customer;
import com.bankingapp.model.account.SavingsAccount;
import com.bankingapp.model.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Component
public class CustomerAccountService {

    @Autowired
    EntityManager entityManager;

    public List<Transaction> getTransactions(int accountNumber, int interval) {

        String sql = "SELECT t FROM "+ Transaction.class.getName() +" t WHERE (t.payer_id=:payer_id OR payee_id = :payer_id ) " +
                "AND (timestamp_updated between DATE_SUB(NOW(), INTERVAL "+ interval +" MONTH) AND NOW()) ORDER BY timestamp_updated DESC";

        Query query = entityManager.createQuery(sql, Transaction.class);
        query.setParameter("payer_id", accountNumber);

        return query.getResultList();
    }

    public SavingsAccount getSavingsAccount(String accountNumber) {
        String sql = "SELECT d FROM debit_bank_accounts where account_number = :account_number";

        Query query = entityManager.createQuery(sql, SavingsAccount.class);
        query.setParameter("account_number", accountNumber);
        return (SavingsAccount) query.getSingleResult();

    }

    public SavingsAccount getCheckingAccount(int accountNumber) {
        String sql = "SELECT d FROM checking_bank_accounts where account_number = :account_number";

        Query query = entityManager.createQuery(sql, CheckingAccount.class);
        query.setParameter("account_number", accountNumber);
        return (SavingsAccount) query.getSingleResult();

    }

    public String getEmailID(Integer userID)
    {
        String sql = "SELECT b from "+ Customer.class.getName() + " b where b.id= :id";
        Query query = entityManager.createQuery(sql, SavingsAccount.class);
        query.setParameter("id", userID);

        Customer customer = (Customer) query.getSingleResult();
        return customer.getEmail();
    }
}
