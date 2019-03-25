package com.bankingapp.repository.requestrepository;

import com.bankingapp.model.request.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
