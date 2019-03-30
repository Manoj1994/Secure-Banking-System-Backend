package com.bankingapp.controller.requestcontroller;

import com.bankingapp.model.account.Account;
import com.bankingapp.model.account.Customer;
import com.bankingapp.model.account.Id;
import com.bankingapp.model.employee.Employee;
import com.bankingapp.model.transaction.TransactionResponse;
import com.bankingapp.service.accountservice.AccountDetailsService;
import com.bankingapp.service.customerservice.CustomerService;
import com.bankingapp.service.employeeservice.EmployeeService;
import com.bankingapp.service.loginservice.SessionService;
import com.bankingapp.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    CustomerService customerService;

    @Autowired
    RequestUtils requestUtils;

    @Autowired
    SessionService sessionService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    AccountDetailsService accountDetailsService;

    @RequestMapping(value = "/editCustomerDetails", method = RequestMethod.POST)
    public TransactionResponse changeCustomerDetails(@RequestBody Customer editedCustomer) throws Exception {

        TransactionResponse transactionResponse = new TransactionResponse();
        if(!sessionService.checkAnyusersExists()) {
            throw new Exception();
        }

        try {
            Customer customer = customerService.getCustomer(editedCustomer.getUser_id());

            if(customer.equals(editedCustomer)) {
                transactionResponse.setSuccess(true);
                transactionResponse.setMessage("You haven't modified any details");
                return transactionResponse;
            }

            if(!requestUtils.validateEmail(editedCustomer.getEmail())) {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Email is not valid! ");

                return transactionResponse;
            }

            if(editedCustomer.getName().length() <= 5) {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Name is should be atleast 5 characters long! ");

                return transactionResponse;
            }

            if(!requestUtils.validateContact(editedCustomer.getContact())) {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Contact is not valid! ");

                return transactionResponse;
            }

            boolean status = customerService.save(editedCustomer);

            if(status) {

                transactionResponse.setSuccess(true);
                transactionResponse.setMessage("Customer Details are Changed");

            } else {

                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Internal Server Error!");
                return transactionResponse;
            }


        } catch(Exception e) {

            transactionResponse.setSuccess(false);
            transactionResponse.setMessage("Sorry! Your request has ran into Exception!");
        }
        return transactionResponse;
    }

    @RequestMapping(value = "/deleteCustomer", method = RequestMethod.POST)
    public TransactionResponse deleteCustomer(@RequestBody Id id) throws Exception {

        if (!sessionService.checkAnyusersExists()) {
            throw new Exception();
        }
        TransactionResponse transactionResponse = new TransactionResponse();
        try {
            boolean status = customerService.delete(id.getId());
            if (status) {
                transactionResponse.setSuccess(true);
                transactionResponse.setMessage("Customer Account is deleted");
            } else {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Internal Server Error!");
                return transactionResponse;
            }
        } catch (Exception e) {
            transactionResponse.setSuccess(false);
            transactionResponse.setMessage("Sorry! Your request has ran into Exception!");
        }
        return transactionResponse;
    }

    @RequestMapping(value = "/editEmployeeDetails", method = RequestMethod.POST)
    public TransactionResponse changeEmployeeDetails(@RequestBody Employee editedEmployee) throws Exception{
        if(!sessionService.checkAnyusersExists()) {
            throw new Exception();
        }
        TransactionResponse transactionResponse = new TransactionResponse();
        try {
             Employee employee = employeeService.getEmployeeAccount(editedEmployee.getEmployee_id());
            if(employee.equals(editedEmployee)) {
                transactionResponse.setSuccess(true);
                transactionResponse.setMessage("You haven't modified any details");
                return transactionResponse;
            }

            if(!requestUtils.validateEmail(editedEmployee.getEmail_id())) {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Email is not valid! ");
                return transactionResponse;
            }

            if(editedEmployee.getEmployee_name().length() <= 5) {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Name is should be atleast 5 characters long! ");
                return transactionResponse;
            }

            if(!requestUtils.validateContact(editedEmployee.getContact_no())) {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Contact is not valid! ");
                return transactionResponse;
            }
            boolean status = employeeService.save(editedEmployee);
            if(status) {
                transactionResponse.setSuccess(true);
                transactionResponse.setMessage("Employee Details are Changed");
            } else {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Internal Server Error!");
                return transactionResponse;
            }
        } catch(Exception e) {

            transactionResponse.setSuccess(false);
            transactionResponse.setMessage("Sorry! Your request has ran into Exception!");
        }
        return transactionResponse;
    }

    @RequestMapping(value = "/deleteEmployee", method = RequestMethod.POST)
    public TransactionResponse deleteEmployee(@RequestBody Id id) throws Exception{
        if(!sessionService.checkAnyusersExists()) {
            throw new Exception();
        }
        TransactionResponse transactionResponse = new TransactionResponse();
        try {
            boolean status = employeeService.delete(id.getId());
            if(status) {
                transactionResponse.setSuccess(true);
                transactionResponse.setMessage("Employee Account is deleted");
            } else {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Internal Server Error!");
                return transactionResponse;
            }
        } catch(Exception e) {
            transactionResponse.setSuccess(false);
            transactionResponse.setMessage("Sorry! Your request has ran into Exception!");
        }
        return transactionResponse;
    }

    @RequestMapping(value = "/changeAccountDetails", method = RequestMethod.POST)
    public TransactionResponse changeAccountDetails(@RequestBody Account editedAccount) throws Exception{
        TransactionResponse transactionResponse = new TransactionResponse();
        if(!sessionService.checkAnyusersExists()) {
            throw new Exception();
        }
        try {
            Account account = accountDetailsService.getAccount(editedAccount.getAccount_no());
            if(account.equals(editedAccount)) {
                transactionResponse.setSuccess(true);
                transactionResponse.setMessage("You haven't modified any details");
                return transactionResponse;
            }
            if(editedAccount.getRouting_no() == 2563) {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Wrong routing number!");
                return transactionResponse;
            }
            boolean status = accountDetailsService.save(account);
            if(status) {
                transactionResponse.setSuccess(true);
                transactionResponse.setMessage("Account Details are Changed");
            } else {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Internal Server Error!");
                return transactionResponse;
            }
        } catch(Exception e) {

            transactionResponse.setSuccess(false);
            transactionResponse.setMessage("Sorry! Your request has ran into Exception!");
        }
        return transactionResponse;
    }

    @RequestMapping(value = "/deleteAccount", method = RequestMethod.POST)
    public TransactionResponse deleteAccount(@RequestBody Id id) throws Exception{
        if(!sessionService.checkAnyusersExists()) {
            throw new Exception();
        }
        TransactionResponse transactionResponse = new TransactionResponse();
        try {
            boolean status = accountDetailsService.delete(id.getId());
            if(status) {
                transactionResponse.setSuccess(true);
                transactionResponse.setMessage("Account is deleted");
            } else {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Internal Server Error!");
                return transactionResponse;
            }
        } catch(Exception e) {
            transactionResponse.setSuccess(false);
            transactionResponse.setMessage("Sorry! Your request has ran into Exception!");
        }
        return transactionResponse;
    }

    @RequestMapping(value = "/createAccount", method = RequestMethod.POST)
    public TransactionResponse createAccount(@RequestBody Account account) throws Exception{
        if(!sessionService.checkAnyusersExists()) {
            throw new Exception();
        }
        TransactionResponse transactionResponse = new TransactionResponse();
        try {
            List<Account> accountList = new ArrayList<>();
            accountList = accountDetailsService.getAccounts();
            for(Account account1 : accountList) {
                if(account.equals(account1)) {
                    transactionResponse.setSuccess(false);
                    transactionResponse.setMessage("Sorry! Can't create another Account with Similar Details!");
                    return transactionResponse;
                }
            }
            boolean status = accountDetailsService.save(account);
            if(status) {
                transactionResponse.setSuccess(true);
                transactionResponse.setMessage("Employee Account is created");
            } else {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Internal Server Error!");
                return transactionResponse;
            }
        } catch(Exception e) {
            transactionResponse.setSuccess(false);
            transactionResponse.setMessage("Sorry! Your request has ran into Exception!");
        }
        return transactionResponse;
    }

    @RequestMapping(value = "/createCustomer", method = RequestMethod.POST)
    public TransactionResponse createCustomer(@RequestBody Customer customer) throws Exception{
        if(!sessionService.checkAnyusersExists()) {
            throw new Exception();
        }
        TransactionResponse transactionResponse = new TransactionResponse();
        try {


            if(!requestUtils.validateEmail(customer.getEmail())) {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Email is not valid! ");

                return transactionResponse;
            }

            if(customer.getName().length() <= 5) {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Name is should be atleast 5 characters long! ");

                return transactionResponse;
            }

            if(!requestUtils.validateContact(customer.getContact())) {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Contact is not valid! ");

                return transactionResponse;
            }

            if(!requestUtils.validateGender(customer.getGender())) {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Gender is not valid, should be in single letter ! ");

                return transactionResponse;
            }

            if(!requestUtils.validateDate(customer.getDob())) {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Date is not valid!");

                return transactionResponse;
            }

            List<Customer> customerList = new ArrayList<>();
            customerList = customerService.getAllCustomers();
            System.out.println(customerList);
            for(Customer customer1 : customerList) {
                if(customer.equals(customer1)) {
                    transactionResponse.setSuccess(false);
                    transactionResponse.setMessage("Sorry! Can't create another customer with Similar Details!");
                    return transactionResponse;
                }
            }
            boolean status = customerService.save(customer);
            if(status) {
                transactionResponse.setSuccess(true);
                transactionResponse.setMessage("Customer Account is created");
            } else {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Internal Server Error!");
                return transactionResponse;
            }
        } catch(Exception e) {

            e.printStackTrace();
            transactionResponse.setSuccess(false);
            transactionResponse.setMessage("Sorry! Your request has ran into Exception!");
        }
        return transactionResponse;
    }

    @RequestMapping(value = "/createEmployee", method = RequestMethod.POST)
    public TransactionResponse createEmployee(@RequestBody Employee employee) throws Exception{
        if(!sessionService.checkAnyusersExists()) {
            throw new Exception();
        }
        TransactionResponse transactionResponse = new TransactionResponse();


        if(!requestUtils.validateEmail(employee.getEmail_id())) {
            transactionResponse.setSuccess(false);
            transactionResponse.setMessage("Sorry! Email is not valid! ");

            return transactionResponse;
        }

        if(employee.getEmployee_name().length() <= 5) {
            transactionResponse.setSuccess(false);
            transactionResponse.setMessage("Sorry! Name is should be atleast 5 characters long! ");

            return transactionResponse;
        }

        if(!requestUtils.validateContact(employee.getContact_no())) {
            transactionResponse.setSuccess(false);
            transactionResponse.setMessage("Sorry! Contact is not valid! ");

            return transactionResponse;
        }

        if(!requestUtils.validateGender(employee.getGender())) {
            transactionResponse.setSuccess(false);
            transactionResponse.setMessage("Sorry! Gender is not valid, should be in single letter ! ");

            return transactionResponse;
        }

        if(!requestUtils.validateAge(employee.getAge())) {
            transactionResponse.setSuccess(false);
            transactionResponse.setMessage("Sorry! Date is not valid!");

            return transactionResponse;
        }
        try {
            List<Employee> employeeList = new ArrayList<>();
            employeeList = employeeService.getEmployeeAccounts();
            for(Employee employee1 : employeeList) {
                if(employee.equals(employee1)) {
                    transactionResponse.setSuccess(false);
                    transactionResponse.setMessage("Sorry! Can't create another Employee with Similar Details!");
                    return transactionResponse;
                }
            }

            employee.setDesignation_id(employee.getTier_level());
            boolean status = employeeService.save(employee);
            if(status) {
                transactionResponse.setSuccess(true);
                transactionResponse.setMessage("Employee Account is created");
            } else {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Internal Server Error!");
                return transactionResponse;
            }
        } catch(Exception e) {
            transactionResponse.setSuccess(false);
            transactionResponse.setMessage("Sorry! Your request has ran into Exception!");
        }
        return transactionResponse;
    }
}
