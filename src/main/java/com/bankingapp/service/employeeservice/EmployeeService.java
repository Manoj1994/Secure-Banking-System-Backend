package com.bankingapp.service.employeeservice;

import com.bankingapp.model.account.Account;
import com.bankingapp.model.employee.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

@Component
public class EmployeeService {

    @Autowired
    EntityManager entityManager;

    public List<Employee> getEmployeeAccounts() {
        String sql = "SELECT d FROM "+ Employee.class.getName() +" d";
        Query query = entityManager.createQuery(sql, Employee.class);
        return query.getResultList();
    }

    public Employee getEmployeeAccount(int employee_id) {
        String sql = "SELECT d FROM "+ Employee.class.getName() +" d where d.employee_id = :employee_id";
        Query query = entityManager.createQuery(sql, Account.class);
        query.setParameter("employee_id", employee_id);

        return (Employee) query.getSingleResult();
    }
}
