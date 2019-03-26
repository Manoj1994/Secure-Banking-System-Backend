package com.bankingapp.service.accountservice;

import com.bankingapp.model.account.CreditCard;
import com.bankingapp.model.login.User;
import com.bankingapp.repository.creditcardrepository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;


import javax.persistence.*;

@Component
@Transactional
public class CreditCardService {

    @Autowired
    EntityManager entityManager;

    @Autowired
    CreditCardRepository creditCardRepository;


    public Boolean AddNewCreditCard(CreditCard creditCard) {

        try {
            creditCardRepository.save(creditCard);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean deleteCreditCard(long card_no)
    {
        boolean status=true;
        try {
            // Update tuple
            String sql = "delete from credit_card where card_no=:card_no";
            Query query = entityManager.createQuery(sql, User.class);
            query.setParameter("card_no", card_no);
        } catch (NoResultException e) {
            status=false;
        } catch (Exception e) {
            e.printStackTrace();
            status=false;
        }
        return status;
    }
}




