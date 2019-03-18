package com.bankingapp.service.customerservice;

import com.bankingapp.model.account.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Component
public class CustomerService {

    @Autowired
    EntityManager entityManager;

    public Customer getCustomer(int id) {
        String sql = "Select e from " + Customer.class.getName() + " e " //
                + " Where e.user_id = :user_id ";

        Query query = entityManager.createQuery(sql, Customer.class);
        query.setParameter("user_id", id);
        return (Customer) query.getSingleResult();
    }

    public List<Customer> getAllCustomers() {
        String sql = "Select e from " + Customer.class.getName() + " e ";
        Query query = entityManager.createQuery(sql, Customer.class);
        return query.getResultList();
    }

}
