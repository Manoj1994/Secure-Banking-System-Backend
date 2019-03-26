package com.bankingapp.controller.employeecontroller;

import com.bankingapp.model.account.Account;
import com.bankingapp.model.account.AccountResponse;
import com.bankingapp.model.account.Customer;
import com.bankingapp.model.employee.Employee;
import com.bankingapp.service.customerservice.CustomerAccountService;
import com.bankingapp.service.employeeservice.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    CustomerAccountService customerAccountService;

    @RequestMapping("/getAllEmployees")
    public List<Employee> getAllEmployees() {

        List<Employee> employeeList = new ArrayList<>();
        try{

            employeeList = employeeService.getEmployeeAccounts();

            return employeeList;

        }catch(Exception e){
        }

        return employeeList;
    }

    @RequestMapping("/getAllEmployee")
    public Employee getAllEmployees(int employee_id) {

        try{
            return employeeService.getEmployeeAccount(employee_id);

        }catch(Exception e){
        }

        return null;
    }

    @RequestMapping("/getCustomerAccounts")
    public List<Customer> getAllUserAccounts() {

        List<Customer> customers = new ArrayList<>();
        try{
            customers = customerAccountService.getAllCustomers();
            return customers;

        }catch(Exception e){
        }

        return customers;
    }

    @RequestMapping("/getEmployeeAccounts")
    public List<Employee> getAllEmployeeAccounts() {

        List<Employee> employees = new ArrayList<>();
        try{
            employees = employeeService.getEmployeeAccounts();
            return employees;

        }catch(Exception e){
        }

        return employees;
    }
}
