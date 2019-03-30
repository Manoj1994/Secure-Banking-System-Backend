package com.bankingapp.service.accountservice;

import com.bankingapp.model.account.Account;
import com.bankingapp.repository.accountrepository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class AccountDetailsService {

    @Autowired
    EntityManager entityManager;

    @Autowired
    AccountRepository accountRepository;

    public Account getDebitAccount(int accountNumber) {
        String sql = "SELECT * FROM bank_accounts where account_number = " + accountNumber;
        Query query = entityManager.createQuery(sql, Account.class);
        query.setParameter("account_number", accountNumber);
        return (Account) query.getSingleResult();
    }

    public Account getAccount(int accountNumber) {
        String sql = "SELECT a FROM "+Account.class.getName()+" a where a.account_no = :account_no";
        Query query = entityManager.createQuery(sql, Account.class);
        query.setParameter("account_no", accountNumber);
        return (Account) query.getSingleResult();
    }

    public List<Account> getAccounts() {
        String sql = "SELECT a FROM "+Account.class.getName()+" a";
        Query query = entityManager.createQuery(sql, Account.class);
        return query.getResultList();
    }

    public boolean save(Account account) {
        try {
            accountRepository.save(account);
            return true;
        } catch(Exception e) {

        }
        return false;
    }

    public boolean delete(int account_no) {
        try {

            Account account = getAccount(account_no);
            accountRepository.delete(account);
            return true;
        } catch(Exception e) {

        }
        return false;
    }
}
