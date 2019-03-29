package com.bankingapp.controller.requestcontroller;

import com.bankingapp.model.account.Customer;
import com.bankingapp.model.employee.Employee;
import com.bankingapp.model.transaction.TransactionResponse;
import com.bankingapp.service.customerservice.CustomerService;
import com.bankingapp.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    CustomerService customerService;

    @Autowired
    RequestUtils requestUtils;

    @RequestMapping(value = "/editCustomerDetails", method = RequestMethod.POST)
    public TransactionResponse changeCustomerDetails(@RequestBody Customer editedCustomer) {

        TransactionResponse transactionResponse = new TransactionResponse();

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

            boolean status = customerService.save(customer);

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
    public TransactionResponse deleteCustomer(@RequestBody int customerId) {

        TransactionResponse transactionResponse = new TransactionResponse();

        return transactionResponse;
    }

    @RequestMapping(value = "/editEmployeeDetails", method = RequestMethod.POST)
    public TransactionResponse changeEmployeeDetails(@RequestBody Customer customer) {

        TransactionResponse transactionResponse = new TransactionResponse();

        return transactionResponse;
    }

    @RequestMapping(value = "/deleteEmployee", method = RequestMethod.POST)
    public TransactionResponse deleteEmployee(@RequestBody Customer customer) {

        TransactionResponse transactionResponse = new TransactionResponse();

        return transactionResponse;
    }

    @RequestMapping(value = "/changeAccountDetails", method = RequestMethod.POST)
    public TransactionResponse changeAccountDetails(@RequestBody Customer customer) {

        TransactionResponse transactionResponse = new TransactionResponse();

        return transactionResponse;
    }

    @RequestMapping(value = "/deleteAccount", method = RequestMethod.POST)
    public TransactionResponse deleteAccount(@RequestBody Customer customer) {

        TransactionResponse transactionResponse = new TransactionResponse();

        return transactionResponse;
    }
}
