package com.bankingapp.service.accountservice;

import com.bankingapp.model.account.DebitCard;
import com.bankingapp.model.login.User;
import com.bankingapp.repository.cardrepository.DebitCardRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "debit_card")
public class DebitCardService {

    @Autowired
    EntityManager entityManager;

    @Autowired
    DebitCardRepository debitCardRepository;


    public Boolean AddNewDebitCard(DebitCard debitCard) {

        try {
            debitCardRepository.save(debitCard);
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    public List<DebitCard> getDebitCards(int customerId) {

        String sql = "Select e from " + User.class.getName() + " e " //
                + " Where e.user_id = :user_id";

        Query query = entityManager.createQuery(sql, DebitCard.class);
        query.setParameter("user_id", customerId);

        return query.getResultList();
    }
}
