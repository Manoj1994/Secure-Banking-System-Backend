package com.bankingapp.repository.customerrepository;

import com.bankingapp.model.account.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
