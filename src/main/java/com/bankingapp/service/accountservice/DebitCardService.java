package com.bankingapp.service.accountservice;

import com.bankingapp.model.account.DebitCard;
import com.bankingapp.repository.debitcardrepository.DebitCardRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;



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
}

