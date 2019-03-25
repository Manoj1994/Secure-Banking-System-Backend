package com.bankingapp.service.accountservice;

import com.bankingapp.model.account.CreditCard;
import com.bankingapp.repository.creditcardrepository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;


import javax.persistence.*;

@Entity
@Table(name = "credit_card")
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
}




