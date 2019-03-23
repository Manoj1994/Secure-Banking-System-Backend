package com.bankingapp.repository.requestrepository;

import com.bankingapp.model.request.TransactionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRequestRepository extends JpaRepository<TransactionRequest, Long> {
}
