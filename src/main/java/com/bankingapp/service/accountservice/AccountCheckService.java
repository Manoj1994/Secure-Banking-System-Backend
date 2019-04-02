package com.bankingapp.service.accountservice;

import com.bankingapp.model.account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

@Component
@Transactional
public class AccountCheckService {

    @Autowired
    EntityManager entityManager;


    public boolean checkAccountExists(int customer_id, int account_no) {

        try {
            String sql = "select b from " + Account.class.getName() + " b where b.account_no=:account_no";
            Query query = entityManager.createQuery(sql, Account.class);
            query.setParameter("account_no", account_no);
            //query.setParameter("user_id", customer_id);

            if (query.getResultList().size() > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {

        }
        return false;
    }

//    public boolean checkAccountExists(int account_no) {
//
//        try {
//
//            String sql = "select b from " + Account.class.getName() + " b where b.account_no = :account_no";
//            Query query = entityManager.createQuery(sql, Account.class);
//            query.setParameter("account_no", account_no);
//
//            if (query.getResultList().size() > 0) {
//                return true;
//            } else {
//                return false;
//            }
//
//        } catch (Exception e) {
//
//        }
//        return false;
//    }

    public boolean checkAccountExistsWithRoutingNo(int account_no, int routing_no) {

        try {
            String sql = "select b from " + Account.class.getName() + " b where b.account_no = :account_no and b.routing_no = :routing_no";
            Query query = entityManager.createQuery(sql, Account.class);
            query.setParameter("account_no", account_no);
            query.setParameter("routing_no", routing_no);

            if (query.getResultList().size() > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public boolean checkAccountExists(String email) {

        try {

            String sql = "select b from " + Account.class.getName() + " b where b.email =: email";

            Query query = entityManager.createQuery(sql, Account.class);
            query.setParameter("email", email);

            if (query.getResultList().size() > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {

        }
        return false;
    }
}