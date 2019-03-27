package com.bankingapp.controller.requestcontroller;

import com.bankingapp.model.account.Customer;
import com.bankingapp.model.account.ObjectCompressor;
import com.bankingapp.model.request.Request;
import com.bankingapp.model.transaction.TransactionResponse;
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
    RequestUtils requestUtils;

    private final int Processed = 2;
    private final int declined = 3;
    private final static int admin = 3;

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
    public Boolean gerRequests(@RequestParam("request_id") int request_id, @RequestParam("employee_id") int employee_id, @RequestParam("action") boolean action) {

        Request request = requestService.getByID(request_id);
        if(!action) {

            request.setStatus("Declined");
            requestService.save(request);
        } else {


            if(request.getRequest_type().equals("Update Customer")) {
                try {
                    Customer customer = (Customer) objectCompressor.fromString(request.getRequested_value());
                    System.out.println(customer);
                    boolean requestStatus = customerService.save(customer);
                    requestService.delete(request);

                } catch(Exception e) {

                }

            } else if(request.getRequest_type().equals("Update Employee")) {

            } else if(request.getRequest_type().equals("Create Customer")) {

            } else if(request.getRequest_type().equals("Create Employee")) {

            } else if(request.getRequest_type().equals("Create Account")) {

            } else if(request.getRequest_type().equals("Create Account")) {

            } else if(request.getRequest_type().equals("Create DebitCard")) {

            } else if(request.getRequest_type().equals("Create CreditCard")) {

            }

            request.setStatus("Processed");
            requestService.save(request);
        }
        return true;
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