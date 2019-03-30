package com.bankingapp.controller.requestcontroller;

import com.bankingapp.model.account.*;
import com.bankingapp.model.employee.Employee;
import com.bankingapp.model.request.Request;
import com.bankingapp.model.transaction.TransactionResponse;
import com.bankingapp.service.accountservice.CreditCardService;
import com.bankingapp.service.accountservice.DebitCardService;
import com.bankingapp.service.customerservice.CustomerAccountService;
import com.bankingapp.service.customerservice.CustomerService;
import com.bankingapp.service.employeeservice.EmployeeService;
import com.bankingapp.service.loginservice.SessionService;
import com.bankingapp.service.requestservice.RequestService;
import com.bankingapp.utils.RequestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.resource.HttpResource;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/request/employee")
public class EmployeeProcessRequestController {

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

    @Autowired
    EmployeeService employeeService;

    @Autowired
    SessionService sessionService;

    private final int Processed = 2;

    private final int declined = 3;

    private final static int admin = 3;

    private final Double defaultSavingsBalance = 1000.0;

    private final Double defaultCheckingBalance = 500.0;

    private final Double defaultSavingsInterest = 12.0;

    private final Double defaultCheckingInterest = 5.0;

    private int routing_no = 2563;

    private final Double defaultCardLImit = 1000.0;

    @RequestMapping(value = "/getRequests", method = RequestMethod.GET)
    public List<Request> gerRequests() throws Exception{

        if(!sessionService.checkAnyusersExists()) {
            throw new Exception();
        }

        List<Request> requests = new ArrayList<>();
        try {
            requests = requestService.getByAllRequest();

            System.out.println(requests);
            return requests;

        } catch(Exception e) {

        }
        return requests;
    }

    @RequestMapping(value = "tier2/getRequests", method = RequestMethod.GET)
    public List<Request> getTier2Requests(HttpServletResponse httpServletResponse) throws Exception{

        if(!sessionService.checkAnyusersExists()) {
            throw new Exception();
        }

        List<Request> requests = new ArrayList<>();
        try {
            requests = requestService.getByAllRequest();

            List<Request> outputRequests = new ArrayList<Request>();

            for(Request request : requests) {
                if(!request.getRequest_type().equals("Update Employee")) {
                    outputRequests.add(request);
                }
            }

            return outputRequests;

        } catch(Exception e) {

        }
        return requests;
    }

    @RequestMapping(value = "/handleRequest", method = RequestMethod.GET)
    public TransactionResponse gerRequests(@RequestParam("request_id") int request_id, @RequestParam("employee_id") int employee_id, @RequestParam("action") int action)
            throws Exception{

        if(!sessionService.checkAnyusersExists()) {
            throw new Exception();
        }

        TransactionResponse transactionResponse = new TransactionResponse();
        Request request = null;

        try {
            request = requestService.getByID(request_id);
            request.setApproverId(employee_id);

        if(action == 0) {
            request.setStatus("Declined");
            requestService.save(request);

            transactionResponse.setMessage("Transaction is Rejected");
            transactionResponse.setSuccess(true);
            return transactionResponse;

        } else {
            if(request.getRequest_type().equals("Update Customer")) {
                try {
                    Customer customer = (Customer) objectCompressor.fromString(request.getRequested_value());
                    boolean status = customerService.save(customer);

                    if(status) {
                        transactionResponse.setSuccess(true);
                        transactionResponse.setMessage("Request is successful, Created new savings account");
                        request.setStatus("Processed");
                        requestService.save(request);
                    } else {
                        transactionResponse.setSuccess(false);
                        transactionResponse.setMessage("Request is unsuccessful");
                    }
                } catch(Exception e) {
                }

                return transactionResponse;
            } else if(request.getRequest_type().equals("Update Employee")) {

                try {
                    Employee employee = (Employee) objectCompressor.fromString(request.getRequested_value());
                    boolean status = employeeService.save(employee);

                    if(status) {
                        transactionResponse.setSuccess(true);
                        transactionResponse.setMessage("Request is successful, Created new savings account");
                        request.setStatus("Processed");
                        requestService.save(request);
                    } else {
                        transactionResponse.setSuccess(false);
                        transactionResponse.setMessage("Request is unsuccessful");
                    }
                } catch(Exception e) {

                    transactionResponse.setSuccess(false);
                    transactionResponse.setMessage("Request is unsuccessful");
                }

                return transactionResponse;

            } else if(request.getRequest_type().equals("Create Savings Account")) {

                try {
                    int customer_id = request.getRequesterId();
                    Account account = new Account();
                    account.setAccount_type(1);
                    account.setUser_id(customer_id);
                    account.setRouting_no(routing_no);
                    account.setBalance(defaultSavingsBalance);
                    account.setInterest(defaultSavingsInterest);

                    System.out.println(account);

                    boolean status = customerAccountService.save(account);

                    if(status) {
                        transactionResponse.setSuccess(true);
                        transactionResponse.setMessage("Request is successful, Created new savings account");
                        request.setStatus("Processed");
                        requestService.save(request);
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

            } else if(request.getRequest_type().equals("Delete Account")) {

                try {
                    int customer_id = request.getRequesterId();
                    int account_id = Integer.parseInt(request.getRequested_value());

                    boolean status = customerAccountService.delete(account_id);
                    System.out.println("Manoj");

                    if(status) {
                        transactionResponse.setSuccess(true);
                        transactionResponse.setMessage("Request is successful, Deleted the account");
                        request.setStatus("Processed");
                        request.setApproverId(employee_id);
                        requestService.save(request);
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
                    account.setRouting_no(routing_no);
                    account.setBalance(defaultCheckingBalance);
                    account.setInterest(defaultCheckingInterest);

                    boolean status = customerAccountService.save(account);

                    if(status) {
                        transactionResponse.setSuccess(true);
                        transactionResponse.setMessage("Request is successful, Created new checking account");
                        request.setStatus("Processed");
                        requestService.save(request);
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


            } else if(request.getRequest_type().equals("Delete Card")) {

                try {

                    System.out.println(request.getRequested_value());
                    long card_no = Long.parseLong(request.getRequested_value());
                    System.out.println(card_no);

                    boolean cardStatus = debitCardService.checkCard(card_no);
                    boolean status;

                    if(cardStatus) {

                        status = debitCardService.delete(card_no);
                        if(status) {
                            transactionResponse.setSuccess(true);
                            transactionResponse.setMessage("Request is successful, deleted debit card");
                            request.setStatus("Processed");
                            requestService.save(request);
                        } else {
                            transactionResponse.setSuccess(false);
                            transactionResponse.setMessage("Request is unsuccessful");
                        }

                        return transactionResponse;

                    } else {

                        cardStatus = creditCardService.checkCard(card_no);

                        if(cardStatus) {

                            status = creditCardService.delete(card_no);
                            if(status) {
                                transactionResponse.setSuccess(true);
                                transactionResponse.setMessage("Request is successful, deleted credit card");
                                request.setStatus("Processed");
                                requestService.save(request);
                            } else {
                                transactionResponse.setSuccess(false);
                                transactionResponse.setMessage("Request is unsuccessful");
                            }

                            return transactionResponse;

                        } else {

                            transactionResponse.setSuccess(false);
                            transactionResponse.setMessage("Request is unsuccessful, card with this id didn't exist");

                        }
                    }
                } catch(Exception e) {

                    e.printStackTrace();
                    transactionResponse.setSuccess(false);
                    transactionResponse.setMessage("Request ran into exception");
                }
                return transactionResponse;

            } else if(request.getRequest_type().equals("Delete Credit Card")) {

                try {

                    int card_no = Integer.parseInt(request.getRequested_value());
                    boolean status = creditCardService.delete(card_no);

                    if(status) {
                        transactionResponse.setSuccess(true);
                        transactionResponse.setMessage("Request is successful, deleted credit card");
                        request.setStatus("Processed");
                        requestService.save(request);
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

            } else if(request.getRequest_type().equals("Create Debit Card")) {

                try {
                    int customer_id = request.getRequesterId();
                    int account_no = Integer.parseInt(request.getRequested_value());

                    DebitCard debitCard = new DebitCard();
                    debitCard.setAccount_no(account_no);
                    boolean status = debitCardService.addNewDebitCard(debitCard);

                    if(status) {
                        transactionResponse.setSuccess(true);
                        transactionResponse.setMessage("Request is successful, Created new Debit Card");
                        request.setStatus("Processed");
                        requestService.save(request);
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

            } else if(request.getRequest_type().equals("Create Credit Card")) {

                try {
                    int customer_id = request.getRequesterId();
                    int account_no = Integer.parseInt(request.getRequested_value());

                    CreditCard creditCard = new CreditCard();
                    creditCard.setAccount_no(account_no);
                    creditCard.setCard_limit(defaultCardLImit);
                    creditCard.setStatement_balance(0.0);
                    boolean status = creditCardService.addNewCreditCard(creditCard);

                    if(status) {
                        transactionResponse.setSuccess(true);
                        transactionResponse.setMessage("Request is successful, Created new Credit Card");
                        request.setStatus("Processed");
                        requestService.save(request);
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

            }

            }
        } catch (Exception e) {

            transactionResponse.setSuccess(false);
            transactionResponse.setMessage("Request with given id didn't exist");
        }

        return transactionResponse;
    }

    @RequestMapping(value = "/employeeEditRequest", method = RequestMethod.GET)
    public TransactionResponse employeeEditAccountRequest(@RequestParam("customer_id") int user_id,
                                                          @RequestParam("name") String name,
                                                          @RequestParam("contact") String contact,
                                                          @RequestParam("address") String address,
                                                          @RequestParam("email") String email)
            throws Exception{

        if(!sessionService.checkAnyusersExists()) {
            throw new Exception();
        }
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