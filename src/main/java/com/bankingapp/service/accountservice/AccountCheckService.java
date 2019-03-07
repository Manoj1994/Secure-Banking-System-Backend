package com.bankingapp.service.accountservice;

import com.bankingapp.model.login.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.persistence.EntityManager;
import javax.persistence.Query;

@Component
public class AccountCheckService {

    @Autowired
    EntityManager entityManager;


    public boolean checkAccountExists(int account_number) {

        try {

            String sql = "select b from bank_accounts b where b.account_number=:account_number";

            Query query = entityManager.createQuery(sql);
            query.setParameter("account_number", account_number);

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
