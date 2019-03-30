package com.bankingapp.service.customerservice;

import com.bankingapp.model.account.Account;
import com.bankingapp.model.account.Customer;
import com.bankingapp.repository.customerrepository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Component
public class CustomerService {

    @Autowired
    EntityManager entityManager;

    @Autowired
    CustomerRepository customerRepository;

    public boolean checkCustomerExists(int id) {
        String sql = "Select e from " + Customer.class.getName() + " e " //
                + " Where e.user_id = :user_id ";

        Query query = entityManager.createQuery(sql, Customer.class);
        query.setParameter("user_id", id);

        if(query.getResultList().size() == 1) {
            return true;
        } else {
            return false;
        }
    }

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

    public List<Account> getAllAccounts(int customerId) {
        String sql = "Select e from " + Account.class.getName() + " e where e.user_id = :user_id";
        Query query = entityManager.createQuery(sql, Account.class);

        query.setParameter("user_id", customerId);
        return query.getResultList();
    }

    public boolean save(Customer customer) {
         try {
             customerRepository.save(customer);
             return true;
         } catch(Exception e) {

         }
         return false;
    }

    public boolean delete(int id) {
        try {
            Customer customer = getCustomer(id);
            customerRepository.delete(customer);
            return true;
        } catch(Exception e) {

        }
        return false;
    }

}
