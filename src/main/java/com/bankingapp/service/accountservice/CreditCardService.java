package com.bankingapp.service.accountservice;

import com.bankingapp.model.account.CreditCard;
import com.bankingapp.model.account.DebitCard;
import com.bankingapp.model.login.User;
import com.bankingapp.repository.cardrepository.CreditCardRepository;
import com.bankingapp.repository.cardrepository.DebitCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Component
public class CreditCardService {

    @Autowired
    EntityManager entityManager;

    @Autowired
    CreditCardRepository creditCardRepository;


    public Boolean AddNewDebitCard(CreditCard creditCard) {

        try {
            creditCardRepository.save(creditCard);
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    public List<DebitCard> getCreditCards(int customerId) {

        String sql = "Select e from " + CreditCard.class.getName() + " e " //
                + " Where e.user_id = :user_id";

        Query query = entityManager.createQuery(sql, CreditCard.class);
        query.setParameter("user_id", customerId);

        return query.getResultList();
    }
}
