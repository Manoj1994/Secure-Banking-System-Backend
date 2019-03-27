package com.bankingapp.service.accountservice;

import com.bankingapp.model.account.CreditCard;
import com.bankingapp.repository.cardrepository.CreditCardRepository;
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


    public Boolean addNewDebitCard(CreditCard creditCard) {

        try {
            creditCardRepository.save(creditCard);
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    public List<CreditCard> getCreditCards(int account_no) {

        String sql = "Select e from " + CreditCard.class.getName() + " e " //
                + " Where e.account_no = :account_no";

        Query query = entityManager.createQuery(sql, CreditCard.class);
        query.setParameter("account_no", account_no);

        return query.getResultList();
    }
}
