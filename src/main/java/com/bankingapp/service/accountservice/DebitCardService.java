package com.bankingapp.service.accountservice;

import com.bankingapp.model.account.DebitCard;
import com.bankingapp.model.login.User;
import com.bankingapp.repository.cardrepository.DebitCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.List;


@Component
public class DebitCardService {

    @Autowired
    EntityManager entityManager;

    @Autowired
    DebitCardRepository debitCardRepository;


    public Boolean addNewDebitCard(DebitCard debitCard) {

        try {
            debitCardRepository.save(debitCard);
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    public List<DebitCard> getDebitCards(int customerId) {

        String sql = "Select e from " + DebitCard.class.getName() + " e " //
                + " Where e.account_no = :account_no";

        Query query = entityManager.createQuery(sql, DebitCard.class);
        query.setParameter("account_no", customerId);

        return query.getResultList();
    }
}
