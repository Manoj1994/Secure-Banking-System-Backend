package com.bankingapp.controller.requestcontroller;

import com.bankingapp.model.account.Customer;
import com.bankingapp.model.account.ObjectCompressor;
import com.bankingapp.model.request.Request;
import com.bankingapp.service.customerservice.CustomerService;
import com.bankingapp.service.requestservice.RequestService;
import com.bankingapp.service.transactionservice.TransactionRequestService;
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

    private final int Processed = 2;
    private final int declined = 3;

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

}