package com.bankingapp.service.employeeservice;

import com.bankingapp.model.account.Account;
import com.bankingapp.model.employee.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Random;

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

    public List<Employee> getEmployeesByTier(int tier) {
        String sql = "SELECT d FROM "+ Employee.class.getName() +" d where d.tier_level =: tier ";
        Query query = entityManager.createQuery(sql, Employee.class);
        query.setParameter("tier", tier);
        return query.getResultList();
    }

    public int getRoleOfEmployee(int employee_id) {
        String sql = "SELECT d FROM "+ Employee.class.getName() +" d where d.employee_id = :employee_id ";
        Query query = entityManager.createQuery(sql, Employee.class);
        query.setParameter("employee_id", employee_id);

        Employee employee = (Employee) query.getSingleResult();
        return employee.getTier_level();
    }

    public List<Account> getAllEmployees() {

        String sql = "SELECT d FROM "+ Employee.class.getName() +" d";
        Query query = entityManager.createQuery(sql, Employee.class);
        return query.getResultList();
    }

    public int getTierEmployeeId(int tier) {

        List<Employee> employeeList = getEmployeesByTier(tier);
        if(employeeList.size() == 1) {
            return employeeList.get(0).getEmployee_id();
        } else {
            Random rand = new Random();
            return employeeList.get(rand.nextInt(employeeList.size())).getEmployee_id();
        }
    }
}
