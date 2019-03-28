package com.bankingapp.controller.requestcontroller;

import com.bankingapp.model.account.Account;
import com.bankingapp.model.account.Customer;
import com.bankingapp.model.account.ObjectCompressor;
import com.bankingapp.model.request.Request;
import com.bankingapp.model.transaction.TransactionResponse;
import com.bankingapp.service.accountservice.CreditCardService;
import com.bankingapp.service.accountservice.DebitCardService;
import com.bankingapp.service.customerservice.CustomerAccountService;
import com.bankingapp.service.customerservice.CustomerService;
import com.bankingapp.service.requestservice.RequestService;
import com.bankingapp.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/request/employee")
public class EmployeeRequestController {

    @Autowired
    RequestService requestService;

    @Autowired
    ObjectCompressor objectCompressor;

    @Autowired
    CustomerService customerService;

    @Autowired
    CustomerAccountService customerAccountService;

    @Autowired
    DebitCardService debitCardService;

    @Autowired
    CreditCardService creditCardService;

    @Autowired
    RequestUtils requestUtils;

    private final int Processed = 2;

    private final int declined = 3;

    private final static int admin = 3;

    private final Double defaultSavingsBalance = 1000.0;

    private final Double defaultCheckingBalance = 500.0;

    private final Double defaultSavingsInterest = 12.0;

    private final Double defaultCheckingInterest = 5.0;

    @RequestMapping(value = "/getRequests", method = RequestMethod.GET)
    public List<Request> gerRequests() {

        List<Request> requests = new ArrayList<>();
        try {
            requests = requestService.getByAllRequest();

            System.out.println(requests);
            return requests;

        } catch(Exception e) {

        }
        return requests;
    }

    @RequestMapping(value = "/handleRequest", method = RequestMethod.GET)
    public TransactionResponse gerRequests(@RequestParam("request_id") int request_id, @RequestParam("employee_id") int employee_id, @RequestParam("action") boolean action) {

        Request request = requestService.getByID(request_id);
        TransactionResponse transactionResponse = new TransactionResponse();

        if(!action) {
            request.setStatus("Declined");
            requestService.save(request);

        } else {
            if(request.getRequest_type().equals("Update Customer")) {
                try {
                    Customer customer = (Customer) objectCompressor.fromString(request.getRequested_value());
                    System.out.println(customer);

                    boolean requestStatus = customerService.save(customer);

                    request.setStatus("Processed");
                    requestService.save(request);

                } catch(Exception e) {

                }

            } else if(request.getRequest_type().equals("Update Employee")) {

            } else if(request.getRequest_type().equals("Create Savings Account")) {

                try {
                    int customer_id = request.getRequesterId();
                    Account account = new Account();
                    account.setAccount_type(1);
                    account.setUser_id(customer_id);
                    account.setBalance(defaultSavingsBalance);
                    account.setInterest(defaultSavingsInterest);

                    boolean status = customerAccountService.save(account);

                    if(status) {
                        transactionResponse.setSuccess(true);
                        transactionResponse.setMessage("Request is successful, Created new savings account");
                    } else {
                        transactionResponse.setSuccess(false);
                        transactionResponse.setMessage("Request is unsuccessful");
                    }

                    return transactionResponse;

                } catch(Exception e) {
                    transactionResponse.setSuccess(false);
                    transactionResponse.setMessage("Request ran into exception");
                }
                return transactionResponse;

            } else if(request.getRequest_type().equals("Create Checking Account")) {

                try {
                    int customer_id = request.getRequesterId();
                    Account account = new Account();
                    account.setAccount_type(2);
                    account.setUser_id(customer_id);
                    account.setBalance(defaultCheckingBalance);
                    account.setInterest(defaultCheckingInterest);

                    boolean status = customerAccountService.save(account);

                    if(status) {
                        transactionResponse.setSuccess(true);
                        transactionResponse.setMessage("Request is successful, Created new checking account");
                    } else {
                        transactionResponse.setSuccess(false);
                        transactionResponse.setMessage("Request is unsuccessful");
                    }

                    return transactionResponse;

                } catch(Exception e) {
                    transactionResponse.setSuccess(false);
                    transactionResponse.setMessage("Request ran into exception");
                }

                return transactionResponse;


            } else if(request.getRequest_type().equals("Delete DebitCard")) {

                try {

                    int card_no = Integer.parseInt(request.getRequested_value());
                    boolean status = debitCardService.delete(card_no);

                    if(status) {
                        transactionResponse.setSuccess(true);
                        transactionResponse.setMessage("Request is successful, deleted debit card");
                    } else {
                        transactionResponse.setSuccess(false);
                        transactionResponse.setMessage("Request is unsuccessful");
                    }

                    return transactionResponse;

                } catch(Exception e) {
                    transactionResponse.setSuccess(false);
                    transactionResponse.setMessage("Request ran into exception");
                }
                return transactionResponse;

            } else if(request.getRequest_type().equals("Delete CreditCard")) {

            } else if(request.getRequest_type().equals("Create DebitCard")) {

            } else if(request.getRequest_type().equals("Create CreditCard")) {

            }

        }

        return transactionResponse;
    }

    @RequestMapping(value = "/employeeEditRequest", method = RequestMethod.GET)
    public TransactionResponse employeeEditAccountRequest(@RequestParam("customer_id") int user_id,
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

}