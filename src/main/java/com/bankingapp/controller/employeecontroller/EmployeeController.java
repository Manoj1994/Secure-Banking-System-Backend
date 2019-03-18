package com.bankingapp.controller.employeecontroller;

import com.bankingapp.model.account.Account;
import com.bankingapp.model.account.AccountResponse;
import com.bankingapp.model.employee.Employee;
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
}
