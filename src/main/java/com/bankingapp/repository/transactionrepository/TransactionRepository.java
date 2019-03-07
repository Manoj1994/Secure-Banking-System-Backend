package com.bankingapp.repository.transactionrepository;

import com.bankingapp.model.login.User;
import com.bankingapp.model.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Set<Transaction> findById(int id);
}
