package com.bankingapp.service.accountservice;

import com.bankingapp.model.account.DebitCard;
import com.bankingapp.repository.debitcardrepository.DebitCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import javax.persistence.*;

@Component
@Transactional
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

    public Boolean deleteDebitCard(long card_no)
    {
        boolean status=true;
        try {
            // Update tuple
            debitCardRepository.deleteById(card_no);
        } catch (NoResultException e) {
            status=false;
        } catch (Exception e) {
            e.printStackTrace();
            status=false;
        }
        return status;
    }
}

