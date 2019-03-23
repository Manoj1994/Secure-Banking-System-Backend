package com.bankingapp.service.transactionservice;

import com.bankingapp.model.request.TransactionRequest;
import com.bankingapp.repository.requestrepository.TransactionRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionRequestService {

    @Autowired
    EntityManager entityManager;

    @Autowired
    TransactionRequestRepository transactionRequestRepository;

    public boolean saveTransactionRequest(TransactionRequest transactionRequest) {

        try {
            transactionRequestRepository.save(transactionRequest);
            return true;
        } catch(Exception e) {

        }
        return false;
    }
}