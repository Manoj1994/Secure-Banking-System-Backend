package com.bankingapp.service.customerservice;

import com.bankingapp.model.account.Account;
import com.bankingapp.model.account.Customer;
import com.bankingapp.model.transaction.Transaction;
import com.bankingapp.repository.accountrepository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Component
public class CustomerAccountService {

    @Autowired
    EntityManager entityManager;

    @Autowired
    AccountRepository accountRepository;

    public List<Transaction> getTransactions(int accountNumber, int interval) {

        String sql = "SELECT t FROM "+ Transaction.class.getName() +" t WHERE (t.account_no=:payer_id) " +
                "AND (transaction_timestamp between DATE_SUB(NOW(), INTERVAL "+ interval +" MONTH) AND NOW()) ORDER BY transaction_timestamp DESC";

        Query query = entityManager.createQuery(sql, Transaction.class);
        query.setParameter("payer_id", accountNumber);

        return query.getResultList();
    }

    public List<Account> getAccounts(int userId) {
        String sql = "SELECT d FROM "+ Account.class.getName() +" d where d.user_id = :user_id";

        Query query = entityManager.createQuery(sql, Account.class);
        query.setParameter("user_id", userId);

        System.out.println(query.getResultList());
        return query.getResultList();
    }

    public List<Account> getAccounts(int userId, int accountType) {
        String sql = "SELECT d FROM "+ Account.class.getName() +" d where d.user_id = :user_id and d.account_type = :account_type";

        Query query = entityManager.createQuery(sql, Account.class);
        query.setParameter("user_id", userId);
        query.setParameter("account_type", accountType);

        System.out.println(query.getResultList());
        return query.getResultList();
    }

    public List<Customer> getAllCustomers() {

        String sql = "SELECT d FROM "+ Customer.class.getName() +" d";
        Query query = entityManager.createQuery(sql, Customer.class);
        return query.getResultList();
    }

    public Account getAccount(int userId, int account_no, int accountType) {
        String sql = "SELECT d FROM "+ Account.class.getName() +" d where d.user_id = :user_id and d.account_type = :account_type and d.account_no = :account_no";

        Query query = entityManager.createQuery(sql, Account.class);
        query.setParameter("user_id", userId);
        query.setParameter("account_type", accountType);
        query.setParameter("account_no", account_no);

        System.out.println(query.getResultList());
        return (Account)query.getSingleResult();
    }

    public Account getCheckingAccount(int accountNumber) {
        String sql = "SELECT d FROM checking_bank_accounts where account_number = :account_number";

        Query query = entityManager.createQuery(sql, Account.class);
        query.setParameter("account_number", accountNumber);
        return (Account) query.getSingleResult();

    }

    public Account getAccount(int account_no) {
        String sql = "SELECT d FROM "+ Account.class.getName() +" d where d.account_no = :account_no";

        Query query = entityManager.createQuery(sql, Account.class);
        query.setParameter("account_no", account_no);
        return (Account)query.getSingleResult();
    }

    public String getEmailID(Integer userID)
    {
        String sql = "SELECT b from "+ Customer.class.getName() + " b where b.id= :id";
        Query query = entityManager.createQuery(sql, Customer.class);
        query.setParameter("id", userID);

        Customer customer = (Customer) query.getSingleResult();
        return customer.getEmail();
    }

    public boolean save(Account account) {

        boolean status = false;
        try {
            accountRepository.save(account);
            return true;
        } catch(Exception e) {

        }
        return status;
    }

    public boolean delete(int account_no) {

        boolean status = false;
        try {
            Account account = getAccount(account_no);
            accountRepository.delete(account);
            return true;
        } catch(Exception e) {

        }
        return status;
    }
}
