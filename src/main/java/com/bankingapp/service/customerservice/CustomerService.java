package com.bankingapp.service.customerservice;

import com.bankingapp.model.account.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;

@Component
public class CustomerService {

    @Autowired
    EntityManager entityManager;

    public Customer getCustomer(String id) {
        String sql = "Select e from " + Customer.class.getName() + " e " //
                + " Where e.id = :id ";

        Query query = entityManager.createQuery(sql, Customer.class);
        query.setParameter("id", id);
        return (Customer) query.getSingleResult();
    }

}
