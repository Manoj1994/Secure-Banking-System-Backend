package com.bankingapp.service.accountservice;

import com.bankingapp.model.account.Account;
import com.bankingapp.model.login.User;
import com.bankingapp.repository.accountrepository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.sql.SQLException;

@Component
@Transactional
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
            String sql = "Select b from " +Account.class.getName()+" b where b.account_no = :account_no";
            Query query = entityManager.createQuery(sql, Account.class);
            query.setParameter("account_no", account_no);

            Account bankAccount = (Account) query.getSingleResult();
            bankAccount.setBalance(bankAccount.getBalance() + amount);
            accountRepository.save(bankAccount);
            return true;
        } catch(Exception e) {

        }
        return false;
    }

    /*TODO: create add account and delete account (set interest rate)*/
    public boolean createAccount(int cus_id,int account_type)
    {
        //insert into account(user_id,balance,account_type,interest,created,updated) values(?,?,?,?,current_timestamp(),null)
        try {
            String sql = "insert into account(user_id,balance,account_type,interest,updated) values("+cus_id+",0,"+account_type+",5.00,"+",null)";
            Query query = entityManager.createNativeQuery(sql, Account.class);
            query.executeUpdate();

            return true;
        } catch(Exception e) {

        }
        return false;
    }
    public boolean deleteAccount(int account_no) {
        try {
            String sql = "DELETE from " + Account.class.getName() + " where account_no="+account_no;
            Query query = entityManager.createNativeQuery(sql, Account.class);
            query.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}