package com.bankingapp.controller.requestcontroller;

import com.bankingapp.model.account.Customer;
import com.bankingapp.model.account.ObjectCompressor;
import com.bankingapp.model.request.Request;
import com.bankingapp.model.transaction.TransactionResponse;
import com.bankingapp.service.customerservice.CustomerService;
import com.bankingapp.service.employeeservice.EmployeeService;
import com.bankingapp.service.requestservice.RequestService;
import com.bankingapp.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/request/customer")
public class CustomerRequestController {

    @Autowired
    RequestService requestService;

    @Autowired
    CustomerService customerService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    ObjectCompressor objectCompressor;

    @Autowired
    RequestUtils requestUtils;

    private int admin = 3;

    @RequestMapping(value = "/customerEditRequest", method = RequestMethod.GET)
    public TransactionResponse customerEditAccountRequest(@RequestParam("customer_id") int user_id,
                                                          @RequestParam("name") String name,
                                                          @RequestParam("contact") String contact,
                                                          @RequestParam("address") String address,
                                                          @RequestParam("email") String email)
    {
        TransactionResponse transactionResponse = new TransactionResponse();

        Customer customer = customerService.getCustomer(user_id);

        System.out.println(customer);
        if(customer == null) {
            transactionResponse.setSuccess(false);
            transactionResponse.setMessage("Sorry! Customer with account id is not available! ");
        }

        try{

            if(!requestUtils.validateEmail(email)) {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your Email is not valid! ");

                return transactionResponse;
            }

            if(name.length() <= 5) {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your name is should be atleast 5 characters long! ");

                return transactionResponse;
            }

            if(!requestUtils.validateContact(contact)) {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your contact is not valid! ");

                return transactionResponse;
            }

            Customer newCustomer = (Customer) customer.clone();
            newCustomer.setAddress(address);
            newCustomer.setContact(contact);
            newCustomer.setEmail(email);
            newCustomer.setName(name);

            if(customer.shortEquals(newCustomer)) {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! You didn't modify any details! ");
            }

            String customerString = objectCompressor.toString(newCustomer);

            Request request=new Request();
            request.setRequesterId(user_id);
            request.setRequest_type("Update Customer");
            request.setRequested_value(customerString);
            request.setDescription("Changing Details of Customer");
            request.setStatus("Pending");
            request.setApproverId(admin);

            System.out.println(request.toString());

            if (!requestService.add_new_request(request)) {
                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry! Your request is not valid!");
                return transactionResponse;
            }
            else{
                transactionResponse.setSuccess(true);
                transactionResponse.setMessage("The request was successfully added to list of Pending requests");
            }
        }catch(Exception e){

            transactionResponse.setSuccess(false);
            transactionResponse.setMessage("Sorry! Your request has." + " ran into Exception!");
        }
        return transactionResponse;
    }

    @RequestMapping(value = "/createNewAccount", method = RequestMethod.GET)
    public TransactionResponse customerCreateNewAccount(@RequestParam("customer_id") int user_id,
                                                          @RequestParam("type") String type) {
        TransactionResponse transactionResponse = new TransactionResponse();
        try {
            Request request = new Request();
            request.setRequesterId(user_id);

            if (type.equals("Savings")) {
                request.setRequest_type("Create Savings Account");
            } else if (type.equals("Checking")) {
                request.setRequest_type("Create Checking Account");
            }
            request.setRequested_value("Savings");
            request.setDescription("Create Account");
            request.setStatus("Pending");
            request.setApproverId(admin);

            transactionResponse.setSuccess(true);
            transactionResponse.setMessage("The request was successfully added to list of Pending requests");

            return transactionResponse;
        } catch(Exception e) {

        }
        return transactionResponse;
    }

    @RequestMapping(value = "/createNewCard", method = RequestMethod.GET)
    public TransactionResponse customerCreateNewCard(@RequestParam("customer_id") int user_id,
                                                        @RequestParam("type") String type) {
        TransactionResponse transactionResponse = new TransactionResponse();
        try {
            Request request = new Request();
            request.setRequesterId(user_id);

            if (type.equals("Debit")) {
                request.setRequest_type("Create Debit Card");
            } else if (type.equals("Credit")) {
                request.setRequest_type("Create Credit Card");
            } else {

                transactionResponse.setSuccess(false);
                transactionResponse.setMessage("Sorry!, Wrong type selected");
                return transactionResponse;
            }

            request.setRequested_value("Savings");
            request.setDescription("Create Account");
            request.setStatus("Pending");
            request.setApproverId(admin);

            transactionResponse.setSuccess(true);
            transactionResponse.setMessage("The request was successfully added to list of Pending requests");

            return transactionResponse;
        } catch(Exception e) {

        }
        return transactionResponse;
    }

    @RequestMapping(value = "/deleteAccount", method = RequestMethod.GET)
    public TransactionResponse deleteAccountRequest(@RequestParam("account_id") int account_id) {

        TransactionResponse transactionResponse = new TransactionResponse();

        try {

        } catch (Exception e) {

        }

        return transactionResponse;
    }

    @RequestMapping(value = "/deleteCard", method = RequestMethod.GET)
    public TransactionResponse deleteCardRequest(@RequestParam("card_id") int card_id) {

        TransactionResponse transactionResponse = new TransactionResponse();

        try {

        } catch (Exception e) {

        }

        return transactionResponse;
    }
}