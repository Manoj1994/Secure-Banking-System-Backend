package com.bankingapp.service.accountservice;

import com.bankingapp.model.account.Account;
import com.bankingapp.model.login.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Component
public class AccountCheckService {

    @Autowired
    EntityManager entityManager;


    public boolean checkAccountExists(int customer_id, int account_no) {

        try {

            String sql = "select b from " + Account.class.getName() + " b where b.account_no=:account_no and b.user_id = :user_id";

            Query query = entityManager.createQuery(sql);
            query.setParameter("account_no", account_no);
            query.setParameter("user_id", customer_id);

            if (query.getResultList().size() > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {

        }
        return false;

    }

    public boolean checkAccountExists(int account_no) {

        try {

            String sql = "select b from " + Account.class.getName() + " b where b.account_no=:account_no";

            Query query = entityManager.createQuery(sql);
            query.setParameter("account_no", account_no);

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
