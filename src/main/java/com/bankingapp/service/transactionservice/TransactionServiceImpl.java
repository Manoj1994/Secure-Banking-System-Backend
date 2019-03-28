package com.bankingapp.service.transactionservice;

import com.bankingapp.model.request.TransactionRequest;
import com.bankingapp.model.transaction.Transaction;
import com.bankingapp.repository.requestrepository.TransactionRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Component
public class TransactionServiceImpl extends BasicTransactionServiceImpl {

    @Autowired
    EntityManager entityManager;

    @Autowired
    TransactionRequestRepository transactionRequestRepository;

    private final int TRANSACTION_PENDING = 1;
    private final int TRANSACTION_APPROVED = 2;
    private final int TRANSACTION_REJECTED = 3;

    public List<TransactionRequest> getAllPending(boolean critical) {

        String sql;
        if(!critical) {
            sql = "SELECT t FROM "+ TransactionRequest.class.getName() +" t where t.critical =:critical";
            Query query = entityManager.createQuery(sql, TransactionRequest.class);
            query.setParameter("critical", critical);
            return query.getResultList();
        } else {
            sql = "SELECT t FROM "+ TransactionRequest.class.getName() +" t where t.status_id = :status_id and c";
            Query query = entityManager.createQuery(sql, TransactionRequest.class);
            return query.getResultList();
        }
    }

    public List<Transaction> getAllTransactions(int accountId) {

        String sql = "SELECT t FROM "+ Transaction.class.getName() +" t where t.account_no = :account_no";

        Query query = entityManager.createQuery(sql, Transaction.class);
        query.setParameter("account_no", accountId);
        return query.getResultList();
    }

    public TransactionRequest getPendingTransaction(int transactionId) {

        String sql = "SELECT t FROM "+ TransactionRequest.class.getName() +" t where t.request_id = :request_id and t.status_id = :status_id";

        Query query = entityManager.createQuery(sql, TransactionRequest.class);
        query.setParameter("request_id", transactionId);
        query.setParameter("status_id", TRANSACTION_PENDING);
        return (TransactionRequest) query.getSingleResult();
    }

    public List<Transaction> getAllApproved() {

        String sql = "SELECT t FROM "+ TransactionRequest.class.getName() +" t where t.status_id = :status_id";

        Query query = entityManager.createQuery(sql, TransactionRequest.class);
        query.setParameter("status_id", TRANSACTION_APPROVED);
        return query.getResultList();
    }

    public Boolean approveTransaction(int transactionID) {

        try {

            TransactionRequest transactionRequest = getPendingTransaction(transactionID);
            transactionRequest.setStatus_id(TRANSACTION_APPROVED);
            transactionRequestRepository.save(transactionRequest);

            return true;
        } catch(Exception e) {
        }

        return false;
    }

    public Boolean rejectTransaction(int transactionID) {

        try {

            TransactionRequest transactionRequest = getPendingTransaction(transactionID);
            transactionRequest.setStatus_id(TRANSACTION_REJECTED);
            transactionRequestRepository.save(transactionRequest);

            return true;
        } catch(Exception e) {
        }

        return false;
    }

}
