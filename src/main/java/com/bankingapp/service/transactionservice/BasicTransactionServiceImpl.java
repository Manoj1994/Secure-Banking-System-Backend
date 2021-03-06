package com.bankingapp.service.transactionservice;

import com.bankingapp.model.transaction.Transaction;
import com.bankingapp.repository.transactionrepository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class BasicTransactionServiceImpl implements TransactionService {

    @Autowired
    EntityManager entityManager;

    @Autowired
    TransactionRepository transactionRepository;

    @Transactional
    public Boolean save(Transaction transaction) {

        try {
            transactionRepository.save(transaction);
            return true;
        } catch(Exception e) {

        }
        return false;
    }

    @Transactional
    public List<Transaction> getById(int id) {

        String sql = "Select e from " + Transaction.class.getName() + " e " //
                + " Where e.id = :id ";

        Query query = entityManager.createQuery(sql, Transaction.class);
        query.setParameter("id", id);
        return query.getResultList();
    }

    @Transactional
    public void update(Transaction employer) {

        String sql = "update Transaction t set t.status = :status where t.name = :name";
        entityManager.createQuery(sql);
    }

    @Transactional
    public boolean deleteById(int id,String type) {

        try {

            String sql = "delete from Transaction t here t.id = :id";
            Query query = entityManager.createQuery(sql);
            query.setParameter("id", id);
            return true;
        } catch(Exception e) {

        }
        return false;
    }
}
