package com.bankingapp.service.transactionservice;

import com.bankingapp.model.request.TransactionRequest;
import com.bankingapp.repository.requestrepository.TransactionRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class TransactionRequestService {

    @Autowired
    EntityManager entityManager;

    @Autowired
    TransactionRequestRepository transactionRequestRepository;

    @Transactional
    public boolean saveTransactionRequest(TransactionRequest transactionRequest) {

        try {
            transactionRequestRepository.save(transactionRequest);
            return true;
        } catch(Exception e) {

        }
        return false;
    }

    @Transactional
    public boolean delete(TransactionRequest transactionRequest) {

        try {
            transactionRequestRepository.delete(transactionRequest);
            return  true;
        } catch(Exception e) {

        }
        return false;
    }
}