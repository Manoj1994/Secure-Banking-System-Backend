package com.bankingapp.service.accountservice;

import com.bankingapp.model.account.Account;
import com.bankingapp.model.login.User;
import com.bankingapp.repository.accountrepository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.SQLException;

@Component
public class AccountUpdateService {

    @Autowired
    EntityManager entityManager;

    @Autowired
    AccountRepository accountRepository;

    public boolean addMoney(int accoutNumber,Double amount) throws SQLException
    {
        boolean status=false;
        java.sql.Timestamp createdDateTime = new java.sql.Timestamp(new java.util.Date().getTime());

        try{

            String sql = "Select e from " + User.class.getName() + " e " //
                    + " Where e.userName = :userName " +" and e.password = :password";

            Query query = entityManager.createQuery(sql, User.class);
            //query.setParameter("userName", username);
            //query.setParameter("password", encryptedPassword);
            status =true;

        }catch(Exception e){

        }
        return status;
    }

    public boolean withdrawMoney(int accoutNumber, Double amount,String accountType) throws SQLException
    {
        boolean status=false;
        java.sql.Timestamp createdDateTime = new java.sql.Timestamp(new java.util.Date().getTime());
        //String query_addBalance ="update bank_accounts set hold = hold + "+amount+" WHERE external_users_id="+user.getId()+" and account_type='"+accountType+"'";
        String query_AddToPending = "INSERT INTO transaction_request (payer_id, payee_id,amount, hashvalue,transaction_type,description,status,approver,critical,timestamp_created,timestamp_updated) values (?,?,?,?,?,?,?,?,?,?,?)";
        try
        {


            status =true;
        }catch(Exception e){

        }
        return status;
    }

    public boolean updateMoney(int accountNumber, Double amount) {

        try {

            String sql = "update " + Account.class.getName() + " a set a.balance = :balance where a.account_no = :account_no";
            Query query = entityManager.createQuery(sql);
            query.setParameter("account_no", accountNumber);
            query.setParameter("balance", amount);
            return true;

        } catch(Exception e) {

        }
        return false;
    }

    public boolean updateBalance(int account_no, Double amount) {

        try {
            Account account = accountRepository.findById(account_no);
            account.setBalance(amount);
            accountRepository.save(account);
            return true;
        } catch(Exception e) {

        }
        return false;
    }


}