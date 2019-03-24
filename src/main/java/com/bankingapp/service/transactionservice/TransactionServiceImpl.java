package com.bankingapp.service.transactionservice;

import com.bankingapp.model.request.TransactionRequest;
import com.bankingapp.model.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;


@Component
public class TransactionServiceImpl extends BasicTransactionServiceImpl {

    @Autowired
    EntityManager entityManager;

    private final int TRANSACTION_PENDING = 1;
    private final int TRANSACTION_REJECTED = 3;
    private final int TRANSACTION_APPROVED = 2;

    public List<TransactionRequest> getAllPending(int employeeId) {

        String sql = "SELECT t FROM "+ TransactionRequest.class.getName() +" t where t.status_id = :status_id and t.approved_by = :approved_by";

        Query query = entityManager.createQuery(sql, TransactionRequest.class);
        query.setParameter("status_id", TRANSACTION_PENDING);
        query.setParameter("approved_by", employeeId);
        return query.getResultList();
    }

    public TransactionRequest getPendingTransaction(int transactionId) {

        String sql = "SELECT t FROM "+ TransactionRequest.class.getName() +" t where t.request_id = :request_id";

        Query query = entityManager.createQuery(sql, TransactionRequest.class);
        query.setParameter("request_id", transactionId);
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
            String sql = "update transaction t set t.status = :status where t.id = :id and t.status = :pending_status";
            Query query = entityManager.createQuery(sql);
            query.setParameter("status", TRANSACTION_APPROVED);
            query.setParameter("id", transactionID);
            query.setParameter("pending_status", TRANSACTION_PENDING);;

            return true;
        } catch(Exception e) {
        }

        return false;
    }

    public Boolean rejectTransaction(int transactionID) {

        try {
            String sql = "update transaction t set t.status = :status where t.id = :id and t.status = :pending_status";
            Query query = entityManager.createQuery(sql);
            query.setParameter("status", TRANSACTION_REJECTED);
            query.setParameter("id", transactionID);
            query.setParameter("pending_status", TRANSACTION_PENDING);;

            return true;
        } catch(Exception e) {
        }

        return false;
    }

}
