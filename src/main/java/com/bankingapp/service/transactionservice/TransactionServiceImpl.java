package com.bankingapp.service.transactionservice;

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

    private String TRANSACTION_PENDING = "transaction_pending";
    private String TRANSACTION_REJECTED = "transaction_rejected";
    private String TRANSACTION_APPROVED = "transaction_approved";

    public List<Transaction> getAllPending() {

        String sql = "SELECT t FROM transaction t where t.status = :status";

        Query query = entityManager.createQuery(sql, Transaction.class);
        query.setParameter("status", TRANSACTION_PENDING);
        return query.getResultList();
    }

    public List<Transaction> getAllCompleted() {

        String sql = "SELECT t FROM transaction t where t.status = :status1 or t.status = :status2";

        Query query = entityManager.createQuery(sql, Transaction.class);
        query.setParameter("status1", TRANSACTION_REJECTED);
        query.setParameter("status2", TRANSACTION_APPROVED);
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
